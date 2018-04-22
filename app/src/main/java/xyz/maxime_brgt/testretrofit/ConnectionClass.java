package xyz.maxime_brgt.testretrofit;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Nate on 4/17/2018.
 */

public class ConnectionClass {

    //String ip = "73.251.9.178";

    String connString = "jdbc:jtds:sqlserver://droneinsp.database.windows.net/DroneInspDB;encrypt=false;user=aksenov;password=Datapass123;instance=SQLEXPRESS;";
    String username = "aksenov";
    String password = "Datapass123";

    //String ConnURL = null;
//    @SuppressLint("NewApi")
//    public Connection Conn() {
////        Log.i("Pineapple Android"," MySQL Connect Example.");
////        Connection conn = null;
////        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
////        StrictMode.setThreadPolicy(policy);
////        try {
////            String driver = "net.sourceforge.jtds.jdbc.Driver";
////            Class.forName(driver).newInstance();
////            //test = com.microsoft.sqlserver.jdbc.S
////            conn = DriverManager.getConnection(connString, username, password);
////            Log.w("Pineapple Connection","open");
//        return;
//
//    }

    public static void insertPhotoQuery(String userID, String date, String title, String description, String location)
    {
        Log.i("Pineapple Android"," MySQL Connect Example.");
        Connection conn = null;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            Class.forName(driver).newInstance();
            //test = com.microsoft.sqlserver.jdbc.SQLServerDriver.class;
            String connString = "jdbc:jtds:sqlserver://droneinsp.database.windows.net/DroneInspDB;encrypt=false;user=aksenov;password=Datapass123;instance=SQLEXPRESS;";
            String username = "aksenov";
            String password = "Datapass123";
            conn = DriverManager.getConnection(connString, username, password);
            Log.w("Pineapple Connection","open");
            Statement stmt = conn.createStatement();
            ResultSet reset = stmt.executeQuery("select * from counties");

            String query = "INSERT INTO DroneInspDB.dbo.photos (userID, date, title, description, location)"
                    + " values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, 2);
            preparedStatement.setString(2, "4/20/2018");
            preparedStatement.setString(3, "Nate test");
            preparedStatement.setString(4, "No, it's the chairs that are wrong.");
            preparedStatement.setString(5, "https://i.imgur.com/iysZR63.jpg");

            preparedStatement.execute();
            preparedStatement.close();

            //Log.w("Pineapple Data:", reset.getString(3));

            conn.close();

        } catch (Exception e)
        {
            Log.w("Pineapple Errconnection","" + e.getMessage());
        }
    }

    }


