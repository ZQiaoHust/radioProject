package com.hust.radiofeeler.SlideMenu;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.hust.radiofeeler.Bean.Connect;
import com.hust.radiofeeler.Bean.Query;
import com.hust.radiofeeler.Bean.UploadData;
import com.hust.radiofeeler.GlobalConstants.ConstantValues;
import com.hust.radiofeeler.Mina.Broadcast;
import com.hust.radiofeeler.R;

/**
 * Created by H on 2015/10/24.
 */
public class SetUploadData extends Activity {
    private Switch mSwitch;
    private Button mgetUploadDataButton;


    private BroadcastReceiver SetUploadDataReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if(action.equals(ConstantValues.uploadQuery)){
                Connect connect=intent.getParcelableExtra("data");
                if(connect==null){
                    return;
                }
                int conn=connect.getConn();
                if(conn==0X01){

                    Toast toast=Toast.makeText(SetUploadData.this, "当前wifi连接：" ,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP , 0, 800);
                    toast.show();
                }else if(conn==0x02){
                    Toast toast=Toast.makeText(SetUploadData.this, "当前bluetooth连接：" ,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP , 0, 800);
                    toast.show();

                }else {
                    Toast toast=Toast.makeText(SetUploadData.this, "当前USB连接：" ,
                            Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP , 0, 800);
                    toast.show();
                }

            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaddata);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConstantValues.uploadQuery);
        filter.addAction(ConstantValues.uploadDataSet);
        filter.addAction(ConstantValues.ConnectPCBQuery);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(SetUploadDataReceiver, filter);

        mSwitch= (Switch) findViewById(R.id.data_upload);
        mgetUploadDataButton= (Button) findViewById(R.id.getDataUpload);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    UploadData uploadData=new UploadData();
                    uploadData.setFunc(0x0a);
                    if (uploadData == null) {
                        return;
                    }
                    Broadcast.sendBroadCast(SetUploadData.this,
                            ConstantValues.uploadDataSet, "uploadDataSet", uploadData);
                }else {
                    UploadData uploadData=new UploadData();
                    uploadData.setFunc(0x0B);
                    if (uploadData == null) {
                        return;
                    }
                    Broadcast.sendBroadCast(SetUploadData.this,
                            ConstantValues.uploadDataSet, "uploadDataSet", uploadData);
                }
            }
        });


        mgetUploadDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query = new Query();
                query.setequipmentID(0);
                query.setFuncID((byte) 0x1A);

                if (query != null) {
                    Broadcast.sendBroadCast(SetUploadData.this,
                            ConstantValues.uploadQuery, "uploadQuery", query);
                }
            }
        });

        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetUploadData.this.finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(SetUploadDataReceiver);
        SetUploadDataReceiver=null;
    }
}
