package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BridgeSelectActivity extends AppCompatActivity {

    Button backButton;
    Button nextButton;
    //EditText bridgeIDEditText;
    EditText userIDEditText;

    public String userID = "";
    public String bridgeID = "";

    String formattedDate = "";

    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_select);

        backButton = (Button) findViewById(R.id.backButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        //bridgeIDEditText = (EditText)findViewById(R.id.bridgeIDEditText);
        userIDEditText = (EditText) findViewById(R.id.userIDEditText);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c);
    }

    public void nextButtonMethod(View v) throws SQLException {

        //File ourFile = null;
        int count = 0;
        try {
            File sdcard = Environment.getExternalStorageDirectory();
            File file = new File(sdcard, "wvDotDroneFolder/filePaths.txt");
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                count++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d("penis", Integer.toString(count));


        userID = userIDEditText.getText().toString();

        if (userID == null || userID.equals("") /* || !rowValues.contains(Integer.parseInt(userID) )*/) {
            Toast.makeText(BridgeSelectActivity.this, "Please enter a user ID", Toast.LENGTH_SHORT).show();
        } else {
            String[] lineArray;

            ArrayList<String> filePaths = dbHandler.loadPathsHandler();
            final ArrayList<String> bridgeNames = dbHandler.loadNamesHandler();
            final ArrayList<String> bridgeDescrtiptions = dbHandler.loadDescriptionsHandler();
            final ArrayList<File> files = new ArrayList<>();

            for(String path : filePaths)
                files.add(new File(path));

//            File ourFile = null;
//            try {
//                File sdcard = Environment.getExternalStorageDirectory();
//                File file = new File(sdcard, "wvDotDroneFolder/filePaths.txt");
//
//                BufferedReader br = new BufferedReader(new FileReader(file));
//                String line;
//                while ((line = br.readLine()) != null) {
//                    lineArray = line.split(",");
//                    filePaths.add(lineArray[0]);
//                    //bridgeIDs.add(lineArray[1]);
//                    bridgeNames.add(lineArray[1]);
//                    bridgeDescrtiptions.add(lineArray[2]);
//                    ourFile = new File(lineArray[0]);
//                    files.add(ourFile);
//                }
//                br.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

//            if (chosenFile == null) {
//                Toast.makeText(MainActivity.this, "Choose a file before upload.", Toast.LENGTH_SHORT)
//                        .show();
//                return;
//            }

            
            for (String item : filePaths) {
                final NotificationHelper notificationHelper = new NotificationHelper(this.getApplicationContext());
                //notificationHelper.createUploadingNotification();
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
                            //notificationHelper.createFailedUploadNotification();
                            return;
                        }
                        if (response.isSuccessful()) {
                            Log.d("URL Picture", "http://imgur.com/" + response.body().data.id);
                            //notificationHelper.createUploadedNotification(response.body());
                            String droneImageURL = "http://imgur.com/" + response.body().data.id;
                            int userIDInt = Integer.parseInt(userID);
                            ConnectionClass.insertPhotoQuery(userIDInt, formattedDate, bridgeNames.get(spot), bridgeDescrtiptions.get(spot), droneImageURL + ".jpg");
                            //Log.d("test123", droneImageURL);
                            Toast.makeText(BridgeSelectActivity.this, "Upload successful!", Toast.LENGTH_LONG).show();

                        }
                    }

                    @Override
                    public void onFailure(Call<ImageResponse> call, Throwable t) {
                        Toast.makeText(BridgeSelectActivity.this, "An unknown error has occured.", Toast.LENGTH_SHORT)
                                .show();
                        notificationHelper.createFailedUploadNotification();
                        t.printStackTrace();
                    }
                });
            }

            dbHandler.clearHandler();

            Intent i = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(i);

        }

//            Intent nextIntent = new Intent(getApplicationContext(), GridActivity.class);
//            startActivity(nextIntent);
    }


    public void backButtonMethod(View v){
        Intent backIntent = new Intent(getApplicationContext(), ReadyActivity.class);
        startActivity(backIntent);
    }
}
