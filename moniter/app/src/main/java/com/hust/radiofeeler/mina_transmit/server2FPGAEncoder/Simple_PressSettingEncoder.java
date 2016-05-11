package com.hust.radiofeeler.mina_transmit.server2FPGAEncoder;

import android.util.Log;

import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_PressSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_PressSettingEncoder implements MessageEncoder<Simple_PressSetting> {
    @Override
    public void encode(IoSession ioSession, Simple_PressSetting press,
                       ProtocolEncoderOutput out) throws Exception {

        if(press!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=press.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","FPAGsession转发设置压制参数："+ Arrays.toString(bytes));
        }

    }
}
