package com.hust.radiofeeler.mina2FPGA.Encode;

import android.util.Log;

import com.hust.radiofeeler.Bean.ReceiveRight;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by jinaghao on 15/11/18.
 */
public class ReceiveRightEncoder implements MessageEncoder<ReceiveRight> {

    @Override
    public void encode(IoSession ioSession, ReceiveRight right,
                       ProtocolEncoderOutput out) throws Exception {

        if(right!=null) {


            // byte[] bytes = right.getContent();
            byte[] bytes=new byte[17];
            bytes[0]= (byte) 0x55;
            bytes[1]= (byte) 0xff;
            bytes[16]= (byte) 0xaa;

            IoBuffer buffer = IoBuffer.allocate(17);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("ReceiveRightEncoder", Arrays.toString(bytes));

        }

    }


}
