package com.example.administrator.testsliding.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_PressSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_PressSettingEncoder implements MessageEncoder<Query_PressSetting> {
    @Override
    public void encode(IoSession ioSession, Query_PressSetting press,
                       ProtocolEncoderOutput out) throws Exception {

        if(press!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=press.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发压制设置查询："+ Arrays.toString(bytes));
        }

    }
}
