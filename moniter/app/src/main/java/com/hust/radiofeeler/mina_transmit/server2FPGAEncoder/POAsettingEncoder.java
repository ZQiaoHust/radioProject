package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_Press;
import com.hust.radiofeeler.bean2server.POA;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class POAsettingEncoder implements MessageEncoder<POA> {
    @Override
    public void encode(IoSession ioSession, POA poa,
                       ProtocolEncoderOutput out) throws Exception {

        if(poa!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=poa.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发POA定位："+ Arrays.toString(bytes));
        }

    }
}
