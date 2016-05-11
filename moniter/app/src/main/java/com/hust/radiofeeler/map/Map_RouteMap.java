package com.hust.radiofeeler.map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.hust.radiofeeler.tab1.Map_Route_Result;
import com.hust.radiofeeler.tab1.Map_Route_Setting;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.bean2server.MapRadioPointInfo;
import com.hust.radiofeeler.bean2server.MapRouteResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 路径图
 * 
 */
public class Map_RouteMap extends Activity {

    // 地图相关
    private MapView mRoute_MapView;
    private BaiduMap mRoute_BaiduMap;
    // UI相关
    private Button resetBtn;
    private Button clearBtn;

    //添加覆盖物
    private Marker mMarkerA;
    private static final LatLng GEO_WUHAN = new LatLng(30.5170810000, 114.4245410000);
    private Polyline mColorfulPolyline;

    private MapRouteResult map = new MapRouteResult();
    private ArrayList<MapRadioPointInfo> mapRoutePointInfoList = new ArrayList<MapRadioPointInfo>();
    private BitmapDescriptor stationMarker = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);

    private static List<LatLng> points = new ArrayList<LatLng>();
    private static List<Integer> colorValue = new ArrayList<Integer>();

    private static String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/PowerSpectrumFile/";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_geometry);
        // 初始化地图
        mRoute_MapView = (MapView) findViewById(R.id.bMapView2);
        mRoute_BaiduMap = mRoute_MapView.getMap();
        // UI初始化
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);
        mRoute_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(15);
        mRoute_BaiduMap.setMapStatus(msu);

