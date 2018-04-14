package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class NavigateActivity extends AppCompatActivity {

    private Button toUploadButton;
    private Button toLoginButton;
    private Button toViewOldButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        toUploadButton = (Button)findViewById(R.id.toUploadButton);
        toLoginButton = (Button)findViewById(R.id.toLoginButton);
        toViewOldButton = (Button)findViewById(R.id.toViewOldButton);

    }

    public void toUploadButton(View v){
        Intent goToUploadIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goToUploadIntent);

    }

    public void setToLoginButton(View v){
        Intent goToLoginIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(goToLoginIntent);

    }

    /*
    public void setToViewOldButton(View v){
        Intent goTo = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(goToLoginIntent);

    }
    */

}
