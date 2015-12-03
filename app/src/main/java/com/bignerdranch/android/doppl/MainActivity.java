package com.bignerdranch.android.doppl;

import com.bignerdranch.android.doppl.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.ImageButton;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.kairos.*;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;


public class MainActivity extends Activity {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    public static final int IMAGE_ONE = 1;
    public static final int IMAGE_TWO = 2;
    public static final float threshold = 0.0f;
    public static int flag = 0;
    ImageButton image1;
    ImageButton image2;
    Button compare_button;

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

        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                compareImage();
            }
        });

            /* * * * * * * * * * * * * * * * * * * * */
            /* * *  Kairos Method Call Examples * * */
            /* * * * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * * */
            /* * * * * * * * * * * * * * * * **/
            /* * * * * * * * * * * * * * * * */


            //  List galleries
            //myKairos.listGalleries(listener);


            /* * * * * * * * DETECT EXAMPLES * * * * * * *


            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null
            String image = "http://media.kairos.com/liz.jpg";
            myKairos.detect(image, null, null, listener);



            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String selector = "FULL";
            String minHeadScale = "0.25";
            myKairos.detect(image, selector, minHeadScale, listener);

            */



            /* * * * * * * * ENROLL EXAMPLES * * * * * * *

            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null
            String image = "http://media.kairos.com/liz.jpg";
            String subjectId = "Elizabeth";
            String galleryId = "friends";
            myKairos.enroll(image, subjectId, galleryId, null, null, null, listener);


            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String subjectId = "Elizabeth";
            String galleryId = "friends";
            String selector = "FULL";
            String multipleFaces = "false";
            String minHeadScale = "0.25";
            myKairos.enroll(image,
                    subjectId,
                    galleryId,
                    selector,
                    multipleFaces,
                    minHeadScale,
                    listener);

                    */


            /* * * * * * * RECOGNIZE EXAMPLES * * * * * * *

            // Bare-essentials Example:
            // This example uses only an image url, setting optional params to null
            String image = "http://media.kairos.com/liz.jpg";
            String galleryId = "friends";
            myKairos.recognize(image, galleryId, null, null, null, null, listener);


            // Fine-grained Example:
            // This example uses a bitmap image and also optional parameters
            Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.liz);
            String galleryId = "friends";
            String selector = "FULL";
            String threshold = "0.75";
            String minHeadScale = "0.25";
            String maxNumResults = "25";
            myKairos.recognize(image,
                    galleryId,
                    selector,
                    threshold,
                    minHeadScale,
                    maxNumResults,
                    listener);

                    */


            /* * * * GALLERY-MANAGEMENT EXAMPLES * * * *


            //  List galleries
            myKairos.listGalleries(listener);



            //  List subjects in gallery
            myKairos.listSubjectsForGallery("your_gallery_name", listener);



            // Delete subject from gallery
            myKairos.deleteSubject("your_subject_id", "your_gallery_name", listener);



            // Delete an entire gallery
            myKairos.deleteGallery("your_gallery_name", listener);

            */
    }


    public void selectImage() {

        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Library")) {
                    Intent libraryIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    libraryIntent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(libraryIntent, "Select File"), SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    public void compareImage(){
        /* * * instantiate a new kairos instance * * */
        Kairos myKairos = new Kairos();

        /* * * set authentication * * */
        String app_id = "10acb675";
        String api_key = "3dd4660b913c705c2e99fb50ad1cf38b";
        myKairos.setAuthentication(this, app_id, api_key);

        // listener
        KairosListener listener = new KairosListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", response);
            }

            @Override
            public void onFail(String response) {
                Log.d("KAIROS DEMO", response);
            }
        };

        /* * * logic block * * */
        try {
            //Enroll First Image
            Bitmap bitmap1 = ((BitmapDrawable)image1.getDrawable()).getBitmap();
            String subjectId = "subject";
            String galleryId = "1";
            myKairos.enroll(bitmap1, subjectId, galleryId, null, null, null, listener);

            //Detect Second Image
            Bitmap bitmap2 = ((BitmapDrawable)image2.getDrawable()).getBitmap();
            //myKairos.recognize(bitmap2, galleryId, null, null, null, threshold, listener);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                if (flag == 1){
                    image1.setImageBitmap(thumbnail);
                }
                else {
                    image2.setImageBitmap(thumbnail);
                }

            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();
                String[] projection = {MediaStore.MediaColumns.DATA};
                Cursor cursor = managedQuery(selectedImageUri, projection, null, null,
                        null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                cursor.moveToFirst();

                String selectedImagePath = cursor.getString(column_index);

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
                if (flag == 1){
                    image1.setImageBitmap(bm);
                }
                else {
                    image2.setImageBitmap(bm);
                }
            }
        }
    }
}