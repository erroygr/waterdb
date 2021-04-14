package MSSQL;

import object.RateAndCost;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Properties;

public class MSSQLConnection {
    private static Connection connection;
    private static RateAndCost rateAndCost;
    public static RateAndCost getRateAndCost() {
        return rateAndCost;
    }

    public static  Connection getConnection() throws SQLException {
        //String url = "jdbc:sqlserver://localhost:1433;databaseName=bd_water;integratedSecurity=true;";

        ResultSet resultSetRateAndCost = null;

            try (Connection conn = getConnectionDB()) {

                System.out.println("Connection to Store DB succesfull!");

               // connection = DriverManager.getConnection(url);
                Statement statement = conn.createStatement();
                // Create and execute a SELECT SQL statement.
                String selectSql = "SELECT *FROM rateANDcost";
                 resultSetRateAndCost = statement.executeQuery(selectSql);

                // Print results from select statement
                while (resultSetRateAndCost.next()) {
                    System.out.println(resultSetRateAndCost.getString(1) +
                            " " + resultSetRateAndCost.getString(2) +
                            " " + resultSetRateAndCost.getString(3) +
                            " " + resultSetRateAndCost.getString(4) +
                            " " + resultSetRateAndCost.getString(5) +
                            " " + resultSetRateAndCost.getString(6) +
                            " " + resultSetRateAndCost.getString(7) +
                            " " + resultSetRateAndCost.getString(8) +
                            " " + resultSetRateAndCost.getString(9) +
                            " " + resultSetRateAndCost.getString(10) +
                            " " + resultSetRateAndCost.getString(11));
                    rateAndCost = new RateAndCost(resultSetRateAndCost.getFloat(1),
                            resultSetRateAndCost.getFloat(2),
                            resultSetRateAndCost.getFloat(3),
                            resultSetRateAndCost.getFloat(4),
                            resultSetRateAndCost.getFloat(5),
                            resultSetRateAndCost.getFloat(6),
                            resultSetRateAndCost.getFloat(7),
                            resultSetRateAndCost.getFloat(8),
                            resultSetRateAndCost.getFloat(9),
                            resultSetRateAndCost.getFloat(10),
                            resultSetRateAndCost.getFloat(11));
                    connection=conn;
                }
                System.out.println(conn);
            }catch (SQLException | IOException e) {
                e.printStackTrace();
            }

        return connection;
        }




    public static Connection getConnectionDB() throws SQLException, IOException {


        Properties props = new Properties();
        try(InputStream in = Files.newInputStream(Paths.get("database.properties"))){
            props.load(in);
        }
        String url = props.getProperty("url");


        return DriverManager.getConnection(url);
    }
}
