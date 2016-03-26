package com.example.administrator.testsliding.SlideMenu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.testsliding.R;

import java.util.ArrayList;
import java.util.List;

public class Work_model extends FragmentActivity implements View.OnClickListener {

    private ViewPager viewPager_work_model;

    private List<Fragment> list;
    private Fragment_work_model1 fragment_work_model1;
    private Fragment_work_model2 fragment_work_model2;
    private Fragment_work_model3 fragment_work_model3;


    private TextView sweep_model, single_sweep_model,compress_model;
    private LinearLayout sweep_freq_linearlayout, single_sweep_freq_linearlayout,compress_linearlayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.work_model);

        initSetting();
        fragmentPagerSetting();
        //back
        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Work_model.this.finish();
            }
        });


        viewPager_work_model.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int index) {
                // TODO Auto-generated method stub
                clearIconState();
                setIconState(index);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });





    }


    private void initSetting() {

        /*绑定视图*/
        sweep_model = (TextView) findViewById(R.id.sweep_freq);
        single_sweep_model = (TextView) findViewById(R.id.single_sweep_freq);
        compress_model= (TextView) findViewById(R.id.compress_freq);


        sweep_freq_linearlayout = (LinearLayout) findViewById(R.id.sweep_freq_linearlayout);
        single_sweep_freq_linearlayout = (LinearLayout) findViewById(R.id.single_sweep_freq_linearlayout);
        compress_linearlayout= (LinearLayout) findViewById(R.id.compress_freq_linearlayout);

        sweep_freq_linearlayout.setOnClickListener(this);
        single_sweep_freq_linearlayout.setOnClickListener(this);
        compress_linearlayout.setOnClickListener(this);

        sweep_model.setTextColor(getResources().getColor(R.color.green));


        viewPager_work_model = (ViewPager) findViewById(R.id.viewpager_work_model);

    }
    /*viewpager加载适配器，装载两个fragment*/
    public void fragmentPagerSetting() {
        fragment_work_model1 = new Fragment_work_model1();
        fragment_work_model2 = new Fragment_work_model2();
        fragment_work_model3 = new Fragment_work_model3();


        list = new ArrayList<Fragment>();

        list.add(fragment_work_model1);
        list.add(fragment_work_model2);
        list.add(fragment_work_model3);


        viewPager_work_model.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
    }



    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }


        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }


    }

//    文字变色
    public void clearIconState() {


        sweep_model.setTextColor(getResources().getColor(android.R.color.black));
        single_sweep_model.setTextColor(getResources().getColor(android.R.color.black));
        compress_model.setTextColor(getResources().getColor(android.R.color.black));

    }

    public void setIconState(int index) {
        // TODO Auto-generated method stub

        switch (index) {
            case 0:
                sweep_model.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1:
                single_sweep_model.setTextColor(getResources().getColor(R.color.green));
                break;
            case 2:
                compress_model.setTextColor(getResources().getColor(R.color.green));
                break;

        }
    }

    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        //viewPager_work_model.setCurrentItem(0);
    }
//设置点击的页面显示
    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            case R.id.sweep_freq_linearlayout:
                viewPager_work_model.setCurrentItem(0, true);
                break;
            case R.id.single_sweep_freq_linearlayout:
                viewPager_work_model.setCurrentItem(1, true);
                break;
            case R.id.compress_freq_linearlayout:
                viewPager_work_model.setCurrentItem(2, true);
                break;


        }
    }
}
