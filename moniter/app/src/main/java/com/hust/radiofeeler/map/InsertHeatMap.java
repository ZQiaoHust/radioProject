
package com.hust.radiofeeler.map;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.os.Environment;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;
        import android.view.View;
        import android.widget.CheckBox;
        import android.widget.ImageView;
        import android.widget.ZoomControls;

        import com.baidu.mapapi.SDKInitializer;
        import com.baidu.mapapi.map.BaiduMap;
        import com.baidu.mapapi.map.BitmapDescriptor;
        import com.baidu.mapapi.map.BitmapDescriptorFactory;
        import com.baidu.mapapi.map.GroundOverlayOptions;
        import com.baidu.mapapi.map.MapStatusUpdate;
        import com.baidu.mapapi.map.MapStatusUpdateFactory;
        import com.baidu.mapapi.map.MapView;
        import com.baidu.mapapi.map.Marker;
        import com.baidu.mapapi.map.OverlayOptions;
        import com.baidu.mapapi.model.LatLng;
        import com.baidu.mapapi.model.LatLngBounds;
        import com.hust.radiofeeler.R;
        import com.hust.radiofeeler.bean2server.MapInterpolationReply;
        import com.hust.radiofeeler.tab1.Map_interpolationResult;


        import java.io.File;
        import java.util.ArrayList;
        import java.util.TimerTask;


/**
 * Created by Administrator on 2015-11-18.
 */

public class InsertHeatMap extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private static String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/com.hust.radiofeeler/interpolationFile/";
    public  final static String INSERTHEATMAP_KEY = "com.example.administrator.testsliding.INSERTHEATMAP";
    private MapView mIn_MapView = null;
    private LatLng  leftdownLatlng,rightuplatlng;
    private BaiduMap mIn_BaiduMap = null;
    private Thread thread;
    private int count = 0;
    private MapInterpolationReply interpolationReply=new MapInterpolationReply();

    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_staitonmap);
        mIn_MapView = (MapView) findViewById(R.id.StationMapView);
        mIn_BaiduMap = mIn_MapView.getMap();

        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12);
        mIn_BaiduMap.setMapStatus(msu);

        count = mIn_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mIn_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }

        interpolationReply = getIntent().getParcelableExtra(Map_interpolationResult.INSERTHEATMAP_KEY);
        //MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(new LatLng(interpolationReply.getG1latitude(),interpolationReply.getG1longitude()));
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);
        mIn_BaiduMap.setMapStatus(u1);


        //final ArrayList<String> fileName = GetFileName("/storage/sdcard/interpolationFile");
        final ArrayList<String> fileName = GetFileName(PSFILE_PATH);
        BubbleSortDateFile(fileName);
        Log.d("fileNaME", PSFILE_PATH + fileName.get(0));
         final Handler handler = new Handler() {
            public void handleMessage(Message msg) {
                LatLng centerLat;
                if ((msg.what >= 0) && (msg.what <= 9) && (msg.what < interpolationReply.getFreNum())) {
                    Log.d("pocess", "now is inside handler");
                    mIn_MapView.getMap().clear();
                    //设置中心点
                    leftdownLatlng = new LatLng(interpolationReply.getG1latitude(),interpolationReply.getG1longitude());
                    rightuplatlng = new LatLng(interpolationReply.getG2latitude(),interpolationReply.getG2longitude());

                    //LatLng southwest = new LatLng(30.51511, 114.42001);
                    //LatLng northeast = new LatLng(31.515, 115.420);
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(leftdownLatlng)
                            .include(rightuplatlng)
                            .build();
        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromPath(PSFILE_PATH+fileName.get(msg.what));

        OverlayOptions ooGround = new GroundOverlayOptions()
                .positionFromBounds(bounds)
                .image(bdGround)
                .transparency(0.5f);

                     mIn_BaiduMap.addOverlay(ooGround);
                } else {
                    Log.d("pocess", "now is here 12");
                }
                super.handleMessage(msg);
            }
        };


          thread = new Thread() {
            @Override
            public void run() {
                try {

                    for ( int i = 0; i < interpolationReply.getFreNum(); i++) {
                        Message message = new Message();
                        message.what =i ;
                        Log.e("pocess"," size "+String.valueOf(interpolationReply.getFreNum()));
                        Log.e("pocess ", "now is " + String.valueOf(i) + "message");
                        handler.sendMessage(message);//发送消息
                        //Thread.sleep(interpolationReply.getFreshtime()*1000);//线程暂停3秒，单位毫秒
                        Thread.sleep(10000);//线程暂停3秒，单位毫秒
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("refreshthread error...");
                }
            }
        };
        thread.start();

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
                // 判断是否为png结尾
                if (name.trim().toLowerCase().endsWith(".png")) {
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

    @Override
    protected void onDestroy() {
        mIn_MapView.onDestroy();
        thread.interrupt();
        super.onDestroy();
    }
}
