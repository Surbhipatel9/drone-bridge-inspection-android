package xyz.maxime_brgt.testretrofit;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Nate on 4/17/2018.
 */

public class ConnectionClass {

    String ip = "73.251.9.178";
    /*
    String db = "DroneInspDB";
    String un = "aksenov";
    String password = "Datapass123";
    */

    String db = "DroneTest";
    String un = "natemiklas1";
    String password = "ravens67";

    //String ConnURL = null;
    @SuppressLint("NewApi")
    public Connection Conn(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        //String ConnURL =  null;

        try{
            Class.forName("net.sourceforge.jtds.jdbs.Driver");
            String ConnURL = "jdbc:jtds:sqlserver://" + db + ";user=" + un +
                    ";password=" + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        }catch(SQLException se){
            Log.d("sql ", se.toString());}
        catch(ClassNotFoundException e){
        Log.d("sql ", e.toString());} catch(NullPointerException e){
            Log.d("sql", e.toString());
        } catch(Exception e){
            Log.d("sql ", e.toString());
        }

        return conn;
    }

    }


