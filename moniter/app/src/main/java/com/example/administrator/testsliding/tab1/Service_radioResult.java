package com.example.administrator.testsliding.tab1;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.administrator.testsliding.GlobalConstants.ConstantValues;
import com.example.administrator.testsliding.R;
import com.example.administrator.testsliding.bean2server.File_ServiceRadio;
import com.example.administrator.testsliding.bean2server.ListMap;
import com.example.administrator.testsliding.view.MyTopBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ZengQiao on 2015/10/28.
 */
public class Service_radioResult extends Activity {
    private ListView mListView;
    private ArrayList<Map<String, Object>> listItems;

    // 接受消息广播
    private BroadcastReceiver contentRecevier = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            listItems.clear();
//            File_ServiceRadio file = intent.getParcelableExtra("data");
//            // 如果接受到空消息时过滤掉
//            if (null == file) {
//                return;
//            }
//            // 将消息展现出来。
//            if (file != null) {
//                listItems = Radio2ListItem(file);
//            }
            ArrayList<ListMap> mlist=new ArrayList<>();
            mlist=intent.getParcelableArrayListExtra("wirlessplan");
            listItems=List2ListItems(mlist);
//
            if (listItems != null) {
                SimpleAdapter simpleAdapter = new SimpleAdapter(Service_radioResult.this, listItems,
                        R.layout.serviceradio_item, new String[]{"section_num", "section", "section_attriutes"},
                        new int[]{R.id.section_num, R.id.section, R.id.section_attriutes});

                mListView.setAdapter(simpleAdapter);
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_radioresult);

        mListView = (ListView) findViewById(R.id.serviceRadio_listview);

        listItems = new ArrayList<>();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.RWIRLESSPLAN);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(contentRecevier, filter);

        MyTopBar topBar= (MyTopBar) findViewById(R.id.topbar_radioResult);
        topBar.setOnTopBarClickListener(new MyTopBar.TopBarClickListener() {
            @Override
            public void leftclick() {
               Service_radioResult.this.finish();
            }

            @Override
            public void rightclick() {

            }
        });
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(contentRecevier);
        contentRecevier = null;
//        stopService(intent);
//        intent = null;
        super.onDestroy();
    }

    private ArrayList<Map<String, Object>> List2ListItems(ArrayList<ListMap> mlist){
        ArrayList<Map<String, Object>> listItems;
        listItems = new ArrayList<>();
        listItems.clear();
        for(ListMap mlistMap:mlist){
            Map<String,Object>map = new HashMap<>();
            map.put("section_num",mlistMap.getNum() );
            map.put("section", mlistMap.getSection());
            map.put("section_attriutes", mlistMap.getSectionAttributes());

            listItems.add(map);

        }
        return listItems;
    }
    private List<Map<String, Object>> Radio2ListItem(File_ServiceRadio radio) {
        List<Map<String, Object>> listItems;
        listItems = new ArrayList<>();
        byte[] radioArray;
        radioArray = radio.getFileContent();

        // 将消息展现出来。
        if (radioArray != null) {
            //8个表示长度，4个不是主要内容
            for (int i = 4; i < radioArray.length - 3; i = i + 16) {
                String start = Freq2float(radioArray[i], radioArray[i + 1], radioArray[i + 2], radioArray[i + 3]);
                String end = Freq2float(radioArray[i + 4], radioArray[i + 5], radioArray[i + 6], radioArray[i + 7]);
                String section=null;
                if(start!=null&&end!=null) {
                    section = start + "~" + end;
                }
                else if(start==null){
                    int startint=0;
                    section = String.valueOf(startint) + "~" + end;
                }
                String str = null;
                for (int j = 0; j < 8; j++) {
                    if (radioArray[i + 8+ j] != 0) {
                        if (str != null) {
                            str = str + ChartPlan((radioArray[i + 8 + j] & 0xff)) + " ";
                        } else {
                            str = ChartPlan((radioArray[i + 8 + j] & 0xff)) + " ";
                        }
                    }
                }
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("section_num", (i / 16) + 1);
                if(section!=null) {
                    map.put("section", section);
                }
                if (str != null) {
                    map.put("section_attriutes", str);
                }
                listItems.add(map);
            }
        }
        return listItems;

    }

    //频率值截位
    private String Freq2float(byte b1, byte b2, byte b3, byte b4) {
        String freq = null;
        int intNum;
        float decimal;
        float de = 0;
        intNum = ((b1& 0xff) << 12)  +( (b2  & 0xff)<< 4) + ((b3& 0xff) >> 4) ;
        for (int i = 7; i > 0; i--) {
            de += ((b4 >> i) & 0x01) * Math.pow(2, i - 12);
        }
        decimal = (float) (((b3 >> 3) & 0x1) * 0.5 + ((b3 >> 2) & 0x1) * 0.25 + ((b3 >> 1) & 0x1) * 0.125 + (b3 & 0x1) * 0.0625 + de);

        java.text.DecimalFormat df = new java.text.DecimalFormat("0.00000");
        if((intNum + decimal)!=0) {
            freq = df.format(intNum + decimal);//截小数点后5位，返回为String
        }

        return freq;
    }

    //规定的业务属性查询表
    private  String ChartPlan(int number){
        String str=null;
        switch(number){
            case 1:
                str="固定";
                break;
            case 2:
                str="移动";
                break;
            case 3:
                str="无线电定位";
                break;
            case 4:
                str="卫星固定";
                break;
            case 5:
                str="空间研究";
                break;
            case 6:
                str="卫星地球探测";
                break;
            case 7:
                str="射电天文";
                break;
            case 8:
                str="广播";
                break;
            case 9:
                str="移动(航空移动除外)";
                break;
            case 10:
                str="无线电导航";
                break;
            case 11:
                str="航空无线电导航";
                break;
            case 12:
                str="水上移动";
                break;
            case 13:
                str="卫星移动";
                break;
            case 14:
                str="卫星间";
                break;
            case 15:
                str="卫星无线电导航";
                break;
            case 16:
                str="业余";
                break;
            case 17:
                str="卫星气象";
                break;
            case 18:
                str="标准频率和时间信号";
                break;
            case 19:
                str="空间操作";
                break;
            case 20:
                str="航空移动";
                break;
            case 21:
                str="卫星业余";
                break;
            case 22:
                str="卫星广播";
                break;
            case 23:
                str="航空移动(OR)";
                break;
            case 24:
                str="气象辅助";
                break;
            case 25:
                str="航空移动(R)";
                break;
            case 26:
                str="水上无线电导航";
                break;
            case 27:
                str="陆地移动";
                break;
            case 28:
                str="移动(航空移动(R)除外)";
                break;
            case 29:
                str="卫星无线电测定";
                break;
            case 30:
                str="卫星航空移动(R)";
                break;
            case 31:
                str="移动(航空移动(R)除外)";
                break;
            case 32:
                str="水上移动(遇险和呼叫)";
                break;
            case 33:
                str="水上移动(使用DSC的遇险和安全呼叫)";
                break;
            case 34:
                str="未划分";
                break;
            default:
                break;
        }

        return str;

    }
}
