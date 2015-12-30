package com.bignerdranch.android.doppl;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.kairos.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jacobo on 12/30/15.
 */
public class KairosManager extends AsyncTask<ArrayList<Bitmap>, Void, Void> {

    // Kairos JSON responee parse constants
    public static final String IMAGES = "images";
    public static final String TRANSACTION = "transaction";
    public static final String CONFIDENCE = "confidence";
    public static final String ATTRIBUTES = "attributes";
    public static final String GENDER = "gender";
    public static final String TYPE = "type";

    // Other Kairos call variables...
    public static String threshold = "0.0";

    // MainActivity Context
    private Context mContext;

    public KairosManager(Context context){
        mContext = context;
    }


    @Override
    protected Double doInBackground(ArrayList<Bitmap> params){

        Bitmap bm1 = params.get(0);
        Bitmap bm2 = params.get(1);

        return compareImages(bm1, bm2);
    }

    @Override
    protected Double onPostExecute(String result){
        // Return confidence or error to MainACtivity

    }

    public Double compareImages(Bitmap bm1, Bitmap bm2){
        // Kairos Call here!

        Double confidence = 0.0;

        /* * * instantiate a new kairos instance * * */
        Kairos myKairos = new Kairos();

        /* * * set authentication * * */
        String app_id = "45001bf1";
        String api_key = "ae1e5431443f875a90085d5b27a36b17";
        myKairos.setAuthentication(mContext, app_id, api_key);

        // listener
        KairosListener listener = new KairosListener() {

            int successCounter = 0;

            @Override
            public void onSuccess(String response) {
                Log.d("KAIROS DEMO", response);
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    if (successCounter == 2) {
                        if (IMAGES != null) {
                            JSONArray images2 = jsonResponse.getJSONArray(IMAGES);
                            JSONObject image = images2.getJSONObject(0);
                            JSONObject subimage = image.getJSONObject(TRANSACTION);
                            confidence = subimage.getDouble(CONFIDENCE);
                            Log.d("Percent: ", confidence.toString());
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

        return confidence;
    }
}

