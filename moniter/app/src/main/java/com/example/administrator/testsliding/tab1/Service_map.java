package com.example.administrator.testsliding.tab1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.map.Map_offline;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.List;

public class Service_map extends FragmentActivity {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TabAdapter_chart mAdapter;

    private List<Fragment> list;
    private Map_Heat_Setting mMap_Base_setting;
    private Map_Route_Setting mMap_Route_Setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_map);
        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_map);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_map.this.finish();
            }

            @Override
            public void rightclick() {
                Intent intent = new Intent();
                //Intent请求的是OtherActivity.class
                intent.setClass(Service_map.this, Map_offline.class);
                startActivity(intent);
            }
        });
        fragmentPagerSetting();
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.map_viewpager);
        tabLayout= (TabLayout)findViewById(R.id.tab_map);
        mAdapter = new TabAdapter_chart(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器

    }

    private void fragmentPagerSetting() {

        mMap_Base_setting = new Map_Heat_Setting();
        mMap_Route_Setting = new Map_Route_Setting();

        list = new ArrayList<Fragment>();

        list.add(mMap_Base_setting);
        list.add(mMap_Route_Setting);


    }

    class TabAdapter_chart extends FragmentPagerAdapter {

        public String[] TITLES = new String[]
                {"态势图", "路径图"};

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

