package com.hust.radiofeeler.tab1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hust.radiofeeler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/4.
 */
public class Service_Interact extends FragmentActivity {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TabAdapter_chart mAdapter;

    private List<Fragment> list;
    private Interact_work1 mInteract_work1;
    private Interact_work2 mInteract_work2;
    private Interact_work3 mInteract_work3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_interact);
        fragmentPagerSetting();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager)findViewById(R.id.viewpager_interact);
        tabLayout= (TabLayout)findViewById(R.id.tab_interact);
        mAdapter = new TabAdapter_chart(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }


    /*viewpager加载适配器，装载两个fragment*/
    private void fragmentPagerSetting() {
        mInteract_work1=new Interact_work1();
        mInteract_work2=new Interact_work2();
        mInteract_work3=new Interact_work3();
        list = new ArrayList<Fragment>();

        list.add(mInteract_work1);
        list.add(mInteract_work2);
        list.add(mInteract_work3);

    }

    class TabAdapter_chart extends FragmentPagerAdapter {

        public  String[] TITLES = new String[]
                {"扫频模式", "定频模式", "压制模式"};

        public TabAdapter_chart(FragmentManager fm) {
            super(fm);
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

        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

    }
}
