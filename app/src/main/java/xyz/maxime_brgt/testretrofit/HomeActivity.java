package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.karan.churi.PermissionManager.PermissionManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    public TextView uploading;
    private Button loginButton;
    public static boolean uploadBoolean = false;
    PermissionManager permissionManager;
    public boolean toContinue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d("TestDB", "Here");
        loginButton = (Button) findViewById(R.id.loginButton);
        uploading = (TextView) findViewById(R.id.uploading);

        permissionManager = new PermissionManager() {
        };
        permissionManager.checkAndRequestPermissions(this);

        loginButton.setVisibility(View.VISIBLE);
        loginButton.setBackgroundColor(Color.TRANSPARENT);

    }

    public void loginButton(View v) {
        Intent goToImageIntent = new Intent(getApplicationContext(), GridActivity.class);

        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;


        File yourAppDir = new File(Environment.getExternalStorageDirectory() + File.separator + "DJI/dji.go.v4/CACHE_IMAGE/");
        if (!yourAppDir.exists() && !yourAppDir.isDirectory()) {
            // create empty directory
            if (yourAppDir.mkdirs()) {
                Log.i("CreateDir", "App dir created");
            } else {
                Log.w("CreateDir", "Unable to create app dir!");
            }
        }
        startActivity(goToImageIntent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantedResults) {
        permissionManager.checkResult(requestCode, permissions, grantedResults);

        ArrayList<String> granted = permissionManager.getStatus().get(0).granted;
        for (String line : granted) {
            Log.d("granted", line);
        }
    }

    //asdasdadsasdasdasdas

}