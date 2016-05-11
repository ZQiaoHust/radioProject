package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_Connect;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_ConnectEncoder implements MessageEncoder<Simple_Connect> {
    @Override
    public void encode(IoSession ioSession, Simple_Connect connect,
                       ProtocolEncoderOutput out) throws Exception {

        if(connect!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=connect.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发设置连接方式："+ Arrays.toString(bytes));
        }

    }
}
