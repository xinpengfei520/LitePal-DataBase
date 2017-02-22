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
 * 数据库的修改页面
 */
public class UpdateActivity extends Activity implements OnClickListener {

    private EditText mIdEdit;
    private EditText mNameEdit;
    private EditText mRoomEdit;
    private EditText mNameToUpdateEdit;
    private EditText mRoomToUpdateEdit;

    private ProgressBar mProgressBar;
    private Button mUpdateBtn1;
    private Button mUpdateBtn2;
    private ListView mDataListView;
    private DataArrayAdapter mAdapter;

    private List<List<String>> mList = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_sample_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mIdEdit = (EditText) findViewById(R.id.singer_id_edit);
        mNameEdit = (EditText) findViewById(R.id.singer_name_edit);
        mRoomEdit = (EditText) findViewById(R.id.singer_age_edit);
        mNameToUpdateEdit = (EditText) findViewById(R.id.name_to_update);
        mRoomToUpdateEdit = (EditText) findViewById(R.id.age_to_update);
        mUpdateBtn1 = (Button) findViewById(R.id.update_btn1);
        mUpdateBtn2 = (Button) findViewById(R.id.update_btn2);
        mDataListView = (ListView) findViewById(R.id.data_list_view);
        mUpdateBtn1.setOnClickListener(this);
        mUpdateBtn2.setOnClickListener(this);
        mAdapter = new DataArrayAdapter(this, 0, mList);
        mDataListView.setAdapter(mAdapter);
        populateDataFromDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_btn1: // 修改指定id的数据
                try {
                    UserBean userBean = new UserBean();
                    userBean.setName(mNameEdit.getText().toString());
                    userBean.setRoom(mRoomEdit.getText().toString());
                    userBean.setPhone("13211110000");
                    int rowsAffected = userBean
                            .update(Long.parseLong(mIdEdit.getText().toString()));
                    if (rowsAffected == 1) {
                        Toast.makeText(this, "已修改指定id的数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "修改失败或指定的id不存在！", Toast.LENGTH_SHORT).show();
                    }
                    populateDataFromDB();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "错误或非法的参数", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.update_btn2: // 修改指定符合条件字段的数据
                try {
                    UserBean userBean = new UserBean();
                    userBean.setName(mNameEdit.getText().toString());
                    userBean.setRoom(mRoomEdit.getText().toString());
                    int rowsAffected = userBean.updateAll("name=? and room=?", mNameToUpdateEdit.getText()
                            .toString(), mRoomToUpdateEdit.getText().toString());
                    if (rowsAffected == 1) {
                        Toast.makeText(this, "已修改符合条件的数据", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "修改失败或找到符合条件的数据！", Toast.LENGTH_SHORT).show();
                    }
                    populateDataFromDB();
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
                    cursor = Connector.getDatabase().rawQuery("select * from userbean order by id", null);
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

}