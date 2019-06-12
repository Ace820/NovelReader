package com.baidu.zhangche.novelreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Map;

/* 表结构：：
=================================================================================
table book_list:
所有书籍的基础信息列表，使用文件路径的hash作为主键
-----------------------------------------------------------------------------
_id             |Name     |FilPath    |ChapterIndex     |ChapterPercentage
文件路径hash     |真实书名  |文件路径    |当前章节索引       |当前章节内容百分比
-----------------------------------------------------------------------------
=================================================================================
table [_id]:
书籍的真实内容信息,_id作为表名，章节索引作为主键
------------------------------------------------------
chapter_index      |chapter_title     |chapter_data
章节索引           |章节标题          |章节内容
------------------------------------------------------
=================================================================================
 */
public class DatabaseFunc extends SQLiteOpenHelper {
    private final static String DATABASENAME = "myNovel";
    private final static int DATABASEVERSION = 1;
    SQLiteDatabase novelDB;

    public DatabaseFunc(Context contexts) {
        super(contexts,DATABASENAME,null,DATABASEVERSION);
        novelDB = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            String sql = "CREATE TABLE IF NOT EXISTS book_list "
                    + "(_id VARCHAR(40) PRIMARY KEY,"
                    + " Name VARCHAR(30)  NOT NULL,"
                    + " FilePath VARCHAR(100) NOT NULL,"
                    + " ChapterIndex int)";
            db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public String getChapterData(String book,String index) {
        String sql = "select chapter_data form table " + book + "where chapter_index is " + index;
        Cursor cursor = novelDB.rawQuery(sql,null);
        return cursor.getString(0);
    }

    public void addBook(String bookPath) throws IOException {
        if (bookPath == null)
            return;
        String _id = path2Sha1(bookPath);
        if (_id != null) {
            String sql = "create table " + _id +
                    " (chapter_index int primary key," +
                    " chapter_title varchar(50)," +
                    " chapter_data text)";
            novelDB.execSQL(sql);
        }
        ArrayList chapterList = TxtParser.initChapter(bookPath);
        for (int index = 0; index <chapterList.size(); index++ ) {
            ContentValues contentValues = (ContentValues) chapterList.get(index);
            novelDB.insert(_id,null,contentValues);
        }

    }

    private boolean isBookExist(String bookPath) {
        String sql = "select * from table book_list where FilePath is '" + bookPath + "'";
        Cursor cursor = novelDB.rawQuery(sql,null);
        return (cursor.getCount() != 0);
    }

    public static String path2Sha1(String bookPath) {
        byte[] bookDigest = null;

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(bookPath.getBytes());
            bookDigest = messageDigest.digest();
            // 首先初始化一个字符数组，用来存放每个16进制字符
            char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };

            // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
            char[] resultCharArray =new char[bookDigest.length * 2];

            // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
            int index = 0;
            for (byte b : bookDigest) {
                resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
                resultCharArray[index++] = hexDigits[b& 0xf];
            }
            return new String(resultCharArray);
        } catch(Exception e) {
            e.printStackTrace();
        }

        // 字符数组组合成字符串返回
        return null;
    }
}
