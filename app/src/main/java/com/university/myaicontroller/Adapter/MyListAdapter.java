package com.university.myaicontroller.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.university.myaicontroller.Bean.Bean_mainList;
import com.university.myaicontroller.R;

import java.util.List;

public class MyListAdapter extends BaseAdapter{
    //声明一个链表和context对象
    private List<Bean_mainList> mList;
    private Context context;

    public MyListAdapter(List<Bean_mainList> mList, Context context)
    {
        this.context = context;
        this.mList = mList;
    }

    //获取数据的数量
    @Override
    public int getCount() {
        return mList.size();
    }

    //获取数据的内容
    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    //获取数据的id
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载一个适配器
        convertView = LayoutInflater.from(context).inflate(R.layout.list_jiaju,parent,false);
        //实例化元件
        ImageView item_img = (ImageView) convertView.findViewById(R.id.iv_image);
        TextView item_name = (TextView)convertView.findViewById(R.id.tv_controllerText);
        //元件获取数据
        item_img.setImageResource(mList.get(position).getImgUrl());
        item_name.setText(mList.get(position).getName());

        return convertView;
    }
}
