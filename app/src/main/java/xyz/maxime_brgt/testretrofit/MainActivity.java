package xyz.maxime_brgt.testretrofit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    public static String ourImageURL = "";

    private String uploadUserName = HomeActivity.enteredUserName;

    public static String formattedDate = "";

    private String imagePath = "";

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

        uploadAsTextView.setText("Adding images as... " + uploadUserName + " to a bridge with ID of " + BridgeSelectActivity.bridgeID);

    }

    public void onChoose(View v) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

//        File sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//        File f = new File(sdDir, "DJI");
//        Log.d("fileTesting", f.getPath());
//        Intent i = new Intent();
//        i.setAction(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.withAppendedPath(Uri.fromFile(f), "DJI"), "image/*");
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(i, "Select Picture"), PICK_IMAGE_REQUEST);
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
        imagePath = filePath;
        if (filePath == null || filePath.isEmpty()) return;
        chosenFile = new File(filePath);
    }

    public void addFilePath(View v) {
        String bridgeID = BridgeSelectActivity.bridgeID;
        String bridgeName = name.getText().toString();
        String bridgeDescription = description.getText().toString();
        String bridgeUserID = HomeActivity.enteredUserName;
        File sdCard = Environment.getExternalStorageDirectory();
        File f = new File(sdCard +  "/" + "wvDotDroneFolder" + "/" + "filePaths" + ".txt");
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
        String pathToWrite = imagePath;
        try {
            fos.write(pathToWrite + ", " + bridgeID + ", " + bridgeName + ", " + bridgeDescription + ", " + bridgeUserID + "\n");
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
        //return;

        imageView.setImageResource(android.R.color.transparent);
        name.setText("");
        description.setText("");
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