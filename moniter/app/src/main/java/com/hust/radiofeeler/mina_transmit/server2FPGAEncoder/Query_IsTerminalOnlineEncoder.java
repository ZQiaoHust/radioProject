package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_IsTerminalOnline;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_IsTerminalOnlineEncoder implements MessageEncoder<Query_IsTerminalOnline> {
    @Override
    public void encode(IoSession ioSession, Query_IsTerminalOnline data,
                       ProtocolEncoderOutput out) throws Exception {

        if(data!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=data.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发终端是否在网查询："+ Arrays.toString(bytes));
        }

    }
}
