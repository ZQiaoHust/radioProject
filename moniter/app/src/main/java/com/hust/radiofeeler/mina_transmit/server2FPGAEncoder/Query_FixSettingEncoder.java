package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGAQuery.Query_FixSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Query_FixSettingEncoder implements MessageEncoder<Query_FixSetting> {
    @Override
    public void encode(IoSession ioSession, Query_FixSetting fix,
                       ProtocolEncoderOutput out) throws Exception {

        if(fix!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=fix.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发定频参数设置："+ Arrays.toString(bytes));
        }

    }
}
