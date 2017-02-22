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

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库的删除页面
 */
public class DeleteActivity extends Activity implements OnClickListener {

    private EditText mSingerIdEdit;
    private EditText mNameToDeleteEdit;
    private EditText mAgeToDeleteEdit;
    private ProgressBar mProgressBar;
    private Button mDeleteBtn1;
    private Button mDeleteBtn2;
    private ListView mDataListView;
    private DataArrayAdapter mAdapter;
    private List<List<String>> mList = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delete_sample_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mSingerIdEdit = (EditText) findViewById(R.id.singer_id_edit);
        mNameToDeleteEdit = (EditText) findViewById(R.id.name_to_delete);
        mAgeToDeleteEdit = (EditText) findViewById(R.id.age_to_delete);
        mDeleteBtn1 = (Button) findViewById(R.id.delete_btn1);
        mDeleteBtn2 = (Button) findViewById(R.id.delete_btn2);
        mDataListView = (ListView) findViewById(R.id.data_list_view);
        mDeleteBtn1.setOnClickListener(this);
        mDeleteBtn2.setOnClickListener(this);
        mAdapter = new DataArrayAdapter(this, 0, mList);
        mDataListView.setAdapter(mAdapter);
        populateDataFromDB();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.delete_btn1: // 删除指定Id的数据
                try {
                    int rowsAffected = DataSupport.delete(UserBean.class,
                            Long.parseLong(mSingerIdEdit.getText().toString()));
                    Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                    populateDataFromDB();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, "错误或非法的参数", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.delete_btn2: // 删除指定符合条件的数据
                try {
                    int rowsAffected = DataSupport.deleteAll(UserBean.class, "name=? and room=?",
                            mNameToDeleteEdit.getText().toString(), mAgeToDeleteEdit.getText()
                                    .toString());
                    Toast.makeText(this, "已删除符合条件的数据", Toast.LENGTH_SHORT).show();
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