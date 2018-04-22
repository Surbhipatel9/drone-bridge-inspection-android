package xyz.maxime_brgt.testretrofit;

import android.content.Intent;
import android.os.Environment;
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
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BridgeSelectActivity extends AppCompatActivity {

    Button backButton;
    Button nextButton;
    EditText bridgeIDEditText;
    EditText userIDEditText;

    public String userID = "";
    public String bridgeID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_select);

        backButton = (Button)findViewById(R.id.backButton);
        nextButton = (Button)findViewById(R.id.nextButton);
        bridgeIDEditText = (EditText)findViewById(R.id.bridgeIDEditText);
        userIDEditText = (EditText)findViewById(R.id.userIDEditText);
    }

    public void nextButtonMethod(View v){
        bridgeID = bridgeIDEditText.getText().toString();
        userID = userIDEditText.getText().toString();

        if(bridgeID == null || bridgeID.equals("")) {
            Toast.makeText(BridgeSelectActivity.this, "Please enter a bridge ID", Toast.LENGTH_SHORT).show();
            Log.d("ok", bridgeIDEditText.toString());
        }
        if(userID == null || userID.equals("")){
            Toast.makeText(BridgeSelectActivity.this, "Please enter a user ID", Toast.LENGTH_SHORT).show();
        }
        else{
//            Intent nextIntent = new Intent(getApplicationContext(), MainActivity.class);
//            startActivity(nextIntent);
            String[] lineArray;

            ArrayList<String> filePaths = new ArrayList<String>();
            final ArrayList<String> bridgeNames = new ArrayList<String>();
            final ArrayList<String> bridgeDescrtiptions = new ArrayList<String>();
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
                    //bridgeIDs.add(lineArray[1]);
                    bridgeNames.add(lineArray[1]);
                    bridgeDescrtiptions.add(lineArray[2]);
                    ourFile = new File(lineArray[0]);
                    files.add(ourFile);
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
                            Toast.makeText(BridgeSelectActivity.this, "Upload successful!", Toast.LENGTH_SHORT)
                                    .show();
                            Log.d("URL Picture", "http://imgur.com/" + response.body().data.id);
                            //notificationHelper.createUploadedNotification(response.body());
                            String droneImageURL = "http://imgur.com/" + response.body().data.id;
                            Log.d("test123", droneImageURL);

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
