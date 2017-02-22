package com.xpf.dbhelper.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.xpf.dbhelper.R;
import com.xpf.dbhelper.adapter.DataArrayAdapter;
import com.xpf.dbhelper.model.UserBean;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库查询操作页面
 */
public class QueryActivity extends Activity implements View.OnClickListener {

    private TextView tv_btn1;
    private TextView tv_btn2;
    private TextView tv_btn3;
    private TextView tv_btn4;
    private TextView tv_btn5;
    private ListView listView;
    private DataArrayAdapter mAdapter;
    private List<List<String>> mList = new ArrayList<List<String>>();
    private List<UserBean> userlist = new ArrayList<UserBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.query_sample_layout);

        tv_btn1 = (TextView) findViewById(R.id.tv_btn1);
        tv_btn2 = (TextView) findViewById(R.id.tv_btn2);
        tv_btn3 = (TextView) findViewById(R.id.tv_btn3);
        tv_btn4 = (TextView) findViewById(R.id.tv_btn4);
        tv_btn5 = (TextView) findViewById(R.id.tv_btn5);
        listView = (ListView) findViewById(R.id.listView);

        tv_btn1.setOnClickListener(this);
        tv_btn2.setOnClickListener(this);
        tv_btn3.setOnClickListener(this);
        tv_btn4.setOnClickListener(this);
        tv_btn5.setOnClickListener(this);

        mAdapter = new DataArrayAdapter(this, 0, mList);
        listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_btn1:
                UserBean userBean = DataSupport.find(UserBean.class, 1);
                if (userBean != null) {
                    userlist.add(userBean);
                    populateDataFromDB();
                } else {
                    Toast.makeText(QueryActivity.this, "没有查找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_btn2:
                List<UserBean> all = DataSupport.findAll(UserBean.class);
                if (all != null && all.size() > 0) {
                    userlist = all;
                    populateDataFromDB();
                } else {
                    Toast.makeText(QueryActivity.this, "没有查找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_btn3:
                List<UserBean> users = DataSupport.where("name=?", "xpf").find(UserBean.class);
                if (users != null && users.size() > 0) {
                    userlist = users;
                    populateDataFromDB();
                } else {
                    Toast.makeText(QueryActivity.this, "没有查找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_btn4:
                List<UserBean> allUsers = DataSupport.where("name=? and room=?", "xin", "A1603").find(UserBean.class);
                if (allUsers != null && allUsers.size() > 0) {
                    userlist = allUsers;
                    populateDataFromDB();
                } else {
                    Toast.makeText(QueryActivity.this, "没有查找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_btn5:
                Cursor cursor = DataSupport.findBySQL("select * from userbean where name=? and room=?", "xin", "A1603");
                if (cursor != null) {
                    populateDataFromDB2(cursor);
                } else {
                    Toast.makeText(QueryActivity.this, "没有查找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void populateDataFromDB() {
        mList.clear();
        List<String> columnList = new ArrayList<String>();
        columnList.add("id");
        columnList.add("name");
        columnList.add("room");
        columnList.add("phone");
        mList.add(columnList);

        if (userlist != null && userlist.size() > 0) {
            for (int i = 0; i < userlist.size(); i++) {
                UserBean user = userlist.get(i);
                List<String> stringList = new ArrayList<String>();
                stringList.add(String.valueOf(user.getId()));
                stringList.add(user.getName());
                stringList.add(user.getRoom());
                stringList.add(user.getPhone());
                mList.add(stringList);
            }
            mAdapter.notifyDataSetChanged();
            userlist.clear();
        }
    }

    // 从数据库迁移数据
    private void populateDataFromDB2(final Cursor cursor) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                userlist.clear();
                List<String> columnList = new ArrayList<String>();
                columnList.add("id");
                columnList.add("name");
                columnList.add("room");
                columnList.add("phone");
                mList.add(columnList);
                try {
                    if (cursor.moveToFirst()) {
                        do {
                            String id = cursor.getString(cursor.getColumnIndex("id"));
                            String name = cursor.getString(cursor.getColumnIndex("name"));
                            String room = cursor.getString(cursor.getColumnIndex("room"));
                            String phone = cursor.getString(cursor.getColumnIndex("phone"));
                            List<String> stringList = new ArrayList<String>();
                            stringList.add(String.valueOf(id));
                            stringList.add(name);
                            stringList.add(room);
                            stringList.add(phone);
                            mList.add(stringList);
                        } while (cursor.moveToNext());
                    }

                    for (UserBean user : userlist) {
                        List<String> stringList = new ArrayList<String>();
                        stringList.add(String.valueOf(user.getId()));
                        stringList.add(user.getName());
                        stringList.add(user.getRoom());
                        stringList.add(user.getPhone());
                        mList.add(stringList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

}