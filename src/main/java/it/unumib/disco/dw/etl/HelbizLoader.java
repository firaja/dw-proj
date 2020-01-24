package it.unumib.disco.dw.etl;

import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.db.LocalDatabaseManager;
import it.unumib.disco.dw.etl.model.HelbizRegion;
import it.unumib.disco.dw.etl.model.HelbizVehicle;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final static Logger LOG = LogManager.getLogger(HelbizLoader.class);

    @PreDestroy
    public void onDestroy()
    {
        lDbManager.close();
    }

    public void loadVehiclesPositions(List<HelbizVehicle> vlst)
    {
        if (vlst.size() == 0)
        {
            return;
        }

        LOG.info("Loading {} vehicles on database for city {}", vlst.size(), vlst.get(0).getGeofence());

        String date = sdf.format(new Date());

        StringBuilder str = new StringBuilder();

        vlst.stream().forEach(v -> {
            if (str.length() != 0)
            {
                str.append(",");
            }

            str.append(String.format(Locale.US, "('%s', '%s', %f, %f, %d, %d, %d, '%s', '')",
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
        Map.Entry<Statement,ResultSet> pair = lDbManager.query(query);
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

    public List<Date> getAllQueryTimes(HelbizRegion region, Date startDate, Date endDate)
    {
        String query;
        if (startDate != null && endDate != null)
        {
            query = String.format(
                    "SELECT DISTINCT query_time FROM vehicles WHERE city = '%s' AND query_time >= '%s' " +
                    "AND query_time <= '%s' ORDER BY query_time;",
                    region.getName(), sdf.format(startDate), sdf.format(endDate));
        }
        else
        {
            query = String.format(
                    "SELECT DISTINCT query_time FROM vehicles WHERE city = '%s' ORDER BY query_time;",
                    region.getName());
        }
        Map.Entry<Statement,ResultSet> pair = lDbManager.query(query);
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

        Map.Entry<Statement,ResultSet> pair = lDbManager.query(query);
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
