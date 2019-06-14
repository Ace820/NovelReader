package com.baidu.zhangche.novelreader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtParser {
    int novelIndex = 0;
    static Context mContext;
    public TxtParser(Context context) {
mContext = context;
    }

    public int getChapterIndex() {
        return novelIndex;
    }

    public StringBuffer getChapterData(int chapter) {
        StringBuffer chapterData = new StringBuffer();
        return chapterData;
    }

    public void setChapterIndex(int index) {
        novelIndex = index;
    }

    public static void initChapter(String novelPath, SQLiteDatabase dateBase,String table) throws IOException {
//        ArrayList<ContentValues> chapterList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(novelPath));
        String line;
        ContentValues contentValue = new ContentValues();
        String chapter = "";
//        DatabaseFunc databaseFunc = new DatabaseFunc(mContext);
        int index = 0;
        contentValue.put("chapter_title","序章");
        while ((line = bufferedReader.readLine()) != null) {
            if (isTitle(line)) {
                contentValue.put("chapter_index",index);
                contentValue.put("chapter_data",chapter);
                Log.d(TxtParser.class.toString(),"get title " + contentValue.getAsString("chapter_title"));
//                chapterList.add(contentValue);
                DatabaseFunc.insert(dateBase,table,contentValue);
                chapter = "";
                index ++;
                contentValue.put("chapter_title",line.trim());
            }
            chapter += "\n" + line;
        }
        //Add last chapter
        contentValue.put("chapter_index",index);
        contentValue.put("chapter_data",chapter);
        DatabaseFunc.insert(dateBase,table,contentValue);
//        chapterList.add(contentValue);
//        databaseFunc.insert(dateBase,table,contentValue);
//        return chapterList;
    }

    public static String getRealName(String novelPath) {
        File file = new File(novelPath);
        String name = file.getName().toLowerCase().replaceAll(".txt","");
        if(name.matches("[0-9]+")) {

            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(novelPath));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    if(line.length() < 1)
                        continue;
                    if(line.length() > 10)
                        continue;
                    if (line.matches("[- ]+"))
                        continue;
                    if(isTitle(line))
                        break;
                    name = line;
                    break;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return name;
    }
/*章节名应有属性：
* 1.不应大于30字符或者小于1字符
* 2.可能为第[0-9,一-九]章/回/节 xxxx
* 3.可能为[0-9，一-九]章/回/节 xxxx
* 4.可能为第[0-9，一-九] xxxx
* 5.可能为[0-9，一-九] xxxx
* 6.不应该用“或者"开头
* */
    private static boolean isTitle(String line) {
        if (line.length() > 30) return false;
        if (line.length() < 1) return false;
        String temp = line.replaceAll("\\s"," ");
        temp = temp.trim();
        if (temp.charAt(0) == '"') return false;
        if (temp.charAt(0) == '”') return false;
        if (temp.endsWith("。")) return false;
        if (temp.endsWith("?")) return false;
        if (temp.endsWith("!")) return false;
        if (temp.endsWith("\"")) return false;
        String[] patternKey = {
                "^第[0-9一二三四五六七八九零]+[章节回]? ",
                "^第[0-9一二三四五六七八九零]+ ",
                "^[0-9一二三四五六七八九零]+[章节回]? ",
                "^[0-9一二三四五六七八九零]+ ",
        };
        Pattern[] patterns = new Pattern[patternKey.length];
        for(int i =0; i< patternKey.length; i++) {
            patterns[i] = Pattern.compile(patternKey[i]);
        }

        for (Pattern pattern:patterns) {
            Matcher matcher = pattern.matcher(temp);
            if(matcher.find()) {
                Log.d("zhangche","title ? is " + temp);
                return true;
            }
        }
        return false;
    }
}
