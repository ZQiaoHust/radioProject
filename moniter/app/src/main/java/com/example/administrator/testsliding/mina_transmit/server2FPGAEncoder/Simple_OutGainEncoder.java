package com.example.administrator.testsliding.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2Transmit.server2FPGASetting.Simple_OutGain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_OutGainEncoder implements MessageEncoder<Simple_OutGain> {
    @Override
    public void encode(IoSession ioSession, Simple_OutGain gain,
                       ProtocolEncoderOutput out) throws Exception {

        if(gain!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=gain.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发设置发射通道增益："+ Arrays.toString(bytes));
        }

    }
}
