package it.unumib.disco.dw.db;

import it.unumib.disco.dw.Config;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LocalDatabaseManager
{

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
            new IllegalStateException(e);
        }
    }
}
