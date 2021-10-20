package utils;
import java.sql.Connection;
import java.sql.SQLException;

import org.sqlite.SQLiteDataSource;

public class DBConnector {
    Connection conn = null;
    SQLiteDataSource ds = null;
    public static Connection conDB()
    {
    	try {
    		SQLiteDataSource ds = new SQLiteDataSource();
            ds.setUrl("jdbc:sqlite:src/application/RosterData.db");
        	try {
        		Connection conn = ds.getConnection();
        		return conn;
              } catch ( SQLException e ) {
                  e.printStackTrace();
                  return null;
              }
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }            
    }
}