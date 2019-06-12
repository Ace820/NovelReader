package com.baidu.zhangche.novelreader;

import android.content.Context;
import android.content.SharedPreferences;

public class ConfigFunc {
    public int fontSize = 20;
    public int fontColor = 0xffffff;
    public int backgroundColor = 0x70dbdb;
    public boolean autoLink = false;
    public String currentBook = "";
    private static SharedPreferences configSp;

    public ConfigFunc(Context contexts) {
        configSp = contexts.getSharedPreferences("config",Context.MODE_PRIVATE);
        loadConfig();
    }
    public int getFontSize() {
        return fontSize;
    }

    public int getFontColor() {
        return fontColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public boolean getAutoLink() {
        return autoLink;
    }
    public String getCurrentBook() {
        return currentBook;
    }

    public void setFontSize(int newFontSize) {
        fontSize = newFontSize;
    }

    public void setFontColor(int newColor) {
        fontColor = newColor;
    }

    public void setBackgroundColor(int newColor) {
        backgroundColor = newColor;
    }

    public void setAutoLink(boolean newAutoLink) {
        autoLink = newAutoLink;
    }
    public void setCurrentBook(String newBook) {
        currentBook = newBook;
    }

    public void loadConfig() {
        fontSize = configSp.getInt("font_size",fontSize);
        fontColor = configSp.getInt("font_color",fontColor);
        backgroundColor = configSp.getInt("background_color",backgroundColor);
        autoLink = configSp.getBoolean("auto_link",autoLink);
        currentBook = configSp.getString("current_book",currentBook);
    }

    public void saveConfig() {
        SharedPreferences.Editor editor = configSp.edit();
        editor.putInt("font_size",fontSize);
        editor.putInt("font_color",fontColor);
        editor.putInt("background_color",backgroundColor);
        editor.putBoolean("auto_link",autoLink);
        editor.putString("current_book",currentBook);
        editor.apply();
    }
}
