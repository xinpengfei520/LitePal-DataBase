package com.xpf.dbhelper;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.xpf.dbhelper.activity.DeleteActivity;
import com.xpf.dbhelper.activity.QueryActivity;
import com.xpf.dbhelper.activity.SaveActivity;
import com.xpf.dbhelper.activity.UpdateActivity;

import org.litepal.tablemanager.Connector;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mSaveBtn;
    private Button mUpdateBtn;
    private Button mDeleteBtn;
    private Button mQueryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSaveBtn = (Button) findViewById(R.id.save_sample_btn);
        mUpdateBtn = (Button) findViewById(R.id.update_sample_btn);
        mDeleteBtn = (Button) findViewById(R.id.delete_sample_btn);
        mQueryBtn = (Button) findViewById(R.id.query_sample_btn);
        mSaveBtn.setOnClickListener(this);
        mUpdateBtn.setOnClickListener(this);
        mDeleteBtn.setOnClickListener(this);
        mQueryBtn.setOnClickListener(this);

        SQLiteDatabase db = Connector.getDatabase();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_sample_btn:
                startActivity(new Intent(MainActivity.this, SaveActivity.class));
                break;
            case R.id.update_sample_btn:
                startActivity(new Intent(MainActivity.this, UpdateActivity.class));
                break;
            case R.id.delete_sample_btn:
                startActivity(new Intent(MainActivity.this, DeleteActivity.class));
                break;
            case R.id.query_sample_btn:
                startActivity(new Intent(MainActivity.this, QueryActivity.class));
                break;
            default:
                break;
        }
    }
}
