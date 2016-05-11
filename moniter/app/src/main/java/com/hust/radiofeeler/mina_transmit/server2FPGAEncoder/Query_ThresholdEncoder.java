package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_Threshold;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_ThresholdEncoder implements MessageEncoder<Query_Threshold> {
    @Override
    public void encode(IoSession ioSession, Query_Threshold sweep,
                       ProtocolEncoderOutput out) throws Exception {

        if(sweep!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=sweep.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发检测门限查询："+ Arrays.toString(bytes));
        }

    }
}
