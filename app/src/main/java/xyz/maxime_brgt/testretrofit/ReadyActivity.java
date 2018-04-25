package xyz.maxime_brgt.testretrofit;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class ReadyActivity extends Activity {

    AsyncTaskLoadFiles myAsyncTaskLoadFiles;

    public static ArrayList<String> ourLines;

    public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

        File targetDirector;
        ImageAdapter myTaskAdapter;

        public AsyncTaskLoadFiles(ImageAdapter adapter) {
            myTaskAdapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            String ExternalStorageDirectoryPath = Environment
                    .getExternalStorageDirectory().getAbsolutePath();

            String targetPath = ExternalStorageDirectoryPath + "/DJI/dji.go.v4/CACHE_IMAGE/";
            //String targetPath = ExternalStorageDirectoryPath + "/document/primary:Pictures/";
            Log.d("Apples", targetPath);
            targetDirector = new File(targetPath);
            myTaskAdapter.clear();

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String[] lineArray;
            ArrayList<String> filePaths = new ArrayList<String>();
            final ArrayList<String> bridgeNames = new ArrayList<String>();
            final ArrayList<String> bridgeDescrtiptions = new ArrayList<String>();
            //final ArrayList<String> bridgeUserIDs = new ArrayList<String>();
            final ArrayList<File> ourFiles = new ArrayList<File>();

            File ourFile = null;
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "wvDotDroneFolder/filePaths.txt");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    lineArray = line.split(",");
                    filePaths.add(lineArray[0]);
                    //bridgeIDs.add(lineArray[1]);
                    bridgeNames.add(lineArray[1]);
                    bridgeDescrtiptions.add(lineArray[2]);
                    //bridgeUserIDs.add(lineArray[4]);
                    //Log.d("lineTesting", lineArray[4]);
                    ourFile = new File(lineArray[0]);
                    ourFiles.add(ourFile);
                    Log.d("testing123", "please work..." + filePaths.get(0));
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            File[] files = ourFiles.toArray(new File[ourFiles.size()]);
            for (int i = 0; i < files.length; i++)
                Log.d("Oranges", files[i].toString());

            Arrays.sort(files);
            for (File file : files) {
                publishProgress(file.getAbsolutePath());
                if (isCancelled()) break;
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            myTaskAdapter.add(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            myTaskAdapter.notifyDataSetChanged();
            super.onPostExecute(result);
        }

    }

    public class ImageAdapter extends BaseAdapter {

        private Context mContext;
        ArrayList<String> itemList = new ArrayList<String>();

        public ImageAdapter(Context c) {
            mContext = c;
        }

        void add(String path) {
            itemList.add(path);
        }

        void clear() {
            itemList.clear();
        }

        void remove(int index) {
            itemList.remove(index);
        }

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        //getView load bitmap ui thread

//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) { // if it's not recycled, initialize some
//                // attributes
//                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(220, 220));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
//            } else {
//                imageView = (ImageView) convertView;
//            }
//
//            Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220,
//                    220);
//
//            imageView.setImageBitmap(bm);
//            return imageView;
//        }
//    }

        //getView load bitmap in AsyncTask
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder;

            ImageView imageView;
            if (convertView == null) { // if it's not recycled, initialize some
                // attributes
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(320, 160));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);

                convertView = imageView;

                holder = new ViewHolder();
                holder.image = imageView;
                holder.position = position;
                convertView.setTag(holder);
            } else {
                //imageView = (ImageView) convertView;
                holder = (ViewHolder) convertView.getTag();
                ((ImageView) convertView).setImageBitmap(null);
            }

            //Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
            // Using an AsyncTask to load the slow images in a background thread
            new AsyncTask<ViewHolder, Void, Bitmap>() {
                private ViewHolder v;

                @Override
                protected Bitmap doInBackground(ViewHolder... params) {
                    v = params[0];
                    Bitmap bm = decodeSampledBitmapFromUri(itemList.get(position), 220, 220);
                    return bm;
                }

                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    //Not work for me!
           /*
           if (v.position == position) {
               // If this item hasn't been recycled already,
            // show the image
               v.image.setImageBitmap(result);
           }
           */

                    v.image.setImageBitmap(result);

                }
            }.execute(holder);

            //imageView.setImageBitmap(bm);
            //return imageView;
            return convertView;
        }

        public Bitmap decodeSampledBitmapFromUri(String path, int reqWidth,
                                                 int reqHeight) {

            Bitmap bm = null;
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth,
                    reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            bm = BitmapFactory.decodeFile(path, options);

            return bm;
        }

        public int calculateInSampleSize(

                BitmapFactory.Options options, int reqWidth, int reqHeight) {
            // Raw height and width of image
            final int height = options.outHeight;
            final int width = options.outWidth;
            int inSampleSize = 1;

            if (height > reqHeight || width > reqWidth) {
                if (width > height) {
                    inSampleSize = Math.round((float) height
                            / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) width / (float) reqWidth);
                }
            }

            return inSampleSize;
        }

        class ViewHolder {
            ImageView image;
            int position;
        }

    }

    static ImageAdapter myImageAdapter;

    Button backButton;
    Button viewRecentButton;

    public void viewRecentButtonMethod(View v) {
        Intent i = new Intent(getApplicationContext(), GridActivity.class);
        startActivity(i);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);

        final GridView gridview = (GridView) findViewById(R.id.gridview);
        myImageAdapter = new ImageAdapter(getApplicationContext());
        gridview.setAdapter(myImageAdapter);
        ourLines = new ArrayList<String>();
        backButton = (Button) findViewById(R.id.backButton);
        /*
         * Move to asyncTaskLoadFiles String ExternalStorageDirectoryPath =
		 * Environment .getExternalStorageDirectory() .getAbsolutePath();
		 *
		 * String targetPath = ExternalStorageDirectoryPath + "/test/";
		 *
		 * Toast.makeText(getApplicationContext(), targetPath,
		 * Toast.LENGTH_LONG).show(); File targetDirector = new
		 * File(targetPath);
		 *
		 * File[] files = targetDirector.listFiles(); for (File file : files){
		 * myImageAdapter.add(file.getAbsolutePath()); }
		 */
        myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
        myAsyncTaskLoadFiles.execute();

        gridview.setOnItemClickListener(myOnItemClickListener);

        // Button buttonReload = (Button)findViewById(R.id.reload);
