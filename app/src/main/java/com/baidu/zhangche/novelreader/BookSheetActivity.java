package com.baidu.zhangche.novelreader;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class BookSheetActivity extends AppCompatActivity {
    Context context = BookSheetActivity.this;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ConfigFunc config = new ConfigFunc(context);
        if (config.currentBook.equals("")) {
            Intent intent = new Intent();
            intent.setClass(BookSheetActivity.this,FullscreenActivity.class);
            startActivity(intent);
        }
    }
}
