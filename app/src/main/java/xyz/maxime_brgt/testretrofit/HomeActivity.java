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


        Intent goToUploadIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goToUploadIntent);

    }

}
