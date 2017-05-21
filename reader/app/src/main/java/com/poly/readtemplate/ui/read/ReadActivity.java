package com.poly.readtemplate.ui.read;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.poly.readtemplate.R;
import com.poly.readtemplate.base.BaseActivity;
import com.poly.readtemplate.widget.MyViewPager;

/**
 * 描述：
 * 作者： poly
 * 创建时间：2017/5/21
 */

public class ReadActivity extends BaseActivity implements MyViewPager.OnTurnListener {
    public final static String CONTENT = "CONTENT";
    private MyViewPager mReaderPager;
    private TextView mPreReaderView;
    private String mContent;
    private TextView mPagerIndex;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getIntent().getStringExtra(CONTENT);
        setContentView(R.layout.activity_read);
        mReaderPager = findId(R.id.reader_view);
        mPreReaderView = findId(R.id.reader_preview);
        mPagerIndex = findId(R.id.pager_index);
        mPreReaderView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mPreReaderView.setText(mContent);
        mReaderPager.setVisibility(View.GONE);
        mReaderPager.setOnTurnListener(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] pages = getPage(mPreReaderView);
                ContentAdapter adapter = new ContentAdapter(pages,mContent);
                mReaderPager.setAdapter(adapter);
                mPreReaderView.setVisibility(View.GONE);
                mReaderPager.setVisibility(View.VISIBLE);
                mPagerIndex.setText(1+"/"+mReaderPager.getAdapter().getCount());
            }
        },500);
        mReaderPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mPagerIndex.setText(position+1+"/"+mReaderPager.getAdapter().getCount());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void start(Context context,String content){
        Intent intent = new Intent(context,ReadActivity.class);
        intent.putExtra(CONTENT,content);
        context.startActivity(intent);
    }

    public int[] getPage( TextView textView){
        int count=textView.getLineCount();
        textView.setText(mContent);
        int pCount=getPageLineCount(textView);
        int pageNum=count/pCount+1;
        Log.d("hmm","pCount="+pCount+"------"+count);
        int page[]=new int[pageNum];
        for(int i=0;i<pageNum-1;i++){
            page[i]=textView.getLayout().getLineEnd((i+1)*pCount-1);
        }
        return page;
    }

    private int getPageLineCount(TextView view){
      /*
      * The first row's height is different from other row.
       */
        int h=view.getBottom()-view.getTop()-view.getPaddingTop();
        int firstH=getLineHeight(0,view);
        int otherH=getLineHeight(1,view);
        return otherH == 0?1:(h-firstH)/otherH + 1 ;
    }
    private int getLineHeight(int line,TextView view){
        Rect rect=new Rect();
        view.getLineBounds(line,rect);
        return rect.bottom-rect.top;
    }

    @Override
    public void nextPager() {
        Toast.makeText(this,"下一章",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void prePager() {
        Toast.makeText(this,"上一章",Toast.LENGTH_SHORT).show();
    }
}
