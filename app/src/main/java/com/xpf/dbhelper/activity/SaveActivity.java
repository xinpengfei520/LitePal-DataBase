package com.xpf.dbhelper.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.xpf.dbhelper.R;
import com.xpf.dbhelper.adapter.DataArrayAdapter;
import com.xpf.dbhelper.model.UserBean;

import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的保存页面
 */
public class SaveActivity extends Activity implements OnClickListener {

    private EditText mNameEdit;  // 用户名
    private EditText mRoomEdit;  // 房间号
    private EditText mPhoneEdit; // 手机号
    private ProgressBar mProgressBar;
    private Button mSaveBtn;
    private ListView mDataListView;
    private DataArrayAdapter mAdapter;
    private List<List<String>> mList = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.save_sample_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mNameEdit = (EditText) findViewById(R.id.singer_name_edit);
        mRoomEdit = (EditText) findViewById(R.id.singer_age_edit);
        mPhoneEdit = (EditText) findViewById(R.id.singer_gender_edit);
        mSaveBtn = (Button) findViewById(R.id.save_btn);
        mDataListView = (ListView) findViewById(R.id.data_list_view);
        mSaveBtn.setOnClickListener(this);
        mAdapter = new DataArrayAdapter(this, 0, mList);
        mDataListView.setAdapter(mAdapter);
        populateDataFromDB(); // 显示本地已保存的数据
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_btn:
                try {
                    UserBean userBean = new UserBean();
                    userBean.setName(mNameEdit.getText().toString());
                    userBean.setRoom(mRoomEdit.getText().toString());
                    userBean.setPhone(mPhoneEdit.getText().toString());
                    userBean.save();
                    // 刷新列表
                    refreshListView(userBean.getId(), userBean.getName(), userBean.getRoom(), userBean.getPhone());
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "错误或非法的参数", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    // 从数据库迁移数据
    private void populateDataFromDB() {
        mProgressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                List<String> columnList = new ArrayList<String>();
                columnList.add("id");
                columnList.add("name");
                columnList.add("room");
                columnList.add("phone");
                mList.add(columnList);
                Cursor cursor = null;
                try {
                    // 根据id从数据库对应的表中查询数据并显示(一个Bean类对应一张表且表名全为小写)
                    cursor = Connector.getDatabase().rawQuery("select * from userbean order by id", null);
                    if (cursor.moveToFirst()) {
                        do {
                            long id = cursor.getLong(cursor.getColumnIndex("id"));
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
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    private void refreshListView(long id, String name, String room, String phone) {
        List<String> list = new ArrayList<String>();
        list.add(String.valueOf(id));
        list.add(name);
        list.add(room);
        list.add(phone);
        mList.add(list);
        mAdapter.notifyDataSetChanged();
        mDataListView.setSelection(mList.size());
    }

}