package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_Connect;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_ConnectEncoder implements MessageEncoder<Query_Connect> {
    @Override
    public void encode(IoSession ioSession, Query_Connect connect,
                       ProtocolEncoderOutput out) throws Exception {

        if(connect!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=connect.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发硬件接入方式："+ Arrays.toString(bytes));
        }

    }
}
