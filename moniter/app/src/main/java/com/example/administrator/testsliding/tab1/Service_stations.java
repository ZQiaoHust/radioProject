package com.example.administrator.testsliding.tab1;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/26.
 */
public class Service_stations extends FragmentActivity {

    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TabAdapter_chart mAdapter;

    private List<Fragment> list;
    private Stations_register mStations_register;
    private Stations_current mStations_current;
    private Station_All mstation_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_stations);
        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_servicestation);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_stations.this.finish();
            }

            @Override
            public void rightclick() {

            }
        });
        fragmentPagerSetting();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.stations_viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tab_station);
        mAdapter = new TabAdapter_chart(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }

    private void fragmentPagerSetting() {

        mstation_all=new Station_All();
        mStations_register = new Stations_register();
        mStations_current = new Stations_current();

        list = new ArrayList<Fragment>();
        list.add(mstation_all);
        list.add(mStations_register);
        list.add(mStations_current);


    }

    class TabAdapter_chart extends FragmentPagerAdapter {

        public String[] TITLES = new String[]
                {"全部台站属性","登记台站属性", "登记台站当前属性"};

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
