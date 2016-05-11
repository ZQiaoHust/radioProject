package com.hust.radiofeeler.SlideMenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.hust.radiofeeler.R;

/**
 * Created by H on 2015/10/24.
 */
public class UploadData extends Activity {
    private EditText edIP, edPort, edData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploaddata);

        findViewById(R.id.title_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadData.this.finish();
            }
        });
//
//        edIP = (EditText) this.findViewById(R.id.ed_ip);
//        edPort = (EditText) this.findViewById(R.id.ed_port);
//
//        findViewById(R.id.bn_connect).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//
//                    String hostIP = edIP.getText().toString();
//                    int port = Integer.parseInt(edPort.getText().toString());
//                     Constants.PORTValue=port;
//                    Constants.IPValue=hostIP;
////                    Intent intent = new Intent(UploadData.this, ToServerMinaService.class);
////                    startService(intent);
//
//                } catch (NumberFormatException e) {
//                    Toast.makeText(UploadData.this, "输入错误", Toast.LENGTH_SHORT).show();
//                    e.printStackTrace();
//                }
//
//            }
//        });
    }


}
