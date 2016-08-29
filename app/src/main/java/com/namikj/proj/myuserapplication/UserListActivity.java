package com.namikj.proj.myuserapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UserListActivity extends AppCompatActivity
{
    int n;
    UserAdapter adapt;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
    }
    public void onStart()
    {
        super.onStart();
        final ArrayList<User> users=new ArrayList<User>();
        final ArrayList<String> names=new ArrayList<String>();
        new Thread()
        {
            public void run()
            {
                InputStream is=MyUtils.requestByUrl(MainActivity.HOST+"/users","GET",null);
                Gson gson = new Gson();
                User[] userArr = gson.fromJson(new InputStreamReader(is), User[].class);
                //users.clear();
                users.addAll(Arrays.asList(userArr));
                //names.clear();
                for(User user : users)
                {
                    names.add(user.getName());
                }
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        ListView listView = (ListView)findViewById(R.id.userlist);
                        UserAdapter adapter=new UserAdapter(UserListActivity.this,R.layout.user_layout,users);
                        adapt=adapter;
                        listView.setAdapter(adapter);
                        //adapter.notifyDataSetChanged();
                        adapter.notifyDataSetChanged();
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            public void onItemClick(AdapterView<?> var1, View var2, final int position, long var4)
                            {
                                Intent intent = new Intent(UserListActivity.this,UserInfoActivity.class);
                                intent.putExtra("key",users.get(position));
                                startActivity(intent);
                            }
                        });
                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
                        {
                            public boolean onItemLongClick(AdapterView<?> adapterView,View view,int i,long l)
                            {
                                n=i;
                                new Thread()
                                {
                                    public void run()
                                    {
                                        InputStream is=MyUtils.requestByUrl(MainActivity.HOST+"/user/"+users.get(n).getId(),"DELETE",null);
                                        Gson gson=new Gson();
                                        Result result=gson.fromJson(new InputStreamReader(is),Result.class);
                                        if (result.getStatus().compareTo("success")==0)
                                        {
                                            runOnUiThread(new Runnable()
                                            {
                                                @Override
                                                public void run()
                                                {
                                                    Toast.makeText(getApplicationContext(),"删除成功！",Toast.LENGTH_SHORT).show();
                                                    users.remove(users.get(n));
                                                    //adapt.setNotifyOnChange(true);
                                                    adapt.notifyDataSetChanged();
                                                }
                                            });
                                        }
                                    }
                                }.start();
                                return true;
                            }
                        });
                    }
                });
            }
        }.start();
    }
    public void onAdd(View view)
    {
        Intent intent=new Intent(UserListActivity.this,UserInfoActivity.class);
        intent.putExtra("key",new User());
        //startActivityForResult(intent,101);
        startActivity(intent);
    }


}
