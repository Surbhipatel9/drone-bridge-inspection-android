package xyz.maxime_brgt.testretrofit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static xyz.maxime_brgt.testretrofit.Constants.PICK_IMAGE_REQUEST;
import static xyz.maxime_brgt.testretrofit.Constants.READ_WRITE_EXTERNAL;

public class MainActivity extends AppCompatActivity {

    private File chosenFile;
    private Uri returnUri;
    private ImageView imageView;
    private EditText description;
    private EditText name;
    private TextView uploadAsTextView;

    private Button uploadButton;

    public String fileName = "helloWorld";
    public String body = "hey its me";
    public final static String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/instinctcoder/readwrite/" ;
    //database info
    public String  data = "yepppp";


    private String ourImageURL;

    //ConnectionClass connectionClass = new ConnectionClass();
    private String uploadUserName = HomeActivity.enteredUserName;

    //String url, userName, password, database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        uploadAsTextView = (TextView) findViewById(R.id.uploadAsTextView);

        uploadButton = (Button) findViewById(R.id.uploadButton);

        //connectionClass = new ConnectionClass();

        uploadAsTextView.setText("Uploading as... " + uploadUserName);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWriteable()){
                    write();
                }
            }
        });
    }

    private void write() {
        File sdCard = Environment.getExternalStorageDirectory();
        File f = new File(sdCard, fileName);

        try{
            FileOutputStream fos = new FileOutputStream(f);
            String data = "yepppppppppp";

            fos.write(data.getBytes());
            fos.close();
        }catch(FileNotFoundException e){
            Log.d("ok", e.toString());
        }catch(IOException e){
            Log.d("ok", e.toString());
        }
    }

    private boolean isWriteable(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }else{
            return false;
        }
    }





    public void onChoose(View v) {

    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);
    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }


    public void onUploadTest(View v){

        String user = "aksenov";
        String password = "Datapass123";
        String url = "jdbc:jtds:sqlserver://droneinsp.database.windows.net";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        /*
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO photo (photoID, userID, date, title, description, location, selected) VALUES ("
                    + 100 + ", "
                    + 2 + ", "
                    + "04/24/2018" + ", "
                    + "awesome" + ", "
                    + "yeppp" + ", "
                    + "google.com" + ", "
                    + 1
                    +  ");";
            Statement stmt = con.createStatement();
            stmt.execute(query);
            stmt.close();
            con.close();

        }catch(SQLException e){
            Log.d("sql", e.toString());
        }catch(IllegalStateException e){
            Log.d("sql", e.toString());
        }catch(ClassNotFoundException e){
            Log.d("sql", e.toString());
        }*/


        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, password);
            String query = "INSERT INTO DroneInspDB.dbo.photos (photoID, userID, date, title, description, location, selected)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, 100);
            preparedStatement.setInt(2, 2);
            preparedStatement.setString(3, "4/16/2018");
            preparedStatement.setString(4, "best bridge ever");
            preparedStatement.setString(5, "yep, so good");
            preparedStatement.setString(6, ourImageURL);
            preparedStatement.setInt(7, 1);

            preparedStatement.execute();
            preparedStatement.close();
            con.close();

        }catch(SQLException e){
            Log.d("sql", e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }



    public void onUpload(View v) {

        if (chosenFile == null) {
            Toast.makeText(MainActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        final NotificationHelper notificationHelper = new NotificationHelper(this.getApplicationContext());
        notificationHelper.createUploadingNotification();

        ImgurService imgurService = ImgurService.retrofit.create(ImgurService.class);

        EditText name = (EditText) findViewById(R.id.name);
        EditText description = (EditText) findViewById(R.id.description);


        final Call<ImageResponse> call =
                imgurService.postImage(
                        name.getText().toString(),
                        description.getText().toString(), "", "",
                        MultipartBody.Part.createFormData(
                                "image",
                                chosenFile.getName(),
                                RequestBody.create(MediaType.parse("image/*"), chosenFile)
                        ));

        call.enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                if (response == null) {
                    notificationHelper.createFailedUploadNotification();
                    return;
                }
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Upload successful !", Toast.LENGTH_SHORT)
                            .show();
                    Log.d("URL Picture", "http://imgur.com/" + response.body().data.id);
                    notificationHelper.createUploadedNotification(response.body());
                    ourImageURL = "http://imgur.com/" + response.body().data.id;

                }
            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "An unknown error has occured.", Toast.LENGTH_SHORT)
                        .show();
                notificationHelper.createFailedUploadNotification();
                t.printStackTrace();
            }
        });

        String photoTitle = name.getText().toString();
        String photoDescription = description.getText().toString();
        String photoUrl = ourImageURL;
/*
        try{
            Connection con = connectionClass.Conn();
            String query = "INSERT INTO photo (photoID, userID, date, title, description, location, selected) VALUES ("
                    + 100 + ", "
                    + 2 + ", "
                    + "awesome" + ", "
                    + "yeppp" + ", "
                    + "google.com" + ", "
                    + 1
                    +  ");";
            Statement stmt = con.createStatement();
            stmt.execute(query);
            con.close();


        }catch(SQLException e){
            Log.d("sql", e.toString());
        }catch(IllegalStateException e){
            Log.d("sql", e.toString());
        }
*/

        /*
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            String connectionURL = "jdbc:jtds:sqlserver://" + url +";databaseName="+ database + ";user=" + userName + ";password=" + password + ";";
            Connection con = DriverManager.getConnection(connectionURL);
            String query = "insert into photo (photoID, userID, date, title, description, location, selected)"
                    + " values (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, 100);
            preparedStatement.setInt(2, 2);
            preparedStatement.setString(3, "4/16/2018");
            preparedStatement.setString(4, "best bridge ever");
            preparedStatement.setString(5, "yep, so good");
            preparedStatement.setString(6, ourImageURL);
            preparedStatement.setInt(7, 1);

            preparedStatement.execute();
            con.close();

        }catch(SQLException e){
            Log.d("sql", e.toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
*/
        imageView.setImageResource(android.R.color.transparent);
        name.setText("");
        description.setText("");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            returnUri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), returnUri);

                ImageView imageView = (ImageView) findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

            super.onActivityResult(requestCode, resultCode, data);

            Log.d(this.getLocalClassName(), "Before check");


            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                final List<String> permissionsList = new ArrayList<String>();
                addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
                addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (!permissionsList.isEmpty())
                    ActivityCompat.requestPermissions(MainActivity.this,
                            permissionsList.toArray(new String[permissionsList.size()]),
                            READ_WRITE_EXTERNAL);
                else
                    getFilePath();
            } else {
                getFilePath();
            }
        }
    }

    private void getFilePath() {
        String filePath = DocumentHelper.getPath(this, this.returnUri);
        //Safety check to prevent null pointer exception
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
        Log.d("FilePath", filePath);
    }

    private void addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            shouldShowRequestPermissionRationale(permission);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_WRITE_EXTERNAL:
            {
                Map<String, Integer> perms = new HashMap<String, Integer>();
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                if (perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "All Permission are granted.", Toast.LENGTH_SHORT)
                            .show();
                    getFilePath();
                } else {
                    Toast.makeText(MainActivity.this, "Some permissions are denied", Toast.LENGTH_SHORT)
                            .show();
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void backButton(View v){
        Intent goToLoginIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(goToLoginIntent);
    }
}