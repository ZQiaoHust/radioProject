package com.example.administrator.testsliding.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_StationState;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_StationStateEncoder implements MessageEncoder<Query_StationState> {
    @Override
    public void encode(IoSession ioSession, Query_StationState state,
                       ProtocolEncoderOutput out) throws Exception {

        if(state!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=state.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发查询："+ Arrays.toString(bytes));
        }

    }
}