//        buttonReload.setOnClickListener(new OnClickListener(){
//
//            @Override
//            public void onClick(View arg0) {
//
//                //Cancel the previous running task, if exist.
//                myAsyncTaskLoadFiles.cancel(true);
//
//                //new another ImageAdapter, to prevent the adapter have
//                //mixed files
//                myImageAdapter = new ImageAdapter(ReadyActivity.this);
//                gridview.setAdapter(myImageAdapter);
//                myAsyncTaskLoadFiles = new AsyncTaskLoadFiles(myImageAdapter);
//                myAsyncTaskLoadFiles.execute();
//            }});

    }

    OnItemClickListener myOnItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            ImageUpdateActivity.fileLocation = myImageAdapter.itemList.get(position);
            //Log.d("testing1234", myImageAdapter.itemList.get(position));
            ImageUpdateActivity.position = position;

            String[] lineArray;
            ArrayList<String> filePaths = new ArrayList<String>();
            final ArrayList<String> bridgeNames = new ArrayList<String>();
            final ArrayList<String> bridgeDescrtiptions = new ArrayList<String>();
            ImageUpdateActivity.displayTitle = "";
            ImageUpdateActivity.displayDescription = "";

            File ourFile = null;
            int lineNumber = 1;
            try {
                File sdcard = Environment.getExternalStorageDirectory();
                File file = new File(sdcard, "wvDotDroneFolder/filePaths.txt");

                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;
                while ((line = br.readLine()) != null) {
                    lineArray = line.split(",");
                    filePaths.add(lineArray[0]);
                    bridgeNames.add(lineArray[1]);
                    bridgeDescrtiptions.add(lineArray[2]);
                    //bridgeUserIDs.add(lineArray[
                    if (ImageUpdateActivity.fileLocation.equals(lineArray[0])) {
                        ImageUpdateActivity.displayTitle = lineArray[1];
                        ImageUpdateActivity.displayDescription = lineArray[2];
                        ImageUpdateActivity.lineNumber = lineNumber;
                        ourLines.add(line);
                    }
                    ourLines.add(line);
                    lineNumber++;
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Intent i = new Intent(getApplicationContext(), ImageUpdateActivity.class);
            startActivity(i);


//            ImageUpdateActivity.displayTitle = "";
//            ImageUpdateActivity.displayDescription = "";


        }
    };

    public void backButtonMethod(View v) {
        Intent back = new Intent(getApplicationContext(), GridActivity.class);
        startActivity(back);

    }

    public void finishButtonMethod(View v) {
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

        if (count != 0) {
            Intent back = new Intent(getApplicationContext(), BridgeSelectActivity.class);
            startActivity(back);
        } else {
            Toast.makeText(ReadyActivity.this, "You ready queue is empty", Toast.LENGTH_SHORT).show();
        }
    }

    public void clearButtonMethod(View v) {
        File sdCard = Environment.getExternalStorageDirectory();
        File f = new File(sdCard + "/" + "wvDotDroneFolder" + "/" + "filePaths" + ".txt");
        if (!f.exists()) {
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
        try {
            fos.write("");
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

        finish();
        startActivity(getIntent());
    }

}