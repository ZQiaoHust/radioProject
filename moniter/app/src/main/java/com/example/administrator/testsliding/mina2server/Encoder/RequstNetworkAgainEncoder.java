package com.example.administrator.testsliding.mina2server.Encoder;


import android.util.Log;

import com.example.administrator.testsliding.Bean.RequestNetworkAgain;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.bean2server.RequstNetwork;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetworkAgainEncoder implements MessageEncoder<RequestNetworkAgain>{
    @Override
    public void encode(IoSession ioSession, RequestNetworkAgain requstNetwork,
                       ProtocolEncoderOutput out)
            throws Exception {
        if (requstNetwork != null) {
            byte[] bytes1=requstNetwork.getContent();
            IoBuffer buffer = IoBuffer.allocate(17);
            buffer.put(bytes1);
            buffer.flip();
            out.write(buffer);
            Log.d("session", Arrays.toString(bytes1));
        }
    }


}