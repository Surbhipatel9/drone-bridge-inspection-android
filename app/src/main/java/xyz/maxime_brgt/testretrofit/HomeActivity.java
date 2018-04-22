package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeActivity extends AppCompatActivity {


    private Button loginButton;
    private EditText usernameEditText;

    public static String enteredUserName ="";


    private static final String DB_URL = "jdbc:mysql://192.168.56.1/dronemysqldb";
    private static final String USER = "root";
    private static final String PASS = "monopoly71";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    }

    public void loginButton(View v){
//        enteredUserName = usernameEditText.getText().toString();
//
//        if(enteredUserName == null || enteredUserName.equals("")){
//            Toast.makeText(getApplicationContext(), "Please enter a username",
//                    Toast.LENGTH_LONG).show();
//        }
//        else {
//            Intent goToNavigateIntent = new Intent(getApplicationContext(), NavigateActivity.class);
//            startActivity(goToNavigateIntent);
//        }
        String photoID = "1";
        String userID = "1";
        String date = "04/24/2018";
        String title = "hey";
        String descrition = "yepp";
        String location = "google.com";
        String selected = "1";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            if(conn == null){
                //
            }
            else{
                String query = "INSERT INTO photos (photoid, userid, date, title, description, location, selected) VALUES "
                        + photoID + ", "
                        + userID + ", "
                        + "'" + date + "'" + ", "
                        + "'" + title + "'" + ", "
                        + "'" + descrition + "'" + ", "
                        + "'" + location + "'" + ", "
                        + selected + ");";
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
            }
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.d("mysql", e.toString());
        } catch (SQLException e) {
            e.printStackTrace();
            Log.d("mysql", e.toString());
        }
    }

}
