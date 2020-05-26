package com.university.myaicontroller.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.university.myaicontroller.Adapter.MyListAdapter;
import com.university.myaicontroller.Bean.Bean_mainList;
import com.university.myaicontroller.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{

    LinearLayout ll_add;
    ListView itemList;
    MyListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Bean_mainList> mList = new ArrayList<>();
        mList.add(new Bean_mainList("灯光控制",R.mipmap.light_on));
        mList.add(new Bean_mainList("电视控制",R.mipmap.dianshi));
        mList.add(new Bean_mainList("空调控制",R.mipmap.kongtiao));
        mList.add(new Bean_mainList("窗帘控制",R.mipmap.chuanglian));
        //初始化元件
        ll_add = findViewById(R.id.ll_add);
        itemList = (ListView) findViewById(R.id.list_item);
        //new一个自定义的列表适配器，给每本小说填上图片和文字
        adapter = new MyListAdapter(mList,MainActivity.this);
        itemList.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转，界面传值过去
                Intent i;
                if (position == 0){
                    i=new Intent(MainActivity.this, lightControllerActivity.class);
                }else if (position == 1){
                    i=new Intent(MainActivity.this, dianshiControllerActivity.class);
                }else if (position == 2){
                    i=new Intent(MainActivity.this, kongtiaoControllerActivity.class);
                }else{
                    i=new Intent(MainActivity.this, chuanglianControllerActivity.class);
                }
                startActivity(i);
            }
        });

        //按钮监听事件
        ll_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "增加设备", Toast.LENGTH_SHORT).show();
            }
        });
        //

    }

}
