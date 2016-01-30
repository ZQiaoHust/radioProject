package com.example.administrator.testsliding.mina2FPGA.Encode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.ReceiveRight;

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
            Log.d("abcd", "回复成功数据时间: "+ String.valueOf(System.currentTimeMillis()));
            Log.d("abcd", "回复成功数据帧: "+ Arrays.toString(bytes));
        }

    }


}
