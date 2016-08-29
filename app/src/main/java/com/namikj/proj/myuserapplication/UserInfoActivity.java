package com.namikj.proj.myuserapplication;

import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Interpolator;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class  UserInfoActivity extends AppCompatActivity
{
    public final static int IMAGE_REQUEST_CODE=10;
    User user=new User();
    String picturePath="";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Intent it=getIntent();
        user=(User) it.getSerializableExtra("key");
        if (user.getId()!=null)
        {
            new Thread()
            {
                public void run()
                {
                    InputStream is=MyUtils.requestByUrl(MainActivity.HOST+"/user/"+user.getId(),"GET",null);
                    Gson gson=new Gson();
                    final User user=gson.fromJson(new InputStreamReader(is),User.class);
                    final InputStream imageIs = MyUtils.requestByUrl(MainActivity.HOST + user.getPhoto(), "GET", null);
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageIs);
                    try
                    {
                        imageIs.close();
                        is.close();
                    }catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            ((ImageView)findViewById(R.id.photo)).setImageBitmap(bitmap);
                            ((EditText)findViewById(R.id.name)).setText(user.getName());
                            ((EditText)findViewById(R.id.telephone)).setText(user.getTel());
                            ((EditText)findViewById(R.id.password)).setText(user.getPassword());
                            ((Button)findViewById(R.id.addUser)).setVisibility(View.INVISIBLE);
                            ((Button)findViewById(R.id.selectPhoto)).setVisibility(View.VISIBLE);
                            ((Button)findViewById(R.id.onPut)).setVisibility(View.VISIBLE);
                        }
                    });
                }
            }.start();
        }else
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    ((Button)findViewById(R.id.addUser)).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.selectPhoto)).setVisibility(View.VISIBLE);
                    ((Button)findViewById(R.id.onPut)).setVisibility(View.INVISIBLE);
                }
            });
        }
    }
    public void onPut(View view)
    {
        new  Thread()
        {
            public void run()
            {
                final Gson gson=new Gson();
                InputStream ins=MyUtils.requestByUrl(MainActivity.HOST+"/user/"+user.getId(),"GET",null);
                User user=gson.fromJson(new InputStreamReader(ins),User.class);
                String name=((EditText)findViewById(R.id.name)).getText().toString();
                String tel=((EditText)findViewById(R.id.telephone)).getText().toString();
                String pw=((EditText)findViewById(R.id.password)).getText().toString();
                user.setName(name);
                user.setTel(tel);
                user.setPassword(pw);
                String content=gson.toJson(user).toString();
                InputStream is=MyUtils.requestByUrl(MainActivity.HOST+"/user/"+user.getId(),"PUT",content);
                Result result=gson.fromJson(new InputStreamReader(is),Result.class);
                if (result.getStatus().compareTo("success")==0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"修改成功！",Toast.LENGTH_SHORT).show();
                            UserInfoActivity.this.finish();
                        }
                    });
                }
            }
        }.start();
    }
    public void addUser(View view)
    {
        new Thread()
        {
            public void run()
            {
                try
                {
                    User user=new User();
                    Gson gson=new Gson();
                    String name=((EditText)findViewById(R.id.name)).getText().toString();
                    String tel=((EditText)findViewById(R.id.telephone)).getText().toString();
                    String pw=((EditText)findViewById(R.id.password)).getText().toString();
                    user.setName(name);
                    user.setTel(tel);
                    user.setPassword(pw);
                    OkHttpClient client = new OkHttpClient();
                    File file = new File(picturePath);
                    MultipartBody body =  new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("photo", file.getName(),
                                    RequestBody.create(MediaType.parse("text/plain"), file))
                            .addFormDataPart("name",user.getName())
                            .addFormDataPart("password",user.getPassword())
                            .addFormDataPart("tel",user.getTel())
                        .build();
                    Request request = new Request.Builder().url(MainActivity.HOST + "/user").post(body).build();
                    Response response = client.newCall(request).execute();
                    Log.v("return body", response.body().string());
                    if ( response.code() == HttpURLConnection.HTTP_OK)
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),"添加成功！",Toast.LENGTH_SHORT).show();
                                UserInfoActivity.this.finish();
                            }
                        });
                    }
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    public void selectPhoto(View view)
    {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode==IMAGE_REQUEST_CODE&&resultCode==RESULT_OK&&data!=null)
        {
            Uri selectedImage=data.getData();

            String[] filePathColumn={MediaStore.Images.Media.DATA};
            Cursor cursor=getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();
            int columnIndex=cursor.getColumnIndex(filePathColumn[0]);
            picturePath=cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap= BitmapFactory.decodeFile(picturePath);
            ((ImageView)findViewById(R.id.photo)).setImageBitmap(bitmap);
        }
    }
//    public void onDelete(View view)
//    {
//        new Thread()
//        {
//            public void run()
//            {
//                InputStream is=MyUtils.requestByUrl(MainActivity.HOST+"/user/"+user.getId(),"DELETE",null);
//                Gson gson=new Gson();
//                Result result=gson.fromJson(new InputStreamReader(is),Result.class);
//                if (result.getStatus().compareTo("success")==0)
//                {
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Toast.makeText(getApplicationContext(),"删除成功！",Toast.LENGTH_SHORT).show();
//                            UserInfoActivity.this.finish();
//                        }
//                    });
//                }
//            }
//        }.start();
//
//    }

}
