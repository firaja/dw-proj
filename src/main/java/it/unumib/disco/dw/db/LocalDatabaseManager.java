package it.unumib.disco.dw.db;

import it.unumib.disco.dw.Config;
import it.unumib.disco.dw.etl.model.ParsedWeatherDetection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LocalDatabaseManager
{

    private static final Logger LOG = LogManager.getLogger();

    private Connection connection;

    private LocalDatabaseManager()
    {
        //
    }

    public static LocalDatabaseManager getInstance()
    {
        try
        {
            Connection connection = DriverManager.getConnection(Config.Database.URL, Config.Database.USER, Config.Database.PASSWORD);
            LocalDatabaseManager manager = new LocalDatabaseManager();
            manager.connection = connection;
            return manager;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public Map.Entry<Statement, ResultSet> query(String query)
    {
        Statement statement;
        try
        {
            LOG.debug(query);
            statement = this.connection.createStatement();
            return new HashMap.SimpleEntry<>(statement, statement.executeQuery(query));
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }

    public int update(String update)
    {
        try (Statement statement = this.connection.createStatement())
        {
            LOG.debug(update);
            return statement.executeUpdate(update);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }

    }



    public void close()
    {
        try
        {
            this.connection.close();
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }
}
