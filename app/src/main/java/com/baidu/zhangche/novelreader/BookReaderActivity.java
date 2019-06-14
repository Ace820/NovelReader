package com.baidu.zhangche.novelreader;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class BookReaderActivity extends AppCompatActivity {
    Context mContext;
    private TextView textTitle, textReader;
    private View controlBar;
    private Button chapterPrevious, chapterNext;
    private TextView chapterProgress;
    private Button chapterList, fontSizeUp, fontSizeDown, readerTheme, extraSettings;
    protected boolean isControlBarVisble = false;
    Handler handler = new Handler();
    public BookReaderActivity(Context context) {
        mContext = context;
    }
    public BookReaderActivity() {
        mContext = BookReaderActivity.this;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_book_reader);

        textTitle = findViewById(R.id.text_title_area);
        textReader = findViewById(R.id.text_read_area);
        controlBar = findViewById(R.id.control_bar);
        chapterPrevious = findViewById(R.id.chapter_previous);
        chapterNext = findViewById(R.id.chapter_next);
        chapterProgress = findViewById(R.id.chapter_progress);
        chapterList = findViewById(R.id.chapter_list);
        fontSizeDown = findViewById(R.id.font_size_down);
        fontSizeUp = findViewById(R.id.font_size_up);
        readerTheme = findViewById(R.id.theme);
        extraSettings = findViewById(R.id.settings);

        textReader.setOnClickListener(new readerClickListener(textReader));
        chapterPrevious.setOnClickListener(new readerClickListener(chapterPrevious));
        chapterNext.setOnClickListener(new readerClickListener(chapterNext));
        chapterList.setOnClickListener(new readerClickListener(chapterList));
        fontSizeDown.setOnClickListener(new readerClickListener(fontSizeDown));
        fontSizeUp.setOnClickListener(new readerClickListener(fontSizeUp));
        readerTheme.setOnClickListener(new readerClickListener(readerTheme));
        extraSettings.setOnClickListener(new readerClickListener(extraSettings));


    }


    class readerClickListener implements View.OnClickListener {
        View view;
        public readerClickListener(View v) {
           view = v;
        }

        @Override
        public void onClick(View v) {
            switch (view.getId()) {
                case R.id.text_read_area :
                    if (isControlBarVisble) {
                        textReader.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                        controlBar.setVisibility(View.GONE);
                        isControlBarVisble = false;
                        Log.d(mContext.toString(), "gone");
                    }else {
                        Log.d(mContext.toString(), "visible");
                        textReader.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
                        controlBar.setVisibility(View.VISIBLE);
                        isControlBarVisble = true;
                    }
                    break;
                case R.id.chapter_previous :
                    break;
                case R.id.chapter_next :
                    break;
                case R.id.chapter_list :
                    break;
                case R.id.font_size_down :
                    break;
                case R.id.font_size_up :
                    break;
                case R.id.theme :
                    break;
                case R.id.settings :
                    break;
            }
        }
    }
    class readerTouchListener implements View.OnTouchListener {
        float xDown, yDown, xUp;
        boolean isLongClickModule = false;
        boolean isLongClicking = false;
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            //当按下时处理
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                xDown = motionEvent.getX();
                yDown = motionEvent.getY();
                Log.v("OnTouchListener", "Down");

            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {// 松开处理
                //获取松开时的x坐标
                if (isLongClickModule) {
                    isLongClickModule = false;
                    isLongClicking = false;
                }
                xUp = motionEvent.getX();

                Log.v("OnTouchListener", "Up");
                //按下和松开绝对值差当大于20时滑动，否则不显示
                if ((xUp - xDown) > 20) {
                    //添加要处理的内容
                } else if ((xUp - xDown) < -20) {
                    //添加要处理的内容
                } else if (0 == (xDown - xUp)) {
                    int viewWidth = view.getWidth();
                    if (xDown < viewWidth / 3) {
                        //靠左点击
                    } else if (xDown > viewWidth / 3 && xDown < viewWidth * 2 / 3) {
                        //中间点击

                    } else {
                        //靠右点击
                    }
                    /**
                     * not scroll
                     */
                    //showNavigation();
                }
            } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                //当滑动时背景为选中状态 //检测是否长按,在非长按时检测
                if (!isLongClickModule) {
                    isLongClickModule = isLongPressed(xDown, yDown, motionEvent.getX(),
                            motionEvent.getY(), motionEvent.getDownTime(), motionEvent.getEventTime(), 300);
                }
                if (isLongClickModule && !isLongClicking) {
                    //处理长按事件
                    isLongClicking = true;
                }
            } else {
                //其他模式
            }
            return false;
        }

        /* 判断是否有长按动作发生
         * @param lastX 按下时X坐标
         * @param lastY 按下时Y坐标
         * @param thisX 移动时X坐标
         * @param thisY 移动时Y坐标
         * @param lastDownTime 按下时间
         * @param thisEventTime 移动时间
         * @param longPressTime 判断长按时间的阀值
         */
        private boolean isLongPressed(float lastX, float lastY,
                                      float thisX, float thisY,
                                      long lastDownTime, long thisEventTime,
                                      long longPressTime) {
            float offsetX = Math.abs(thisX - lastX);
            float offsetY = Math.abs(thisY - lastY);
            long intervalTime = thisEventTime - lastDownTime;
            if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
                return true;
            }
            return false;
        }

    }
}
