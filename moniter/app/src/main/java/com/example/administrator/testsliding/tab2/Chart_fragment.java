package com.example.administrator.testsliding.tab2;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.view.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/24.
 */
public class Chart_fragment extends Fragment {
    private NoScrollViewPager mViewPager;
    private TabLayout tabLayout;
    private TabAdapter_chart mAdapter;

    private List<Fragment> list;
//    private Chart_spectrum mChart_spectrum;
//    private Chart_waterfall mChart_waterfall;
//
//    private Chart_Abnormal mChart_Abnormal;
    private Button btn_spectrum,btn_water,btn_abnormal;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     //   getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
        //getActivity().getActionBar().setNavigationMode(ActionBar.DISPLAY_HOME_AS_UP);
        return inflater.inflate(R.layout.chart_fragment, container, false);

    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mViewPager.setCurrentItem(0);
//
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
//        ((AppCompatActivity)(getActivity())).setSupportActionBar(toolbar);
      //  fragmentPagerSetting();
      //  initView();

      /*  mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(position){
                    case 0:
                        Intent intent=new Intent(getActivity(),Chart_spectrum.this);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(getActivity(),Chart_waterfall.this);
                        startActivity(intent1);
//                        Constants.Queue_DrawRealtimewaterfall.clear();//清掉旧数据
//                        Constants.IsDrawWaterfall=true;
                        break;
                    case 2:
//                        Constants.Queue_AbnormalFreq_List.clear();
//                        Constants.IsshowAbnormalList=true;
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        btn_spectrum= (Button) getActivity().findViewById(R.id.btn_spectrum);
        btn_water= (Button) getActivity().findViewById(R.id.btn_water);
        btn_abnormal= (Button) getActivity().findViewById(R.id.btn_abnormal);

        btn_spectrum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Chart_spectrum.class);
                startActivity(intent);
            }
        });
        btn_water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Chart_waterfall.class);
                startActivity(intent);
            }
        });
        btn_abnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),Chart_Abnormal.class);
                startActivity(intent);
            }
        });

    }

    private void initView() {
//        mViewPager =  (NoScrollViewPager)getActivity().findViewById(R.id.id_viewpager);
//        tabLayout= (TabLayout) getActivity().findViewById(R.id.tabs);
//        mAdapter = new TabAdapter_chart(getChildFragmentManager());
//        mViewPager.setAdapter(mAdapter);
//        tabLayout.setupWithViewPager(mViewPager);//将TabLayout和ViewPager关联起来。
//        tabLayout.setTabsFromPagerAdapter(mAdapter);//给Tabs设置适配器
//        mViewPager.setNoScroll(true);//自定义是否可以滑动

    }


    /*viewpager加载适配器，装载两个fragment*/
//   private void fragmentPagerSetting() {
//
//       mChart_spectrum=new Chart_spectrum();
//       mChart_waterfall=new Chart_waterfall();
//
//       mChart_Abnormal=new Chart_Abnormal();
//
//
//       list = new ArrayList<Fragment>();
//
//       list.add(mChart_spectrum);
//       list.add(mChart_waterfall);
//
//       list.add(mChart_Abnormal);
//
//    }

     class TabAdapter_chart extends FragmentPagerAdapter {

        public  String[] TITLES = new String[]
                {"频谱图", "瀑布图", "异常表"};

         public TabAdapter_chart(android.support.v4.app.FragmentManager fm) {
             super(fm);
         }

//        public TabAdapter_chart(FragmentManager fm) {
//            super(fm);
//        }

         @Override
         public android.support.v4.app.Fragment getItem(int position) {
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