package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2server.POA;
import com.hust.radiofeeler.bean2server.TDOA;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**TDOA定位
 * Created by Administrator on 2016/7/12.
 */
public class TDOAsettingEncoder implements MessageEncoder<TDOA> {
    @Override
    public void encode(IoSession ioSession, TDOA td,
                       ProtocolEncoderOutput out) throws Exception {

        if(td!=null){
            IoBuffer buffer=IoBuffer.allocate(19,true);
            byte[] bytes=td.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发TDOA定位："+ Arrays.toString(bytes));
        }

    }
}
