package com.namikj.proj.myuserapplication;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    public final static String HOST="http://192.168.0.117:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onLogin(View view)
    {
        String name=((EditText)findViewById(R.id.name)).getText().toString();
        String pw=((EditText)findViewById(R.id.password)).getText().toString();
        final User myUser=new User(name,pw);
        new Thread()
        {
            public void run()
            {
                Gson gson=new Gson();
                //Log.v("login user", gson.toJson(myUser));
                InputStream is=MyUtils.requestByUrl(HOST+"/login","POST",gson.toJson(myUser));
                final User user=gson.fromJson(new InputStreamReader(is),User.class);
                if (user.getId()!=null)
                {
                    Log.v("test","sucess");
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast ts=Toast.makeText(getApplicationContext(),"登录成功！",Toast.LENGTH_SHORT);
                            ts.show();
                            Intent intent=new Intent(MainActivity.this,UserListActivity.class);
                            startActivity(intent);
                        }
                    });
                } else
                {
                     runOnUiThread(new Runnable()
                     {
                         @Override
                         public void run()
                         {
                             Toast ts=Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT);
                             ts.show();
                         }
                     });
                }
            }
        }.start();
    }

}
