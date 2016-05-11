package com.hust.radiofeeler.tab2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.R;
import com.hust.radiofeeler.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2015/10/26.
 */
public class Chart_Abnormal extends Activity {
    private List<Map<String, Object>> listItems;
    private ListView recordListView;
    private SimpleAdapter simpleAdapter;

    private Timer timer = new Timer();
    private TimerTask task;
    private Handler handler;
    private int seqNum=0;//表的序号

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.chart_abnormal,container,false);
//    }

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        listItems = new ArrayList<>();
//        recordListView = (ListView) getActivity().findViewById(R.id.abnormal_frequency_listview);
//        seqNum=0;
//        /*新建适配器，适配器加载数据源，同时在这里面让适配器加载自定义的布局xml,自定义的表格的每一行的样式*/
//        if(listItems!=null) {
//            simpleAdapter = new SimpleAdapter(getActivity(), listItems,
//                    R.layout.abnormal_frequency_item, new String[]{"seq_num", "freq", "PowerSpectrum"},
//                    new int[]{R.id.seq_num, R.id.freq, R.id.PowerSpectrum});
//
//            recordListView.setAdapter(simpleAdapter);
//        }
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_abnormal);
        listItems = new ArrayList<>();
        recordListView = (ListView)findViewById(R.id.abnormal_frequency_listview);
        seqNum=0;
        /*新建适配器，适配器加载数据源，同时在这里面让适配器加载自定义的布局xml,自定义的表格的每一行的样式*/
        if(listItems!=null) {
            simpleAdapter = new SimpleAdapter(Chart_Abnormal.this, listItems,
                    R.layout.abnormal_frequency_item, new String[]{"seq_num", "freq", "PowerSpectrum"},
                    new int[]{R.id.seq_num, R.id.freq, R.id.PowerSpectrum});

            recordListView.setAdapter(simpleAdapter);
        }
        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_chartAb);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
                Chart_Abnormal.this.finish();
            }

            @Override
            public void rightclick() {

            }
        });
    }

    @Override
    public void onPause() {
        //当被切到其他frgment时，列表从头开始
        seqNum=0;
        listItems.clear();
      //  timer.cancel();
        super.onPause();
    }

    @Override
    public void onStart() {
        //回到本fragment时又开始显示
        Constants.Queue_AbnormalFreq_List.clear();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == 1) {

                    updateChart();
                }
            }
        };

        task = new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                handler.sendMessage(message);
            }
        };

        timer.schedule(task, 1000, 1000);
        super.onStart();
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }

    private void updateChart(){
        if(!Constants.Queue_Abnormal.isEmpty()){
            Map<Float,Float> mapList= Constants.Queue_Abnormal.poll();
            Iterator<Float> iter = mapList.keySet().iterator();
            while (iter.hasNext()) {
                seqNum++;
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("seq_num", seqNum);
                float key = iter.next();
                map.put("freq", key);
                float value = mapList.get(key);
                map.put("PowerSpectrum", value);
                listItems.add(map);
            }
            simpleAdapter.notifyDataSetChanged();
        }
    }


}
