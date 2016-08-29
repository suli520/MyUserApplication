package com.namikj.proj.myuserapplication;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by michael on 2016/8/28.
 */
public class UserAdapter extends ArrayAdapter<User>
{
    private Activity context;
    List<User> userList;
    public UserAdapter(Activity context, int resource, List<User> userList) {
        super(context, resource, userList);
        this.context = context;
        this.userList = userList;
    }

    public UserAdapter(Activity context, int resource, User[] userArr) {
        super(context, resource, userArr);
        this.context = context;
        this.userList = Arrays.asList(userArr);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView;
        // LayoutInflater:作用类似于findViewById()。不同点是LayoutInflater是用来找res/layout/下的xml布局文件，并且实例化
        LayoutInflater inflater = context.getLayoutInflater();
        ////将xml布局转换为view对象
        rowView = inflater.inflate(R.layout.user_layout, parent, false);
        ////利用view对象，找到布局中的组件
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        TextView firstLine = (TextView) rowView.findViewById(R.id.firstLine);
        TextView secondLine = (TextView) rowView.findViewById(R.id.secondLine);
        User user = userList.get(position);
        if(!TextUtils.isEmpty(user.getPhoto()))
        {
            new GetImageTask(imageView).execute(user.getPhoto());
        }
        Log.v("user name", user.getName());
        firstLine.setText(user.getName());
        secondLine.setText(user.getTel());
        return rowView;
    }
}
