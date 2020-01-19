package it.unumib.disco.dw.db;

import it.unumib.disco.dw.Config;

import java.sql.*;

public class ConnectionManager
{

    private Connection connection;

    private ConnectionManager()
    {
        //
    }

    public static ConnectionManager getInstance()
    {
        try
        {
            Connection connection = DriverManager.getConnection(Config.DB.URL, Config.DB.NAME, Config.DB.PASSWORD);
            ConnectionManager manager = new ConnectionManager();
            manager.connection = connection;
            return manager;
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }
    }


    public ResultSet query(String query)
    {
        try(Statement statement = this.connection.createStatement())
        {
            return statement.executeQuery(query);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }

    }

    public int update(String update)
    {
        try(Statement statement = this.connection.createStatement())
        {
            return statement.executeUpdate(update);
        }
        catch (SQLException e)
        {
            throw new IllegalStateException(e);
        }

    }


}
