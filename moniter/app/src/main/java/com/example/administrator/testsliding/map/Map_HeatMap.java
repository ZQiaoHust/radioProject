package com.example.administrator.testsliding.map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.HeatMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.administrator.testsliding.tab1.Map_Heat_Result;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.MapRadioPointInfo;
import com.example.administrator.testsliding.bean2server.MapRadioResult;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

/**
 * 路径图
 * 
 */

public class Map_HeatMap extends Activity {

    // 地图相关
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    //private Marker mMarkerA;
    //BitmapDescriptor stationMarker = null;
    private SDKReceiver mReceiver;
    private int count = 0;
    private HeatMap heatmap;
    private MapRadioResult map = new MapRadioResult();

    private int Nx,Ny;
    private double Ratio;
    //网格化点数组
    public static List<PointInfo> poinList = new ArrayList<PointInfo>();


    //模拟收到的数据
    public static List<PointInfo> AbnormalPointList = new ArrayList<PointInfo>();
    //线程终止位
    public volatile boolean exit = false;





    //真正的数据接口
    public static ArrayList<MapRadioPointInfo> HeatMapPointList = new ArrayList<MapRadioPointInfo>();
//    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            MapRadioResult map=intent.getParcelableExtra("map_radio");
//            // 如果接受到空消息时过滤掉
//            if (null == map) {
//                return;
//            }
//            // 将消息展现出来。
//            if (map != null) {
//                HeatMapPointList = map.getMapRadioPointInfoList();
//                Nx = map.getNx();
//                Ny = map.getNy();
//                Ratio = map.getDieta();
//            }
//
//
//
//        }
//    };


