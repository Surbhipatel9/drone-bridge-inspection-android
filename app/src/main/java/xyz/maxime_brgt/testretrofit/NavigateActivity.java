package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigateActivity extends AppCompatActivity {

    private Button toUploadButton;
    private Button toLoginButton;
    private Button toViewOldButton;

    public static String formattedDate = "";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigate);

        toUploadButton = (Button)findViewById(R.id.toUploadButton);
        toLoginButton = (Button)findViewById(R.id.toLoginButton);
        toViewOldButton = (Button)findViewById(R.id.toViewOldButton);

        Date c = Calendar.getInstance().getTime();
        //System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);

        Log.d("ourDate", formattedDate.toString());

    }

    public void toUploadButton(View v){
        Intent goToUploadIntent = new Intent(getApplicationContext(), BridgeSelectActivity.class);
        startActivity(goToUploadIntent);

    }

    public void readyButton(View v){
        Intent ready = new Intent(getApplicationContext(), ReadyActivity.class);
        startActivity(ready);
    }

    public void setToLoginButton(View v){
        Intent goToLoginIntent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(goToLoginIntent);

    }

    public void setToViewOldButton(View v){

            String[] lineArray;

            ArrayList<String> filePaths = new ArrayList<String>();
            final ArrayList<String> bridgeIDs = new ArrayList<String>();
            final ArrayList<String> bridgeNames = new ArrayList<String>();
            final ArrayList<String> bridgeDescrtiptions = new ArrayList<String>();
            final ArrayList<String> bridgeUserIDs = new ArrayList<String>();
            final ArrayList<File> files = new ArrayList<File>();

            File ourFile = null;
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard,"wvDotDroneFolder/filePaths.txt");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    lineArray = line.split(",");
                    filePaths.add(lineArray[0]);
                    bridgeIDs.add(lineArray[1]);
                    bridgeNames.add(lineArray[2]);
                    bridgeDescrtiptions.add(lineArray[3]);
                    bridgeUserIDs.add(lineArray[4]);
                    Log.d("lineTesting", lineArray[0]);
                    Log.d("lineTesting", lineArray[1]);
                    Log.d("lineTesting", lineArray[2]);
                    Log.d("lineTesting", lineArray[3]);
                    Log.d("lineTesting", lineArray[4]);
                    ourFile = new File(lineArray[0]);
                    files.add(ourFile);
                    Log.d("testing123", "please work..." + filePaths.get(0));
                }
                br.close() ;
            }catch (IOException e) {
                e.printStackTrace();
            }

//            if (chosenFile == null) {
//                Toast.makeText(MainActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }

            for(String item : filePaths) {

                final NotificationHelper notificationHelper = new NotificationHelper(this.getApplicationContext());
                notificationHelper.createUploadingNotification();
                ImgurService imgurService = ImgurService.retrofit.create(ImgurService.class);
                final int spot = filePaths.indexOf(item);
        Log.d("testing123", filePaths.get(spot));
                final Call<ImageResponse> call =
                        imgurService.postImage(
                                bridgeNames.get(spot),
                                bridgeDescrtiptions.get(spot), "", "",
                                MultipartBody.Part.createFormData(
                                        "image",
                                        filePaths.get(spot),
                                        RequestBody.create(MediaType.parse("image/*"), /*filePaths.get(spot)*/ files.get(spot))
                                ));
                call.enqueue(new Callback<ImageResponse>() {

                    @Override
                    public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                        if (response == null) {
                            notificationHelper.createFailedUploadNotification();
                            return;
                        }
                        if (response.isSuccessful()) {
                            Toast.makeText(NavigateActivity.this, "Upload successful !", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("URL Picture", "http://imgur.com/" + response.body().data.id);
                            notificationHelper.createUploadedNotification(response.body());
                            String droneImageURL = "http://imgur.com/" + response.body().data.id;
                            Log.d("test123", droneImageURL);
                            File sdCard = Environment.getExternalStorageDirectory();
                            File f = new File(sdCard + "/" + "wvDotDroneFolder" + "/" + bridgeIDs.get(spot)  /*"bridgeInfo" */+ ".txt");
                            if (!f.exists()) {
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
                            Log.d("Test Again", MainActivity.ourImageURL);
                            String query = "INSERT INTO photos (userID, reportID, date, title, description, location) VALUES ("
                                    + bridgeUserIDs.get(spot) + ", "
                                    + bridgeIDs.get(spot)+ ", "
                                    + "'" + ImageEditActivity.formattedDate + "'" + ", "
                                    + "'" + bridgeNames.get(spot) + "'" + ", "
                                    + "'" + bridgeDescrtiptions.get(spot) + "'" + ", "
                                    + "'" + droneImageURL + ".jpg" + "'" + ");";
                            try {
                                fos.write(query + "\n");
                                Log.d("done", "done");
                                fos.close();
                                fw.close();
                            } catch (IOException e) {
                                Log.d("ok", e.toString());
                            } finally {
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
                        Toast.makeText(NavigateActivity.this, "An unknown error has occured.", Toast.LENGTH_SHORT)
                                .show();
                        notificationHelper.createFailedUploadNotification();
                        t.printStackTrace();
                    }
                });
            }
        Toast.makeText(NavigateActivity.this, "FINISHED UPLOADING", Toast.LENGTH_LONG).show();
       }

    }
