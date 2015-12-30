package com.bignerdranch.android.doppl;

import com.bignerdranch.android.doppl.util.SystemUiHider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.ExifInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;


public class MainActivity extends Activity {

    // Potential intents request codes
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;

    //Permission codes constants
    final private int REQUEST_CODE_CAMERA = 123;
    final private int REQUEST_CODE_READ_STORAGE = 321;

    public static final String threshold = "0.0";
    public static Double percent = 0.0;
    public static int flag = 0;
    public static int successCounter = 0;
    public static boolean openFlag = false;
    ImageButton image1;
    ImageButton image2;
    Button compare_button;
    TextView similarity_percent;
    TextView please_wait;
    TextView img1data;
    TextView img2data;
    final Context context = this;
    public JSONArray images1, images2;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        image1 = (ImageButton) findViewById(R.id.image1);
        image2 = (ImageButton) findViewById(R.id.image2);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                flag = 1;
                selectImage();
            }
        });
        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                flag = 2;
                selectImage();
            }
        });

        compare_button = (Button) findViewById(R.id.compare_button);
        similarity_percent = (TextView) findViewById(R.id.similarity_percent);
        img1data = (TextView) findViewById(R.id.img1data);
        img2data = (TextView) findViewById(R.id.img2data);
        please_wait = (TextView) findViewById(R.id.please_wait);
        please_wait.setVisibility(View.INVISIBLE);
        img1data.setVisibility(View.INVISIBLE);
        img2data.setVisibility(View.INVISIBLE);
        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                    compareImage();
                    //please_wait.setText("Socket Timeout: Please Try Again");
//                try {
//                    JSONObject image2 = images1.getJSONObject(1);
//                    JSONObject subimage2 = image2.getJSONObject(GENDER);
//                    String gender2 = subimage2.getString(TYPE);
//                    String gPercent2 = subimage2.getString(CONFIDENCE);
//                    if (gender2 == "M"){
//                        gender2 = "Male ";
//                    } else {
//                        gender2 = "Female ";
//                    }
//                    img2data.setText(gender2 + gPercent2);
//

//
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        if (openFlag == false) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

            // set title
            alertDialogBuilder.setTitle("Welcome to Doppl!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Compare your friends selfies! Uses the Kairos API! Please note photos can take up to 15 seconds to process.")
                    .setCancelable(false)
                    .setPositiveButton("Get to it!", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

            openFlag = true;
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bignerdranch.android.doppl/http/host/path")
        );
        AppIndex.AppIndexApi.start(mClient, viewAction);

    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.bignerdranch.android.doppl/http/host/path")
        );
        AppIndex.AppIndexApi.end(mClient, viewAction);
        mClient.disconnect();
    }


    public void selectImage() {

        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    startCameraActivity();
                } else if (items[item].equals("Choose from Library")) {
                    startPhotoLibraryActivity();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    // NICK, LEAVE THIS HERE!

//    private void startCameraActivityDummy() {
//        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
//        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[] {Manifest.permission.CAMERA},
//                    REQUEST_CODE_CAMERA);
//            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_CODE_READ_STORAGE);
//            return;
//        }
//        startCameraActivity();
//    }
//
//    private void startPhotoLibraryActivityDummy() {
//        int hasReadStoragePermission = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
//        if (hasReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},
//                    REQUEST_CODE_READ_STORAGE);
//            return;
//        }
//        startPhotoLibraryActivity();
//    }

    public void startCameraActivity() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    public void startPhotoLibraryActivity() {
        Intent libraryIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        libraryIntent.setType("image/*");
        startActivityForResult(
                Intent.createChooser(libraryIntent, "Select File"), SELECT_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (flag == 1) {
                    image1.setImageBitmap(Bitmap.createScaledBitmap(thumbnail, 350, 350, false));
                } else {
                    image2.setImageBitmap(Bitmap.createScaledBitmap(thumbnail, 350, 350, false));
                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

                ExifInterface exif = null;
                try {
                    exif = new ExifInterface(selectedImagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                Bitmap bm;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(selectedImagePath, options);
                final int REQUIRED_SIZE = 200;
                int scale = 1;
                while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                        && options.outHeight / scale / 2 >= REQUIRED_SIZE)
                    scale *= 2;
                options.inSampleSize = scale;
                options.inJustDecodeBounds = false;
                bm = BitmapFactory.decodeFile(selectedImagePath, options);
                Bitmap bmRotated = rotateBitmap(bm, orientation);

                if (flag == 1) {
                    image1.setImageBitmap(Bitmap.createScaledBitmap(bmRotated, 350, 350, false));
                } else {
                    image2.setImageBitmap(Bitmap.createScaledBitmap(bmRotated, 350, 350, false));
                }
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void compareImage(){
        boolean network = isNetworkAvailable();

        if (network == false) {
            //get the LayoutInflater and inflate the custom_toast layout
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.custom_toast, (ViewGroup)
                    findViewById(R.id.toast_layout_root));

            //get the TextView from the custom_toast layout
            TextView text = (TextView) layout.findViewById(R.id.toastText);
            text.setText("This action requires internet connectivity");

            //create the toast object, set display duration,
            //set the view as layout that's inflated above and then call show()
            Toast t = new Toast(getApplicationContext());
            t.setDuration(Toast.LENGTH_LONG);
            t.setView(layout);
            t.show();
            return;
        }

        Log.d("This = ", this.toString());
        KairosManager myKairosManager = new KairosManager(this);
        myKairosManager.execute(((BitmapDrawable) image1.getDrawable()).getBitmap(), ((BitmapDrawable) image2.getDrawable()).getBitmap());
        please_wait.setVisibility(View.VISIBLE);
    }

    public void setSimilarityPercent(String confidence){
        similarity_percent.setText(confidence);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}