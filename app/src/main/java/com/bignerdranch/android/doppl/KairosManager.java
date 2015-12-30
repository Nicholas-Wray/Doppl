package com.bignerdranch.android.doppl;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by Jacobo on 12/30/15.
 */
public class KairosManager extends AsyncTask<List, Void, Void> {

    @Override
    protected String doInBackground(List params){
        Bitmap bm1 = params.get(0);
        Bitmap bm2 = params.get(0);

        return compareImages(bm1, bm2);
    }

    @Override
    protected Double onPostExecute(String result){

    }
}
