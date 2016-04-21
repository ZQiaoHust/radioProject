package com.example.administrator.testsliding.mina2server.Encoder;



import android.util.Log;

import com.example.administrator.testsliding.bean2server.Terminal_Online;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/24.
 */
public class TerminalOnlineEncoder implements MessageEncoder<Terminal_Online> {
    @Override
    public void encode(IoSession ioSession, Terminal_Online online,
                       ProtocolEncoderOutput out) throws Exception {

        if(online!=null){
            byte[] bytes=new byte[7];
            bytes[0]=0x55;
            bytes[1]= (byte) 0xA9;
            bytes[2]= (byte) (online.getEquipmentID()&0xff);
            bytes[3]= (byte) ((online.getEquipmentID()>>8)&0xff);
            bytes[4]=0;
            bytes[6]= (byte) 0xAA;

            IoBuffer buffer=IoBuffer.allocate(7);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("axy", Arrays.toString(bytes));

        }

    }
}