//        OnClickListener clearListener = new OnClickListener() {
//            public void onClick(View v) {
//                clearClick();
//            }
//        };
//
//        clearBtn.setOnClickListener(clearListener);
//        resetBtn.setOnClickListener(restListener);

        // 界面加载时添加绘制图层
        //addRoutine(route_infos);
        //OpenRealTimeSpectrunfile();
         /*intent方法*/
        map = getIntent().getParcelableExtra(Map_Route_Result.ROUTE_KEY);
        if (null == map) {
            return;
        }

        if (map != null) {
            // 将消息展现出来。
            mapRoutePointInfoList = map.getMapRadioPointInfoList();
        }


        final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                LatLng centerLat;
                addRoutine(mapRoutePointInfoList, msg.what);
                super.handleMessage(msg);
            }
        };
        new Thread() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < mapRoutePointInfoList.size(); i++) {
                        Message message = new Message();
                        message.what = i;
                        Log.e("pocess", "size " + String.valueOf(mapRoutePointInfoList.size()));
                        handler.sendMessage(message);//发送消息
                        Log.e("pocess", "now is here 12");
                        Thread.sleep(100);//线程暂停3秒，单位毫秒

                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("refreshthread error...");
                }
            }
        }.start();

    }

    public static List<SpecInfo> route_infos = new ArrayList<SpecInfo>();


    /**
     * 添加点、线、多边形、圆、文字
     */
    public void addRoutine(List<MapRadioPointInfo> infos, int i) {
        LatLng latLng = null, first_latLng, end_latlng;
        int color = 0xffddddff;
        int spectrum = 0;


        if (i == 0) {
            MarkerOptions ooA = new MarkerOptions().position(new LatLng(infos.get(i).getLatitude(), infos.get(i).getLongitude())).icon(stationMarker);
            mMarkerA = (Marker) (mRoute_BaiduMap.addOverlay(ooA));
        } else {
            first_latLng = new LatLng(infos.get(i - 1).getLatitude(), infos.get(i - 1).getLongitude());
            end_latlng = new LatLng(infos.get(i).getLatitude(), infos.get(i).getLongitude());




            points.add(first_latLng);
            points.add(end_latlng);
            spectrum = (int) infos.get(i).getEqualPower();
            color = getResponseColor(spectrum);
            colorValue.add(color);
            OverlayOptions ooPolyline1 = new PolylineOptions().width(10)
                    .color(0xAAFF0000).points(points).colorsValues(colorValue);
            mColorfulPolyline = (Polyline) mRoute_BaiduMap.addOverlay(ooPolyline1);
            colorValue.clear();
            points.clear();


        }


        // spectrum_16 = Integer.toHexString(spectrum);


    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route_menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.id_map_route_settings) {

            Intent intent = new Intent(Map_RouteMap.this, Map_Route_Setting.class);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void clearClick() {
        // 清除所有图层
        mRoute_MapView.getMap().clear();
    }


    @Override
    protected void onPause() {
        mRoute_MapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mRoute_MapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        mRoute_MapView.onDestroy();


        super.onDestroy();
    }

    protected int getResponseColor(double spectrum) {

        int color = 0xffddddff;
        int power = (int) spectrum;
        int index = 0;
        if ((power >= -100) && (power < 20)) {
            if (Math.abs(power % 10) < 5 && Math.abs(power % 10) >= 0)
                index = power / 10 * 2 - 1;
            else if (Math.abs(power % 10) >= 5 && Math.abs(power % 10) < 9)
                index = power / 10 * 2;
        } else if (power < -100)
            index = -21;
        else if (power > 20)
            index = 5;

        switch (index) {
            case 5:
                color = 0x66960000;
                break;
            case 4:
                color = 0x66C80006;
                break;
            case 3:
                color = 0x66E10006;
                break;
            case 2:
                color = 0x66F50400;
                break;
            case 1:
                color = 0x66FD2D00;
                break;
            case 0:
                color = 0x66FA5000;
                break;
            case -1:
                color = 0x66FF7302;
                break;
            case -2:
                color = 0x66FFb600;
                break;
            case -3:
                color = 0x66FFD600;
                break;
            case -4:
                color = 0x66FBFF0E;
                break;
            case -5:
                color = 0x66DBFE00;
                break;
            case -6:
                color = 0x66B0FF4E;
                break;
            case -7:
                color = 0x667FFE7D;
                break;
            case -8:
                color = 0x665EFE9F;
                break;
            case -9:
                color = 0x6635FCC8;
                break;
            case -10:
                color = 0x6604FDC5;
                break;
            case -11:
                color = 0x6606E6DC;
                break;
            case -12:
                color = 0x6602CEFF;
                break;
            case -13:
                color = 0x6608AAFF;
                break;
            case -14:
                color = 0x660895FF;
                break;
            case -15:
                color = 0x660376FF;
                break;
            case -16:
                color = 0x66005BFF;
                break;
            case -17:
                color = 0x661717FF;
                break;
            case -18:
                color = 0x660000DB;
                break;
            case -19:
                color = 0x660000BD;
                break;
            case -20:
                color = 0x660000A2;
                break;
            case -21:
                color = 0x66000082;
                break;

        }

        return color;
    }

    //打开实时功率谱文件  并将相应频段范围的数据存到数组中显示
    private void OpenRealTimeSpectrunfile() {

        int temp = 0;
        int len = 0;
        ArrayList fileName = GetFileName(PSFILE_PATH);
        BubbleSortDateFile(fileName);
       // for (int i = 0; i < fileName.size(); i++)
            //Log.e("FileName", String.valueOf(fileName.get(i)));
        {
            File file = new File(PSFILE_PATH,
                    String.valueOf(fileName.get(0)));
            FileInputStream in = null;
            try {
                in = new FileInputStream(file);
                byte[] content = new byte[4000];
                byte[] buffer = new byte[4000];
                while ((temp = in.read(buffer)) != -1) {
                    content = buffer;

                }
                Log.e("FileFirst", Integer.toHexString(content[0] & 0xFF) );
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //获取当前目录下所有的功率谱文件 按时间顺序
    public ArrayList<String> GetFileName(String fileAbsolutePath) {
        ArrayList<String> Filename = new ArrayList<>();
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String name = subFile[iFileLength].getName();
                // 判断是否为pwr结尾
                if (name.trim().toLowerCase().endsWith(".pwr")) {
                    Filename.add(name);
                }
            }
        }
        return Filename;
    }

    private ArrayList<String> BubbleSortDateFile(ArrayList<String> arrayList) {

        String temp; // 记录临时中间值
        int size = arrayList.size(); // 数组大小
        for (int i = 0; i < size - 1; i++) {
            for (int j = i + 1; j < size; j++) {
                if (arrayList.get(i).compareTo(arrayList.get(j)) >0) { // 交换两数的位置
                    temp = arrayList.get(i);
                    arrayList.set(i, arrayList.get(j)) ;
                    arrayList.set(j,temp) ;
                }
            }
        }
        return  arrayList;
    }
}