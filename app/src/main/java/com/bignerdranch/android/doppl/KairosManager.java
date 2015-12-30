package com.bignerdranch.android.doppl;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.kairos.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacobo on 12/30/15.
 */
public class KairosManager extends AsyncTask<Bitmap, Void, JSONObject> {

    // Kairos JSON response parse constants
    public static final String IMAGES = "images";
    public static final String TRANSACTION = "transaction";
    public static final String CONFIDENCE = "confidence";

    // Other Kairos call variables...
    public final String threshold = "0.0";
    public static Double confidence = 0.0;
    public static int successCounter = 0;
    public static JSONObject jsonResponse;

    // MainActivity Context
    private Activity mActivity;

    //
    TextView similarity_percent;


    public KairosManager(Activity activity){
        mActivity = activity;
        similarity_percent = (TextView) activity.findViewById(R.id.similarity_percent);
    }

    // Take both images in the array and call compareImages which calls the service.
    @Override
    protected JSONObject doInBackground(Bitmap... params){
        Bitmap bm1 = params[0];
        Bitmap bm2 = params[1];
        Kairos myKairos = new Kairos();

        /* * * set authentication * * */
        String app_id = "45001bf1";
        String api_key = "ae1e5431443f875a90085d5b27a36b17";
        myKairos.setAuthentication(mActivity, app_id, api_key);

        // listener
        KairosListener listener = new KairosListener() {

            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", response);
                try {
                    if (successCounter == 2) {
                        if (IMAGES != null) {
                            jsonResponse = new JSONObject(response);
                            successCounter = 0;
                        }
                    }
                    successCounter++;
                    Log.d("Response_number: ", Integer.toString(successCounter));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String response) {
                Log.d("KAIROS DEMO", response);
            }
        };


        /* * * logic block * */
        try {
            //Enroll First Image
            String subjectId = "subject";
            String galleryId = "1";

            myKairos.enroll(bm1, subjectId, galleryId, null, null, null, listener);

            //Detect Second Image
            myKairos.recognize(bm2, galleryId, null, threshold, null, null, listener);

            //Delete Gallery
            myKairos.deleteGallery(galleryId, listener);


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return jsonResponse;
    }

    // Parse JSONObject response and return 'confidence' value to MainActivity
    @Override
    protected void onPostExecute(JSONObject result){
        // Return 'confidence' or error to MainActivity
        try{
            JSONArray TwoImages = result.getJSONArray(IMAGES);
            JSONObject image = TwoImages.getJSONObject(0);
            JSONObject subimage = image.getJSONObject(TRANSACTION);
            Double confidence = subimage.getDouble(CONFIDENCE);
            Log.d("Percent: ", confidence.toString());
            similarity_percent.setText(confidence.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

//    // Call Kairos API and return JSONObject response to doInBackground()
//  public JSONObject compareImages(Bitmap bm1, Bitmap bm2){
//        // Kairos Call here!
//
//        /* * * instantiate a new kairos instance * * */
//
//    }

}

