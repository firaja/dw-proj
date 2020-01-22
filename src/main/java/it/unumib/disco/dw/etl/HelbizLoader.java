package it.unumib.disco.dw.etl;

import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.PreDestroy;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

public class HelbizLoader
{
    private final LocalDatabaseManager lDbManager = LocalDatabaseManager.getInstance();

    private final static Logger LOG = LogManager.getLogger(HelbizLoader.class);

    @PreDestroy
    public void onDestroy()
    {
        lDbManager.close();
    }

    public void loadVehiclesPositions(List<HelbizVehicle> vlst)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(new Date());

        StringBuilder str = new StringBuilder();

        vlst.stream().forEach(v -> {
            if (str.length() != 0)
            {
                str.append(",");
            }

            str.append(String.format(Locale.US, "('%s', '%s', %f, %f, %d, %d, %d, '%s')",
                    v.getId(), v.getGeofence(), v.getLat(), v.getLon(),
                    v.getBatteryLevelInMiles(), Math.round(v.getPower()), v.getRange(),
                    date));
        });

        str.append(";");

        String query = "INSERT INTO vehicles VALUES " + str.toString();
        try
        {
            lDbManager.update(query);
        }
        catch(Exception e)
        {
            LOG.error("An error occurred during load of vehicles on database.");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }
    }

    public List<Date> getLastQueryTimes(HelbizRegion region)
    {
        String query = String.format(
                "SELECT DISTINCT query_time FROM vehicles WHERE city = '%s' ORDER BY city, query_time DESC LIMIT 2",
                region.getName());
        Pair<Statement,ResultSet> pair = lDbManager.query(query);
        List<Date> result = new ArrayList<>();
        try(Statement statement = pair.getKey(); ResultSet resultSet = pair.getValue())
        {
            while(resultSet.next())
            {
                result.add(resultSet.getTimestamp("query_time"));
            }
        }
        catch(SQLException e)
        {
            LOG.error("An error occurred during execution of method getLastQueryTimes");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        return result;
    }

    public void getDiffBetweenProfilings(HelbizRegion region, List<Date> lastTimes)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String query = String.format(
                "SELECT v1.id, v1.city, v1.latitude, v1.longitude, v1.battery_level_miles, v1.power, v1.miles_range, " +
                        "v2.latitude, v2.longitude, v2.battery_level_miles, v2.power, v2.miles_range " +
                        "FROM vehicles v1, vehicles v2 " +
                        "WHERE v1.id = v2.id AND v1.city = v2.city AND v1.query_time = '%s' AND v2.query_time = '%s' " +
                        "AND v1.city = '%s'", sdf.format(lastTimes.get(0)), sdf.format(lastTimes.get(1)),
                region.getName());

        Pair<Statement,ResultSet> pair = lDbManager.query(query);

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

        vprofilingMap.forEach((id, lst) -> {
            LOG.info("{}", lst.get(0));
            LOG.info("{}", lst.get(1));
        });
    }

    public int getTableSize(String tableName)
    {
        /*
        * query useful to get table size if allowed to access to informatio_schema
        *
        * SELECT TABLE_NAME AS `tableName`, ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024) AS `Size (MB)`
        * FROM information_schema.TABLES
        * WHERE TABLE_SCHEMA = "schemaName"
        * ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;
        *
        * */
        String query = String.format(
                "SELECT TABLE_NAME AS `%s`, ROUND((DATA_LENGTH + INDEX_LENGTH) / 1024 / 1024) AS size_mb " +
                "FROM information_schema.TABLES WHERE TABLE_SCHEMA = '%s' " +
                "ORDER BY (DATA_LENGTH + INDEX_LENGTH) DESC;", tableName, Config.Database.NAME);

        Pair<Statement,ResultSet> pair = lDbManager.query(query);
        int size = 0;
        try(Statement statement = pair.getKey(); ResultSet resultSet = pair.getValue())
        {
            if (resultSet.next())
            {
                size = resultSet.getInt("size_mb");
            }

        }
        catch(SQLException e)
        {
            LOG.error("An error occurred during execution of method getTableSize");
            LOG.error("The following exception has been thrown: ", e);
            if (LOG.isDebugEnabled())
            {
                LOG.debug("Dump of query executed: " + query);
            }
        }

        return size;
    }

}
