package com.example.administrator.testsliding.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.administrator.testsliding.R;


/**
 * Created by Administrator on 2015/10/26.
 */



public class MyTopBar extends RelativeLayout {

    private Button leftButton,rightButton;
    private TextView tv_title;

    private int titleText_Color;
    private float titleTextSize;
    private String titleText;

    private int leftTextColor;
    private Drawable leftBackground;
    private String leftText;

    private int rightTextColor;
    private Drawable rightBackground;
    private String rightText;

    private LayoutParams leftParams, rightParams, tvParams;
    private TopBarClickListener listener;


    // 点击事件监听器接口
    public interface TopBarClickListener {

        public void leftclick();

        public void rightclick();
    }



    // 设置监听器
    public void setOnTopBarClickListener(TopBarClickListener listener) {
        this.listener = listener;
    }


    public MyTopBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.TopBar);

        titleText_Color = ta.getColor(R.styleable.TopBar_titleText_Color, 0);
        titleTextSize = ta.getDimension(R.styleable.TopBar_titleTextSize, 0);
        titleText = ta.getString(R.styleable.TopBar_titleText);

        leftTextColor = ta.getColor(R.styleable.TopBar_leftTextColor, 0);
        leftBackground =ta.getDrawable(R.styleable.TopBar_leftBackground);
        leftText = ta.getString(R.styleable.TopBar_leftText);

        rightTextColor = ta.getColor(R.styleable.TopBar_rightTextColor, 0);
        rightBackground = ta.getDrawable(R.styleable.TopBar_rightBackground);
        rightText = ta.getString(R.styleable.TopBar_rightText);

        ta.recycle();

        leftButton = new Button(context);
        rightButton = new Button(context);
        tv_title=new TextView(context);

        tv_title.setText(titleText);
        tv_title.setTextColor(titleText_Color);
        tv_title.setTextSize(titleTextSize);
        tv_title.setGravity(Gravity.CENTER);

        leftButton.setTextColor(leftTextColor);
        leftButton.setBackground(leftBackground);
        leftButton.setText(leftText);


        rightButton.setTextColor(rightTextColor);
        rightButton.setBackground(rightBackground);
        rightButton.setText(rightText);

        setBackgroundColor(0XFF58ACED);

        leftParams=new LayoutParams(100, 100);
        leftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);

        addView(leftButton, leftParams);

        rightParams=new LayoutParams(100, 100);
        rightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);

        addView(rightButton, rightParams);


        tvParams=new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.MATCH_PARENT);
        tvParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);

        addView(tv_title, tvParams);


        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.leftclick();
            }
        });

        rightButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v){

                listener.rightclick();
            }
        });
    }
    public void setLeftIsVisible(boolean visible) {
        if (visible) {
            leftButton.setVisibility(View.VISIBLE);
        } else {
            leftButton.setVisibility(View.GONE);
        }
    }
    public void setRightIsVisible(boolean visible) {
        if (visible) {
            rightButton.setVisibility(View.VISIBLE);
        } else {
            rightButton.setVisibility(View.GONE);
        }
    }
}

