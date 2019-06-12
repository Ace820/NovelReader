package com.baidu.zhangche.novelreader;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TxtParser {
    int novelIndex = 0;
    public TxtParser(String Name,String FilePath) {

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

    public static ArrayList initChapter(String novelPath) throws IOException {
        ArrayList<ContentValues> chapterList = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(novelPath));
        String line;
        ContentValues contentValue = new ContentValues();
        String chapter = "";
        while ((line = bufferedReader.readLine()) != null) {
            if (isTitle(line)) {
                contentValue.put("chapter_data",chapter);
                chapterList.add(contentValue);
                chapter = "";
                contentValue.put("chapter_index",chapterList.size());
                contentValue.put("chapter_title",line);
            }
            //Add last chapter
            contentValue.put("chapter_data",chapter);
            chapterList.add(contentValue);
            chapter += "\n" + line;
        }
        return chapterList;
    }

    public String getRealName(String novelPath) {
        return null;
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
        String temp = line.replaceAll("\\s*","");
        if (temp.charAt(0) == '"') return false;
        if (temp.charAt(0) == '”') return false;
        String[] patternKey = {"^第[0-9一二三四五六七八九零]+","^[0-9一二三四五六七八九零]+"};
        Pattern[] patterns = new Pattern[patternKey.length];
        for(int i =0; i< patternKey.length; i++) {
            patterns[i] = Pattern.compile(patternKey[i]);
        }

        for (Pattern pattern:patterns) {
            Matcher matcher = pattern.matcher(temp);
            if(matcher.find()) return true;
        }
        return false;
    }
}
