package com.baidu.zhangche.novelreader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class BookSheetActivity extends AppCompatActivity {
    Context context = BookSheetActivity.this;
    LinearLayout bookSheet;
    Button addBookBtn;
    ConfigFunc config;
    DatabaseFunc database;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booksheet);
        config = new ConfigFunc(context);
        database = new DatabaseFunc(context);
        bookSheet = findViewById(R.id.book_sheet);
        verifyStoragePermissions(BookSheetActivity.this);
        addBookBtn = findViewById(R.id.add_book);
        if (config.currentBook.equals("")) {
//            addBookBtn.setVisibility(View.GONE);
//            String newBook = getBook(Environment.getExternalStorageDirectory());
//            try {
//                database.addBook(newBook);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
//        try {
//            database.addBook("/storage/emulated/0/1.txt");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        showBookSheet();
    }

    protected void showBookSheet() {
        bookSheet.removeAllViews();
        ArrayList<String> bookList = database.getBookList();
        if (!bookList.isEmpty()) {
            for(String book :database.getBookList()) {
                Button bookBtn = new Button(this);
                bookBtn.setText(database.getBookName(book));
                bookBtn.setOnClickListener(new bookButtonListener(book));
                bookBtn.setAllCaps(false);
                bookBtn.setGravity(Gravity.LEFT);
                bookSheet.addView(bookBtn);
            }
        }
        bookSheet.setVisibility(View.VISIBLE);
        addBookBtn.setVisibility(View.VISIBLE);
        addBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBookBtn.setVisibility(View.GONE);
                bookSheet.setVisibility(View.GONE);
                String newBook = getBook(Environment.getExternalStorageDirectory());
                Log.d(context.toString(),"get file is " + newBook);
                try {
//                    database.addBook(newBook);
                    showBookSheet();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }
    protected String getBook(File rootFile) {
        if (rootFile == null)
            rootFile = Environment.getExternalStorageDirectory();

        Toast.makeText(context, rootFile.getPath(), Toast.LENGTH_SHORT).show();
        LinearLayout file_broswer = findViewById(R.id.file_browser);
        file_broswer.removeAllViews();
        if (rootFile.isFile()) {
            Log.d(context.toString(),"get file " + rootFile.getPath());
            try {
                database.addBook(rootFile.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return rootFile.getPath();
        }

        Button parentFileButton = new Button(this);
        parentFileButton.setText("..");
        parentFileButton.setGravity(Gravity.LEFT);
        parentFileButton.setOnClickListener(new fileButtonListener(rootFile.getParentFile()));
        file_broswer.addView(parentFileButton);

        ArrayList<File> sortedFile= sortFile(rootFile.listFiles());
        for (File file:sortedFile) {
            Button fileButton = new Button(this);
            fileButton.setText(file.getName());
            fileButton.setOnClickListener(new fileButtonListener(file));
            fileButton.setAllCaps(false);
            fileButton.setGravity(Gravity.LEFT);
            file_broswer.addView(fileButton);
        }
        return null;
    }
    class fileButtonListener implements View.OnClickListener {
        File innerFile;
        public fileButtonListener(File file) {
            innerFile = file;
        }
        @Override
        public void onClick(View v) {
            getBook(innerFile);
        }
    }

    class bookButtonListener implements View.OnClickListener {
        String bookId;
        public bookButtonListener(String book) {
            bookId = book;
        }
        @Override
        public void onClick(View v) {
            config.setCurrentBook(bookId);

            Intent intent = new Intent();
            intent.setClass(BookSheetActivity.this,BookReaderActivity.class);
            startActivity(intent);

        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        final int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    private ArrayList<File> sortFile(File[] files) {
        String rootPath = files[0].getParent();
        ArrayList<String> dirArray = new ArrayList<>();
        ArrayList<String> fileArray = new ArrayList<>();
        ArrayList<File> allArray = new ArrayList<>();
        for(File file:files) {
            if(file.isFile()) {
                fileArray.add(file.getName());
            } else {
                dirArray.add(file.getName());
            }
        }
        Collections.sort(dirArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        Collections.sort(fileArray, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        for(String file:dirArray) {
            if(file.charAt(0) == '.')
                continue;
            allArray.add(new File(rootPath + "/" + file));
        }
        for(String file:fileArray) {
            if(file.charAt(0) == '.')
                continue;
            allArray.add(new File(rootPath + "/" + file));
        }
        return allArray;
    }
}
