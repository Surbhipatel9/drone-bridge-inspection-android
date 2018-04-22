package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.StrictMode;
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
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class HomeActivity extends AppCompatActivity {


    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginButton = (Button) findViewById(R.id.loginButton);
    }

    public void loginButton(View v) {
        Intent goToImageIntent = new Intent(getApplicationContext(), GridActivity.class);
        startActivity(goToImageIntent);

       // ConnectionClass.insertPhotoQuery();

    }

}