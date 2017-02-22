package com.xpf.dbhelper;

import android.app.Application;

import org.litepal.LitePal;

/**
 * Created by xpf on 2017/2/21:)
 * Function:进行初始化等相关操作
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化LitePal库
        LitePal.initialize(this);
    }
}
