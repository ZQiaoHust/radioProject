package com.hust.radiofeeler.tab1;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.hust.radiofeeler.R;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/16.
 */
public class Service_ModifyChart  extends FragmentActivity {
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private TabAdapter_chart mAdapter;

    private List<Fragment> list;
    private Service_InGainModify mService_InGainModify;
    private Service_AntennaModify mService_AntennaModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_stations);
        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_servicestation);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Service_ModifyChart.this.finish();
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

        mService_InGainModify = new Service_InGainModify();
        mService_AntennaModify= new Service_AntennaModify();

        list = new ArrayList<Fragment>();

        list.add(mService_InGainModify);
        list.add(mService_AntennaModify);


    }

    class TabAdapter_chart extends FragmentPagerAdapter {

        public String[] TITLES = new String[]
                {"接收通道增益", "接收天线增益"};

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
