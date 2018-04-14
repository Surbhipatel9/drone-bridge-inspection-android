package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {


    private Button loginButton;
    private EditText usernameEditText;

    public static String enteredUserName ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginButton = (Button) findViewById(R.id.loginButton);
        usernameEditText = (EditText) findViewById(R.id.usernameEditText);
    }

    public void loginButton(View v){
        enteredUserName = usernameEditText.getText().toString();

        if(enteredUserName == null || enteredUserName.equals("")){
            Toast.makeText(getApplicationContext(), "Please enter a username",
                    Toast.LENGTH_LONG).show();
        }
        else {
            Intent goToNavigateIntent = new Intent(getApplicationContext(), NavigateActivity.class);
            startActivity(goToNavigateIntent);
        }


    }

}
