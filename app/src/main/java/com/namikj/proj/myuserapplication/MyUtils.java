package com.namikj.proj.myuserapplication;

import android.text.TextUtils;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

/**
 * Created by michael on 2016/8/24.
 */
public class MyUtils
{
    static public InputStream requestByUrl(String urlStr, String method, String data)
    {
        if (TextUtils.isEmpty(method))
        {
            method = "GET";
        }
        try
        {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setRequestProperty("Content-Type", "application/json");
            if (data!=null)
            {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter wt = new OutputStreamWriter(os, "utf-8");
                wt.write(data);
                wt.flush();
                os.close();
                wt.close();
            }
            int responsecode = conn.getResponseCode();
            if (responsecode == 200)
            {
                return conn.getInputStream();
            }

        } catch (IOException e)
        {
            Log.v("IOE", e.getMessage());
        }
        return  null;
    }

}
