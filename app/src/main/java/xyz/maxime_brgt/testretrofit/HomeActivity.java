package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    public TextView uploading;
    private Button loginButton;
    public static boolean uploadBoolean = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        loginButton = (Button) findViewById(R.id.loginButton);
        uploading = (TextView)findViewById(R.id.uploading);
    }

    public void loginButton(View v) {
        Intent goToImageIntent = new Intent(getApplicationContext(), GridActivity.class);
        startActivity(goToImageIntent);
    }


}