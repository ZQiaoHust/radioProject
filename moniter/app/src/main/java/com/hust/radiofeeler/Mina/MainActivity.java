package com.hust.radiofeeler.Mina;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hust.radiofeeler.R;
import com.hust.radiofeeler.SlideMenu.ConnectPCB;
import com.hust.radiofeeler.SlideMenu.FinalStationState;
import com.hust.radiofeeler.SlideMenu.SetPara;
import com.hust.radiofeeler.SlideMenu.UploadData;
import com.hust.radiofeeler.SlideMenu.Work_model;
import com.hust.radiofeeler.tab1.Service_fragment;
import com.hust.radiofeeler.tab2.Chart_fragment;
import com.hust.radiofeeler.tab3.Share_fragment;
import com.hust.radiofeeler.tab4.Me_fragment;
import com.hust.radiofeeler.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{
    private Intent mIntent;
    //private ViewPager viewPager;
    private NoScrollViewPager viewPager;
    private List<Fragment> list;

    private Service_fragment servicefragment;
    private Chart_fragment chartfragment;
    private Share_fragment sharefragment;
    private Me_fragment mefragment;
    private TextView tv_service, tv_chart, tv_share, tv_me;
    private ImageView im_service, im_chart, im_share, im_me;
    private LinearLayout lilay_service, lilay_chart, lilay_share, lilay_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
//        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        Intent intent = new Intent(MainActivity.this, ToServerMinaService.class);
//        startService(intent);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        initSetting();
        fragmentPagerSetting();
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.zengyimenxian:
                mIntent = new Intent(MainActivity.this, SetPara.class);
                startActivity(mIntent);
                break;

            case R.id.gongzuomoshi:
                mIntent = new Intent(MainActivity.this, Work_model.class);
                startActivity(mIntent);
                break;

            case R.id.lianjie:
                mIntent = new Intent(MainActivity.this, ConnectPCB.class);
                startActivity(mIntent);
                break;

            case R.id.taizhanzhuangtai:
                mIntent = new Intent(MainActivity.this, FinalStationState.class);
                startActivity(mIntent);
                break;

            case R.id.shujushangchuan:
                mIntent = new Intent(MainActivity.this, UploadData.class);
                startActivity(mIntent);
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initSetting() {
        im_service = (ImageView) findViewById(R.id.im_service);
        im_chart = (ImageView) findViewById(R.id.im_chart);
        im_share = (ImageView) findViewById(R.id.im_share);
        im_me = (ImageView) findViewById(R.id.im_me);

        tv_service = (TextView) findViewById(R.id.tv_service);
        tv_chart = (TextView) findViewById(R.id.tv_chart);
        tv_share = (TextView) findViewById(R.id.tv_share);
        tv_me = (TextView) findViewById(R.id.tv_me);


        lilay_service = (LinearLayout) findViewById(R.id.lilay_service);
        lilay_chart = (LinearLayout) findViewById(R.id.lilay_chart);
        lilay_share = (LinearLayout) findViewById(R.id.lilay_share);
        lilay_me = (LinearLayout) findViewById(R.id.lilay_me);

        lilay_service.setOnClickListener(this);
        lilay_chart.setOnClickListener(this);
        lilay_share.setOnClickListener(this);
        lilay_me.setOnClickListener(this);

        im_service.setImageResource(R.drawable.shouye2);
        tv_service.setTextColor(getResources().getColor(R.color.green));

       viewPager = (NoScrollViewPager) findViewById(R.id.viewpager);

        viewPager.setNoScroll(true);//自定义是否可以滑动

    }

    public void fragmentPagerSetting() {
        servicefragment = new Service_fragment();
        chartfragment = new Chart_fragment();
        sharefragment = new Share_fragment();
        mefragment = new Me_fragment();

        list = new ArrayList<Fragment>();

        list.add(servicefragment);
        list.add(chartfragment);
        list.add(sharefragment);
        list.add(mefragment);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        viewPager.setOffscreenPageLimit(4);
    }


    private class MyPagerAdapter extends FragmentStatePagerAdapter {


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


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //super.destroyItem(container, position, object);
        }
    }

    public void clearIconState() {

        im_service.setImageResource(R.drawable.shouye1);
        im_chart.setImageResource(R.drawable.faxian1);
        im_share.setImageResource(R.drawable.fenxiang1);
        im_me.setImageResource(R.drawable.wo1);

        tv_service.setTextColor(getResources().getColor(android.R.color.black));
        tv_chart.setTextColor(getResources().getColor(android.R.color.black));
        tv_share.setTextColor(getResources().getColor(android.R.color.black));
        tv_me.setTextColor(getResources().getColor(android.R.color.black));
    }

    public void setIconState(int index) {
        // TODO Auto-generated method stub

        switch (index) {
            case 0:
                im_service.setImageResource(R.drawable.shouye2);
                tv_service.setTextColor(getResources().getColor(R.color.green));
                break;
            case 1:
                im_chart.setImageResource(R.drawable.faxian2);
                tv_chart.setTextColor(getResources().getColor(R.color.green));
                break;

            case 2:
                im_share.setImageResource(R.drawable.fenxiang2);
                tv_share.setTextColor(getResources().getColor(R.color.green));
                break;
            case 3:
                im_me.setImageResource(R.drawable.wo2);
                tv_me.setTextColor(getResources().getColor(R.color.green));
                break;
        }
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
        //viewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.lilay_service:
                viewPager.setCurrentItem(0, true);
                break;
            case R.id.lilay_chart:
                viewPager.setCurrentItem(1, true);
                break;

            case R.id.lilay_share:
                viewPager.setCurrentItem(2, true);
                break;
            case R.id.lilay_me:
                viewPager.setCurrentItem(3, true);
                break;
        }
    }
}
