package de.clearit.test.common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import de.clearit.test.framework.PropertyManager;

/**
 * DBUtils
 * 
 * Methoden um mit Datenbank zu arbeiten.
 * 
 * @author Ilja Winokurow
 */
public class DBUtils
{
   // JDBC database URL
   private static String db_url;

   // Database credentials
   private static String db_user;
   private static String db_pass;

   /* Logger */
   static Logger logger = Logger.getLogger("DBUtils");

   static
   {
      PropertyManager propertyManager = PropertyManager.getInstance();
      db_url = propertyManager.getProperty("db.url", "");
      db_user = propertyManager.getProperty("db.user", "");
      db_pass = propertyManager.getProperty("db.pass", "");
   }

   /**
    * SQL Update ausführen.
    * 
    * @param sqlQuery
    *           - die SQL Befehl.
    * @param values
    *           - die Argumenten
    * 
    * @throws SQLException
    *            - die Ausnahme, die wird währen SQL Ausführung geworden.
    */
   public static void executeSQLUpdate(final String sqlQuery, Object... values) throws SQLException
   {
      try (Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);)
      {

         for (int i = 1; i <= values.length; i++)
         {
            pstmt.setObject(i, values[i - 1]);
         }
         pstmt.executeUpdate();
      }

   }

   /**
    * Get results.
    * 
    * Results aus DB auslesen.
    * 
    * @throws SQLException
    * 
    */
   public static List<String> getValuesFromDB(final String sqlQuery, Object... values) throws SQLException
   {
      List<String> results = new ArrayList<>();

      try (Connection conn = DriverManager.getConnection(db_url, db_user, db_pass);
            PreparedStatement pstmt = conn.prepareStatement(sqlQuery);)
      {
         for (int i = 1; i <= values.length; i++)
         {
            pstmt.setObject(i, values[i - 1]);
         }
         try (ResultSet resultSet = pstmt.executeQuery();)
         {
            if (resultSet != null)
            {
               ResultSetMetaData rsmd = resultSet.getMetaData();
               int columnsNumber = rsmd.getColumnCount();
               while (resultSet.next())
               {
                  String temp = "";
                  for (int i = 1; i <= columnsNumber; i++)
                  {
                     temp += resultSet.getString(i) + ";";
                  }
                  results.add(temp);
               }
            }
         }
      }
      return results;
   }
}
