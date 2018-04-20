package xyz.maxime_brgt.testretrofit;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
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
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
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

    //public String  toWrite = "its me adam";
    public String filename = "test";
    public static String ourImageURL = "";

    private String uploadUserName = HomeActivity.enteredUserName;

    String formattedDate = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView)findViewById(R.id.imageView);
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.description);
        uploadAsTextView = (TextView) findViewById(R.id.uploadAsTextView);

        uploadButton = (Button) findViewById(R.id.uploadButton);

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        Log.d("ourDate", formattedDate.toString());

/*
        File sdCard = Environment.getExternalStorageDirectory();
        Log.d("location", sdCard.getAbsolutePath());
        Log.e("help", String.valueOf(sdCard.exists()));
        File f = new File(sdCard + "/test.txt");
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileWriter fw = null;
        try {
            fw = new FileWriter(f, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter fos = new BufferedWriter(fw);
        String descriptionInsert = description.getText().toString();
        String nameInsert = name.getText().toString();
        try{
            fos.write("");
            Log.d("done", "done");
            fos.close();
            fw.close();
        } catch(FileNotFoundException e){
            Log.d("ok", e.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        */

        uploadAsTextView.setText("Uploading as... " + uploadUserName);

    }

/*
    public void fileUpload(View v) throws IOException {
        File sdCard = Environment.getExternalStorageDirectory();
        Log.d("location", sdCard.getAbsolutePath());
        //File dir = new File("/storage/self/primary");
        Log.e("help", String.valueOf(sdCard.exists()));
        File f = new File(sdCard + "/test.txt");
        if(!f.exists()){
            f.createNewFile();
        }
        FileWriter fw = new FileWriter(f, true);
        BufferedWriter fos = new BufferedWriter(fw);
        String descriptionInsert = description.getText().toString();
        String nameInsert = name.getText().toString();
        try{
            fos.write(toWrite + "\n");
            Log.d("done", "done");
            fos.close();
            fw.close();
        } catch(FileNotFoundException e){
            Log.d("ok", e.toString());
        }finally {
            fw.close();
        }
    }

    */

    public void onChoose(View v) {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    public void onUpload(View v) throws IOException{

        if (chosenFile == null) {
            Toast.makeText(MainActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
                    .show();
            return;
        }

        final NotificationHelper notificationHelper = new NotificationHelper(this.getApplicationContext());
        notificationHelper.createUploadingNotification();

        ImgurService imgurService = ImgurService.retrofit.create(ImgurService.class);

        final String postName = name.getText().toString();
        final String postDescription = description.getText().toString();
        final Call<ImageResponse> call =

                imgurService.postImage(
                        name.getText().toString(),
                        description.getText().toString(), "", "",
                        MultipartBody.Part.createFormData(
                                "image",
                                chosenFile.getName(),
                                RequestBody.create(MediaType.parse("image/*"), chosenFile)
                        ));

        call.enqueue(new Callback<ImageResponse>(){
            String imageUrl = "";
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
                    Log.d("Test URL", ourImageURL);
                    File sdCard = Environment.getExternalStorageDirectory();
                    //File f = new File(sdCard + "/test.txt");
                    File f = new File(sdCard + "/" + BridgeSelectActivity.bridgeID + ".txt");
                    if(!f.exists()){
                        try {
                            f.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    FileWriter fw = null;
                    try {
                        fw = new FileWriter(f, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BufferedWriter fos = new BufferedWriter(fw);
                    String photoTitle = name.getText().toString();
                    String photoDescription = description.getText().toString();
                    String id = HomeActivity.enteredUserName;
                    String bridgeID = BridgeSelectActivity.bridgeID;
                    Log.d("Test Again", ourImageURL);
                    String query = "INSERT INTO photos (userID, reportID, date, title, description, location) VALUES ("
                            + id + ", "
                            + bridgeID + ", "
                            + "'" + formattedDate +"'" + ", "
                            + "'" + postName +"'" + ", "
                            + "'" + postDescription + "'" + ", "
                            + "'" + ourImageURL + ".jpg" + "'" + ");";
                    try{
                        fos.write(query + "\n");
                        Log.d("done", "done");
                        fos.close();
                        fw.close();
                    } catch(IOException e){
                        Log.d("ok", e.toString());
                    }
                     finally {
                            try {
                                fw.close();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    return;
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
        Intent goToLoginIntent = new Intent(getApplicationContext(), NavigateActivity.class);
        startActivity(goToLoginIntent);
    }
}