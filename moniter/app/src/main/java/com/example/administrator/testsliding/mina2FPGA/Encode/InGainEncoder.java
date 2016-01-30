package com.example.administrator.testsliding.mina2FPGA.Encode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.InGain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/18.
 */
public class InGainEncoder implements MessageEncoder<InGain> {
//    public SettoFPGAPacket settoFPGAPacket;

    @Override
    public void encode(IoSession ioSession, InGain message, ProtocolEncoderOutput out) throws Exception {


//            int capacity = send.length;
//            IoBuffer buffer = IoBuffer.allocate(capacity, false);
//            buffer.setAutoExpand(true);
//            buffer.put(send);
//            buffer.flip();
//            out.write(buffer);
//            Log.d("xyz", "发送成功:" + capacity);

            byte[] bytes=RecvGainData(message);
            IoBuffer buffer = IoBuffer.allocate(17);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("xyz", "发送成功: ");

    }
    /**
     * 接受通道增益设置数据帧
     * @return
     */
    public byte[] RecvGainData(InGain gain){
        byte[] data=new byte[17];
        data[0]=0x55;
        data[1]=0x04;
        data[2]= (byte) (gain.getEqipmentID()&0xff);
        data[3]= (byte) ((gain.getEqipmentID()>>8)&0xff);
        data[4]=(byte)gain.getIngain();

        data[16]= (byte) 0xAA;
        return data;
    }
}
