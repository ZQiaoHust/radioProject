package com.hust.radiofeeler.tab1;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.hust.radiofeeler.R;
import com.gc.flashview.FlashView;
import com.gc.flashview.constants.EffectConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/21.
 */
public class Service_fragment extends Fragment implements
        AdapterView.OnItemClickListener {

    private List<String> imageUrls;


    private GridView  gridView;

    private List<Map<String,Object>> dataList;

    private int[] icon={R.drawable.ditu2,R.drawable.location,
            R.drawable.gongzuomoshi2,R.drawable.pinpu2,R.drawable.yingyong,R.drawable.iq,
            R.drawable.jiaohu,R.drawable.xiuzhengbiao};
    private String[] iconName={"电磁辐射态势图","异常频点定位","台站状态","无线电规划","终端属性","历史IQ波",
    "远程监控","终端修正表"};
    private SimpleAdapter adapter;


    public  void onResume(){
        super.onResume();

    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        InitFlash();
        gridView= (GridView) getActivity().findViewById(R.id.gridView);
        dataList=new ArrayList<Map<String,Object>>();

        adapter=new SimpleAdapter(getActivity(),getData(),R.layout.griditem_fragment1,
                new String[]{"image","text"},new int[]{R.id.image,R.id.text});

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_fragment,container,false);



    }




    private void InitFlash(){
        FlashView flash= (FlashView) getActivity().findViewById(R.id.flash);
        imageUrls=new ArrayList<>();
        //imageUrls.add("http://www.qipaox.com/tupian/200810/20081051924582.jpg");
        imageUrls.add("http://www.bz55.com/uploads1/allimg/120312/1_120312100435_8.jpg");
        imageUrls.add("http://img3.iqilu.com/data/attachment/forum/201308/21/192654ai88zf6zaa60zddo.jpg");
        imageUrls.add("http://img2.pconline.com.cn/pconline/0706/19/1038447_34.jpg");
        // imageUrls.add("http://www.kole8.com/desktop/desk_file-11/2/2/2012/11/2012113013552959.jpg");
        // imageUrls.add("http://www.237.cc/uploads/pcline/712_0_1680x1050.jpg");
        flash.setImageUris(imageUrls);
        flash.setEffect(EffectConstants.DEFAULT_EFFECT);//更改图片切换的动画效果
    }

    private List<Map<String,Object>> getData(){

        for(int i=0;i<icon.length;i++) {

            Map<String, Object>map = new HashMap<String, Object>();
            map.put("image",icon[i]);
            map.put("text",iconName[i]);
            dataList.add(map);
        }

        return dataList;

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch(position){

            case 0:
                Intent intent0=new Intent(getActivity(),Service_map.class);
                startActivity(intent0);
                break;
            case 1:
                Intent intent1=new Intent(getActivity(),Service_abnormal.class);
                startActivity(intent1);
                break;
            case 2:
                Intent intent2=new Intent(getActivity(),Service_stations.class);
                startActivity(intent2);
                break;

            case 3:
                Intent intent4=new Intent(getActivity(),Service_radio.class);
                startActivity(intent4);
                break;
            case 4:
                Intent intent3=new Intent(getActivity(),Service_Terminal.class);
                startActivity(intent3);
                break;

            case 5:
                Intent intent5=new Intent(getActivity(),Service_IQ.class);
                startActivity(intent5);
                break;
            case 6:
                Intent intent6=new Intent(getActivity(),Service_Interact.class);
                startActivity(intent6);
                break;
            case 7:
                Intent intent7=new Intent(getActivity(),Service_ModifyChart.class);
                startActivity(intent7);
                break;



        }
    }
}
