package com.namikj.proj.myuserapplication;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;


/**
 * Created by michael on 2016/8/28.
 */
public class GetImageTask extends AsyncTask<String,Void,Bitmap>
{
    public ImageView imageView;
    public GetImageTask(ImageView imageView) {
        this.imageView = imageView;
    }
    public Bitmap doInBackground(String... params)
    {
        InputStream is = MyUtils.requestByUrl(MainActivity.HOST + params[0], null, null);
        return BitmapFactory.decodeStream(is);
    }

    public void onPostExecute(Bitmap result) {
        imageView.setImageBitmap(result);
    }
}
