package it.unumib.disco.dw.etl.transformers;

import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.HelbizProfiling;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import it.unumib.disco.dw.etl.utils.CalculationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.Holder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class HelbizTransformer
{
    private LocalDatabaseManager lDbManager = LocalDatabaseManager.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final Logger LOG = LogManager.getLogger(HelbizTransformer.class);

    private static final String QUERY_SELECT_VEHICLES_COUPLES_BETWEEN_PROFILINGS =
            "SELECT v1.id, v1.city, v1.latitude, v1.longitude, v1.battery_level_miles, v1.power, v1.miles_range, " +
            "v2.latitude, v2.longitude, v2.battery_level_miles, v2.power, v2.miles_range " +
            "FROM vehicles v1, vehicles v2 WHERE v1.id = v2.id AND v1.city = v2.city AND v1.query_time = '%s' " +
            "AND v2.query_time = '%s' AND v1.city = '%s'";
    private static final String QUERY_INSERT_VEHICLES_PROFILINGS = "INSERT INTO vehicles_profilings VALUES ";
    private static final String QUERY_SELECT_NEW_VEHICLES =
            "SELECT id FROM vehicles WHERE city = '%s' AND query_time = '%s' AND id NOT IN (" +
            "SELECT id FROM vehicles WHERE city = '%s' AND query_time = '%s')";
    private static final String QUERY_SELECT_CITY_PROFILINGS =
            "SELECT vehicle_id, start_profiling_time, end_profiling_time, position_changed, new, travelled_distance " +
            "FROM vehicles_profilings WHERE city_id = '%s' ORDER BY vehicle_id, start_profiling_time;";
    private static final String QUERY_SELECT_CITY_PROFILINGS_BETWEEN_DATES =
            "SELECT vehicle_id, start_profiling_time, end_profiling_time, position_changed, new, travelled_distance " +
                    "FROM vehicles_profilings WHERE city_id = '%s' AND start_profiling_time >= '%s' AND " +
                    "end_profiling_time <= '%s' ORDER BY vehicle_id, start_profiling_time;";
    private static final String QUERY_INSERT_VEHICLE_HOURLY_USE = "INSERT INTO vehicle_hourly_use VALUES ";
    private static final String QUERY_INSERT_CITY_HOURLY_USE =
            "INSERT INTO city_hourly_use (city_id, start_time, end_time, total_uses, total_travelled_distance, " +
                    "total_vehicles) SELECT city_id, start_time, end_time, SUM(uses), SUM(travelled_distance), " +
                    "COUNT(vehicle_id) FROM vehicle_hourly_use WHERE city_id = '%s' AND start_time >= '%s' AND " +
                    "end_time <= '%s' GROUP BY start_time, end_time ORDER BY start_time, end_time;";

    public void getDiffBetweenProfilings(HelbizRegion region, List<Date> lastTimes)
    {
        /*
        * Get vehicles present on both the profiling session for which a date has been provided
         */
        LOG.info("Start calculate differences between the following two profilings:");
        LOG.info("city: " + region.getName() + " profiling1_date: " + lastTimes.get(1) + " profiling2_date: " +
                lastTimes.get(0));

        String query = String.format(QUERY_SELECT_VEHICLES_COUPLES_BETWEEN_PROFILINGS,
                sdf.format(lastTimes.get(0)), sdf.format(lastTimes.get(1)), region.getName());

        Map.Entry<Statement, ResultSet> pair = lDbManager.query(query);

        Map<String,List<HelbizVehicle>> vprofilingMap = new HashMap<>();
        try(Statement statement = pair.getKey(); ResultSet resultSet = pair.getValue())
        {
            while(resultSet.next())
            {
                String id = resultSet.getString("v1.id");
                String city = resultSet.getString("v1.city");

                List<HelbizVehicle> vCouple = new ArrayList<>();
                vCouple.add(new HelbizVehicle(id,
                        resultSet.getDouble("v1.latitude"),
                        resultSet.getDouble("v1.longitude"),
                        resultSet.getInt("v1.battery_level_miles"),
                        resultSet.getInt("v1.miles_range"),
                        resultSet.getDouble("v1.power"),
                        city));
                vCouple.add(new HelbizVehicle(id,
                        resultSet.getDouble("v2.latitude"),
                        resultSet.getDouble("v2.longitude"),
                        resultSet.getInt("v2.battery_level_miles"),
                        resultSet.getInt("v2.miles_range"),
                        resultSet.getDouble("v2.power"),
                        city));

                vprofilingMap.put(id, vCouple);
            }
        }
        catch(SQLException e)
        {
            LOG.error("An error occurred during execution of method getDiffBetweenProfilings");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        /*
        * Scan the list of vehicles retrieved above, calculate the travelled distance between two positions and
        * feed the vehicles_profilings table
         */

        StringBuilder str = new StringBuilder();

        vprofilingMap.forEach((id, lst) -> {
            if (str.length() != 0)
            {
                str.append(", ");
            }

            HelbizVehicle v1 = lst.get(0);
            HelbizVehicle v2 = lst.get(1);


            long distance = Math.round(CalculationUtils.calculateDistance(v1.getLat(), v1.getLon(), v2.getLat(),
                    v2.getLon()));

            str.append(String.format("('%s', '%s', '%s', '%s', '%d', '%d', '%d')",
                    v1.getId(),
                    v1.getGeofence(),
                    sdf.format(lastTimes.get(1)),
                    sdf.format(lastTimes.get(0)),
                    distance > 100L ? 1 : 0,
                    0,
                    distance
                    ));
        });

        query = QUERY_INSERT_VEHICLES_PROFILINGS + str.toString() + ";";
        try
        {
            lDbManager.update(query);
        }
        catch(Exception e)
        {
            LOG.error("An error occurred during execution of method getDiffBetweenProfilings");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        getNewVehicles(region, lastTimes);
        LOG.info("End calculate differences between profilings");
    }

    private void getNewVehicles(HelbizRegion region, List<Date> lastTimes)
    {
        /*
        * Analyse the last two profilings in order to obtain the list of vehicles present in the last profiling
        * but not present into the elder one
         */
        String query = String.format(QUERY_SELECT_NEW_VEHICLES, region.getName(), sdf.format(lastTimes.get(0)),
                region.getName(), sdf.format(lastTimes.get(1)));

        List<String> vLst = new ArrayList<>();

        Map.Entry<Statement,ResultSet> pair = lDbManager.query(query);
        try(Statement statement = pair.getKey(); ResultSet resultSet = pair.getValue())
        {
            while (resultSet.next())
            {
                vLst.add(resultSet.getString("id"));
            }
        }
        catch(SQLException e)
        {
            LOG.error("An error occurred during execution of method getNewVehicles");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        if (vLst.size() == 0)
        {
            return;
        }

        StringBuilder str = new StringBuilder();

        vLst.stream().forEach(v -> {
            if (str.length() != 0)
            {
                str.append(", ");
            }

            str.append(String.format("('%s', '%s', '%s', '%s', 0, 1, 0)", v, region.getName(), lastTimes.get(1),
                    lastTimes.get(0)));
        });

        query = QUERY_INSERT_VEHICLES_PROFILINGS + str.toString() + ";";

        try
        {
            lDbManager.update(query);
        }
        catch(Exception e)
        {
            LOG.error("An error occurred during execution of method getNewVehicles");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }
    }

    public void processListOfProfilings(HelbizRegion region, List<Date> profilings)
    {
        LOG.info("Start analyzing last {} profilings", profilings.size());
        if (profilings.size() < 2)
        {
            return;
        }

        Iterator<Date> it = profilings.iterator();
        Date prev = it.next();
        while (it.hasNext())
        {
            LocalDateTime ldt1 = LocalDateTime.ofInstant(prev.toInstant(), ZoneId.systemDefault());
            Date next = it.next();
            LocalDateTime ldt2 = LocalDateTime.ofInstant(next.toInstant(), ZoneId.systemDefault());

            Duration duration = Duration.between(ldt1, ldt2);
            long minDiff = duration.toMinutes();

            LOG.info("Prev profiling: " + sdf.format(prev));
            LOG.info("Next profiling: " + sdf.format(next));
            LOG.info("Difference in minutes between profilings: " + minDiff);

            if (minDiff > 3 && minDiff < 10)
            {
                getDiffBetweenProfilings(region, Arrays.asList(next, prev));
            }
            prev = next;
        }
        LOG.info("End analyzing profilings");
    }

    public List<HelbizProfiling> getProfilings(HelbizRegion region, Date startDate, Date endDate)
    {
        String query;
        if (startDate != null && endDate != null)
        {
            query = String.format(QUERY_SELECT_CITY_PROFILINGS_BETWEEN_DATES, region.getName(), sdf.format(startDate),
                    sdf.format(endDate));
        }
        else
        {
            query = String.format(QUERY_SELECT_CITY_PROFILINGS, region.getName());
        }

        List<HelbizProfiling> pLst = new ArrayList<>();

        Map.Entry<Statement,ResultSet> pair = lDbManager.query(query);
        try(Statement statement = pair.getKey(); ResultSet resultSet = pair.getValue())
        {
            while (resultSet.next())
            {
                pLst.add(new HelbizProfiling(resultSet.getString("vehicle_id"), region.getName(),
                        resultSet.getTimestamp("start_profiling_time"),
                        resultSet.getTimestamp("end_profiling_time"),
                        resultSet.getBoolean("position_changed"), resultSet.getBoolean("new"),
                        resultSet.getInt("travelled_distance")));
            }
        }
        catch (SQLException e)
        {
            LOG.error("An error occurred during execution of method getProfilings");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        return pLst;
    }

    private void loadVehicleHourlyUse(HelbizRegion region, Date startDate, Date endDate)
    {
        List<HelbizProfiling> pList = getProfilings(region, startDate, endDate);

        Map<String,Map<String,Map<String, Holder<Integer>>>> pMap = new HashMap<>();

        pList.stream().forEach(p -> {
            String startDateStr = getStartDateTimeString(p.getStartTime());
            Map<String,Map<String,Holder<Integer>>> iMap = pMap.get(p.getVehicleId());
            if (iMap == null)
            {
                iMap = new HashMap<>();
                pMap.put(p.getVehicleId(), iMap);
            }

            Map<String,Holder<Integer>> iIMap = iMap.get(startDateStr);
            if (iIMap == null)
            {
                Holder<Integer> uses = new Holder<>(new Integer(0));
                Holder<Integer> travelledDistance = new Holder<>(new Integer(0));
                iIMap = new HashMap<>();
                iIMap.put("uses", uses);
                iIMap.put("travelled_distance", travelledDistance);
                iMap.put(startDateStr, iIMap);
            }

            if (p.isPositionChanged() && p.getTravelledDistance() > 100 &&
                    p.getTravelledDistance() <= 4000)
            {
                // travelledDistance different by 0 and less than 4 KM far from previous position
                iIMap.get("uses").value++;
                iIMap.get("travelled_distance").value += p.getTravelledDistance();
            }
        });

        if (pList.size() == 0)
        {
            return;
        }

        StringBuilder str = new StringBuilder();

        pMap.forEach((vId /*vehicleId*/,
                      dtM /*dateTimeMap*/) -> {
            dtM.forEach((dt /*dateTime Map*/,
                         p /*profiling Map*/) -> {
                if (str.length() != 0)
                {
                    str.append(", ");
                }

                int uses = pMap.get(vId).get(dt).get("uses").value;
                int tDist = pMap.get(vId).get(dt).get("travelled_distance").value;

                LocalDateTime dtPlus1 = LocalDateTime.parse(dt, dtf).plusHours(1L);

                str.append(String.format("('%s', '%s', '%s', '%s', %d, %d)", vId, region.getName(),
                        dt, dtPlus1.format(dtf), uses, tDist));
            });
        });

        String query = QUERY_INSERT_VEHICLE_HOURLY_USE + str.toString() + ";";

        try
        {
            lDbManager.update(query);
        }
        catch (Exception e)
        {
            LOG.error("An error occurred during execution of method loadVehicleHourlyUse");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }
    }

    private String getStartDateTimeString(Date date)
    {
        LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        return LocalDateTime.of(ldt.getYear(), ldt.getMonthValue(), ldt.getDayOfMonth(), ldt.getHour(), 0)
                .format(dtf);
    }

    public void feedVehicleHourProfilingTable(HelbizRegion region, Date startDate, Date endDate)
    {
        LocalDateTime start = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());
        LOG.info("Start loading data on vehicle_hourly_use table for profilings");
        // if interval between dates is greater than or equal to 24 hours, proceed day by day
        if (Duration.between(start, end).toDays() >= 1)
        {
            LocalDateTime currentEnd = null;
            while (!start.isAfter(end))
            {
                currentEnd = start.plusHours(24);
                LOG.info("Vehicles Profilings under analysis: [city: {}, startDate: {}, endDate: {}]",
                        region.getName(), start.format(dtf), currentEnd.format(dtf));
                loadVehicleHourlyUse(region, Date.from( start.atZone( ZoneId.systemDefault()).toInstant()),
                        Date.from( currentEnd.atZone( ZoneId.systemDefault()).toInstant()));
                start = currentEnd;
            }
        }
        else
        {
            // otherwise proceed directly
            LOG.info("Vehicles Profilings under analysis: [city: {}, startDate: {}, endDate: {}]",
                    region.getName(), sdf.format(startDate), sdf.format(endDate));
            loadVehicleHourlyUse(region, startDate, endDate);
        }
        LOG.info("End loading data on vehicle_hourly_use table for profilings");
    }

    public void feedCityHourProfilingTable(HelbizRegion region, Date startDate, Date endDate)
    {
        LOG.info("Start loading data on city_hourly_use table for profilings");
        LocalDateTime start = LocalDateTime.ofInstant(startDate.toInstant(), ZoneId.systemDefault());
        LocalDateTime end = LocalDateTime.ofInstant(endDate.toInstant(), ZoneId.systemDefault());

        Duration duration = Duration.between(start, end);

        if (duration.toDays() > 1)
        {
            // if between startDate and endDate there is more than one day,
            // proceed day by day

            while (!start.isAfter(end))
            {
                LocalDateTime currentEnd = start.plusDays(1L);
                feedCityHourProfilingTableInner(region, start, currentEnd);
                start = currentEnd;
            }
        }
        else
        {
            // otherwise proceed directly
            feedCityHourProfilingTableInner(region, start, end);
        }
        LOG.info("End loading data on city_hourly_use table for profilings");
    }

    private void feedCityHourProfilingTableInner(HelbizRegion region, LocalDateTime start, LocalDateTime end)
    {
        LOG.info("Vehicles Profilings under analysis: [city: {}, startDate: {}, endDate: {}]", region.getName(),
                start.format(dtf), end.format(dtf));

        String query = String.format(QUERY_INSERT_CITY_HOURLY_USE, region.getName(), start.format(dtf),
                end.format(dtf));

        try
        {
            lDbManager.update(query);
        }
        catch (Exception e)
        {
            LOG.error("An error occurred during execution of method loadVehicleHourlyUse");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }
    }
}