    //检验Key是否验证通过
    public class SDKReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            String s = intent.getAction();//获得Intent的MIME type
            if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Toast.makeText(getApplicationContext(), "key 验证出错! 请在 AndroidManifest.xml 文件中检查 key 设置",
                        Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK)) {
                Toast.makeText(getApplicationContext(), "key 验证成功! 功能可以正常使用",
                        Toast.LENGTH_SHORT).show();
            } else if (s.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Toast.makeText(getApplicationContext(), "网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_heatmap);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bMapView_radio);
        mBaiduMap = mMapView.getMap();
        // UI初始化





        //去掉放大缩小的标识
        count = mMapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mMapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        // 注册 SDK 广播监听者
        IntentFilter iFilter = new IntentFilter();
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_OK);
        iFilter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        iFilter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        mReceiver = new SDKReceiver();
        registerReceiver(mReceiver, iFilter);


         /*intent方法*/
        map = getIntent().getParcelableExtra(Map_Heat_Result.PAR_KEY);
        if (null == map) {
            return;
        }

        if (map != null) {
            // 将消息展现出来。
            Log.d("mapdataget","size is "+String.valueOf(map.getMapRadioPointInfoList().size()));
            HeatMapPointList = map.getMapRadioPointInfoList();
            Nx = map.getNx();
            Ny = map.getNy();
            Ratio = map.getDieta();
        }
        Log.d("mapdataget", "size is " + String.valueOf(HeatMapPointList.size()));
        //Log.e("mapdataget", "EqualPower is " + String.valueOf(map.getMapRadioPointInfoList().get(0).getEqualPower()));


        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                LatLng centerLat;
                if ((msg.what >= 0) && (msg.what <= 9) && (msg.what <  HeatMapPointList.size())) {
                    Log.d("pocess", "now is inside handler");
                    mMapView.getMap().clear();
                    poinList.clear();
                    //设置中心点
                    centerLat = new LatLng( HeatMapPointList.get(msg.what).getLatitude(),HeatMapPointList.get(msg.what).getLongitude());
                    MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(centerLat);
                    mBaiduMap.setMapStatus(u1);
                    MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15.0f);
                    mBaiduMap.setMapStatus(msu);
                    //添加标记
                    addMarker(centerLat);
                    ComputeMapNetPoint(centerLat.longitude, centerLat.latitude, HeatMapPointList.get(msg.what).getHeight(), HeatMapPointList.get(msg.what).getEqualPower(),
                            HeatMapPointList.get(msg.what).getrPara(),Ratio, Nx,Ny);
                    //addRadioMap(poinList, 2.66);
                } else {
                    Log.d("pocess", "now is here 12");
                    exit = true;
                }
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                        for ( int i = 0; i < HeatMapPointList.size(); i++) {
                            Message message = new Message();
                            Log.e("pocess","now is here 11");
                            message.what =i ;
                            Log.e("pocess","size "+String.valueOf(HeatMapPointList.size()));
                            Log.e("pocess ","now is "+String.valueOf(i)+"message");
                            handler.sendMessage(message);//发送消息
                            Log.e("pocess", "now is here 12");
                            Thread.sleep(10000);//线程暂停3秒，单位毫秒

                             }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("refreshthread error...");
                }
            }
        }.start();

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ConstantValues.RMAPRADIO);
//        filter.addCategory(Intent.CATEGORY_DEFAULT);
//        registerReceiver(contentRecevier, filter);
    }



    /**
     * 添加点、线、多边形、圆、文字
     */
    private void addMarker( LatLng position) {
        BitmapDescriptor stationMarker1 = BitmapDescriptorFactory
                .fromResource(R.drawable.marker1);
        BitmapDescriptor stationMarker2 = BitmapDescriptorFactory
                .fromResource(R.drawable.marke2);

        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
        giflist.add(stationMarker1);
        giflist.add(stationMarker2);
        MarkerOptions ooD = new MarkerOptions().position(position).icons(giflist)
                .zIndex(0).period(10);
        ooD.animateType(MarkerOptions.MarkerAnimateType.grow);
        mBaiduMap.addOverlay(ooD);
    }

       private void addRadioMap(List<PointInfo> infos,double mRatio) {

           LatLng LeftOnPoint;
           LatLng RightOnPoint;
           LatLng LeftDownPoint;
           LatLng RightDownPoint;
           //MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
           //mBaiduMap.setMapStatus(msu);

           int color;
           double spectrum = 0;
           List<LatLng> pts = new ArrayList<LatLng>();
           for (PointInfo info : infos) {
               // 功率谱
               spectrum = info.getPower();
               // spectrum_16 = Integer.toHexString(spectrum);
               color = getResponseColor(spectrum);
//               OverlayOptions ooCircle = new CircleOptions().fillColor(color)
//                       .center(latLng).radius(1000);
               // 添加多边形
               LeftOnPoint = new LatLng(info.getLatitude() - mRatio / 120, info.getLongtitude() - mRatio / 120);
               RightOnPoint = new LatLng(info.getLatitude() - mRatio / 120, info.getLongtitude() + mRatio / 120);
               RightDownPoint = new LatLng(info.getLatitude() + mRatio / 120, info.getLongtitude() + mRatio / 120);
               LeftDownPoint = new LatLng(info.getLatitude() + mRatio / 120, info.getLongtitude() - mRatio / 120);

               pts.add(LeftOnPoint);
               pts.add(RightOnPoint);
               pts.add(RightDownPoint);
               pts.add(LeftDownPoint);
               OverlayOptions ooPolygon = new PolygonOptions().points(pts).fillColor(color);
               mBaiduMap.addOverlay(ooPolygon);
               pts.clear();
           }
       }

    public void clearClick() {
        // 清除所有图层
        mMapView.getMap().clear();
    }

    //设置显示的态势图的频率范围

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        unregisterReceiver(mReceiver);
        mReceiver=null;
        super.onDestroy();
    }

    //刷新

    protected int getResponseColor(double spectrum)
    {

        int color =0xffddddff ;
        int power = (int)spectrum;
        int index = 0;
        if((power>= -100)&&(power<0))
        {
            if(Math.abs(power % 10)<5 &&Math.abs(power%10)>=0)
                index = power/10*2+ 1;
            else if(Math.abs(power%10)>=5 &&Math.abs(power%10)<=9)
                index = power/10*2;
        }
        else if((power>= 0)&&(power<=20))
        {
            if(Math.abs(power % 10)<5 &&Math.abs(power%10)>=0)
                index = power/10*2-1;
            else if(Math.abs(power%10)>=5 &&Math.abs(power%10)<=9)
                index = power/10*2;
        }
        else if (power < -100)
            index = -21;
        else if (power >20)
            index = 5;

        switch(index)
        {
            case 5:
                color = 0x88960000;
                break;
            case 4:
                color = 0x88C80006;
                break;
            case 3:
                color = 0x88E10006;
                break;
            case 2:
                color = 0x88F50400;
                break;
            case 1:
                color = 0x88FD2D00;
                break;
            case 0:
                color = 0x88FA5000;
                break;
            case -1:
                color = 0x88FF7302;
                break;
            case -2:
                color = 0x88FFb600;
                break;
            case -3:
                color = 0x88FFD600;
                break;
            case -4:
                color = 0x88FBFF0E;
                break;
            case -5:
                color = 0x88DBFE00;
                break;
            case -6:
                color = 0x88B0FF4E;
                break;
            case -7:
                color = 0x887FFE7D;
                break;
            case -8:
                color = 0x885EFE9F;
                break;
            case -9:
                color = 0x8835FCC8;
                break;
            case -10:
                color = 0x8804FDC5;
                break;
            case -11:
                color = 0x8806E6DC;
                break;
            case -12:
                color = 0x8802CEFF;
                break;
            case -13:
                color = 0x8808AAFF;
                break;
            case -14:
                color = 0x880895FF;
                break;
            case -15:
                color = 0x880376FF;
                break;
            case -16:
                color = 0x88005BFF;
                break;
            case -17:
                color = 0x881717FF;
                break;
            case -18:
                color = 0x880000DB;
                break;
            case -19:
                color = 0x880000BD;
                break;
            case -20:
                color = 0x880000A2;
                break;
            case -21:
                color = 0x88000082;
                break;


        }

        return color;
    }

    //网格化
    private void ComputeMapNetPoint(double Longtitude,double Latitude,double Height,double Power,double index,double Ratio,int Nx,int Ny){
         double PI = 3.14159265358979323846;
         double H0=6378137;//地球半径，单位米
         double mConstant=21600;//计算的常数：360*60

         double mLatitude;//纬度
         double mLongitude;//经度
         double mHeight;//高度
         double mPower;//功率值
         double Rindex;//损耗指数
        // double mRadius; //半径
         double mRatio;//分辨率
         double DetaY,DetaX;
         double d;//距离



        mLatitude=Latitude;
        mLongitude=Longtitude;
        mHeight=Height;
        mPower=Power;
        Rindex=index;
        //mRadius=Radius;
        mRatio=Ratio;

        Log.e("cy yyyypower", String.valueOf(mPower));

        //======计算网格数据==============================================================//
        DetaY=2*PI*(H0+mHeight)*mRatio/mConstant;
        DetaX=DetaY*Math.cos(mLatitude);
        //Nx= (int) (mRadius/DetaX);
        //Ny= (int) (mRadius/DetaY);
        Log.e("detaX", String.valueOf( DetaY));
        Log.e("detay", String.valueOf(DetaX));
        for(int i=-Nx;i<=Nx;i++)
            for(int j=-Ny;j<=Ny;j++){
                PointInfo mpointInfo=new PointInfo();
                mpointInfo.latitude=mLatitude+i*mRatio/60.0;
                mpointInfo.longtitude=mLongitude+j*mRatio/60.0;
                mpointInfo.xheight=mHeight;
                Log.e("cyyyyy", String.valueOf(i));
                Log.e("cyyyyy", String.valueOf(j));
                //计算距离
                double dd=Math.pow(i*DetaY,2)+Math.pow(j*DetaY,2);
               // Log.e("cyyyyy", String.valueOf(dd));
               // d=Math.sqrt(dd);
                //mpointInfo.power=10*Math.log10(mPower/Math.pow(d,Rindex)*1000) ;
                //第二种计算功率的方法
                 mpointInfo.power=mPower-5*Rindex*Math.log10(dd);

                poinList.add(mpointInfo);
            }
        addRadioMap(poinList,Ratio);


    }


}
