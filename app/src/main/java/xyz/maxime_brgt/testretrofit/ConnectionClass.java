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

    String ip = "206.212.36.194";
    String db = "DroneInspDB";
    String un = "aksenov";
    String password = "Datapass123";

    //String ConnURL = null;
    @SuppressLint("NewApi")
    public Connection Conn(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        //String ConnURL =  null;

        try{
            Class.forName("net.sourceforge.jtds.jdbs.Driver");
            String ConnURL = "jdbc:jtds:sqlserver://" + ip + "/" + db + ";user=" + un +
                    ";password=" + password + ";";
            conn = DriverManager.getConnection(ConnURL);
        }catch(SQLException se){
            Log.d("sql ", se.toString());}
        catch(ClassNotFoundException e){
        Log.e("sql ", e.toString());}
        catch(Exception e){
            Log.e("sql ", e.toString());
        }

        return conn;
    }

    }


