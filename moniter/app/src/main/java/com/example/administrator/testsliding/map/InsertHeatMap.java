
package com.example.administrator.testsliding.map;

        import android.app.Activity;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.os.Environment;
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
        import com.example.administrator.testsliding.R;

        import java.io.File;


/**
 * Created by Administrator on 2015-11-18.
 */

public class InsertHeatMap extends Activity {

    private static final LatLng GEO_WUHAN = new LatLng(30.515, 114.420);
    private static String PSFILE_PATH = Environment.getExternalStorageDirectory().
            getAbsolutePath() + "/DCIM/Camera/IMG_19700125_060900";

    private MapView mStation_MapView = null;

    private BaiduMap mStation_BaiduMap = null;

    private int count = 0;


    BitmapDescriptor bdA = BitmapDescriptorFactory
            .fromResource(R.drawable.icon_gcoding);


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_staitonmap);
        mStation_MapView = (MapView) findViewById(R.id.StationMapView);
        mStation_BaiduMap = mStation_MapView.getMap();
        MapStatusUpdate u1 = MapStatusUpdateFactory.newLatLng(GEO_WUHAN);

        mStation_BaiduMap.setMapStatus(u1);
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(12);
        mStation_BaiduMap.setMapStatus(msu);
        //ȥ���Ŵ���С�ı�ʶ
        count = mStation_MapView.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = mStation_MapView.getChildAt(i);
            if (child instanceof ZoomControls) {
                child.setVisibility(View.INVISIBLE);
            }
        }
        Log.d("Absolute route:", Environment.getExternalStorageDirectory().getAbsolutePath());

        LatLng southwest = new LatLng(30.51511, 114.42001);
        LatLng northeast = new LatLng(31.515, 115.420);
        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(northeast)
                .include(southwest)
               .build();

////����Ground��ʾ��ͼƬ
//        BitmapDescriptor bdGround = BitmapDescriptorFactory.fromPath(PSFILE_PATH);

//        OverlayOptions ooGround = new GroundOverlayOptions()
//                .positionFromBounds(bounds)
//                .image(bdGround)
//                .transparency(0.8f);

       // mStation_BaiduMap.addOverlay(ooGround);
    }

}
