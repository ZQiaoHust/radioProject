package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.Station_CurrentRequst;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/27.
 */
public class StationCurrentEncoder implements MessageEncoder<Station_CurrentRequst> {
    @Override
    public void encode(IoSession ioSession, Station_CurrentRequst current,
                       ProtocolEncoderOutput out) throws Exception {

        if(current!=null){
            IoBuffer buffer=IoBuffer.allocate(24,false);
            byte[] bytes=GetBytes(current);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("current", Arrays.toString(bytes));
        }

    }

    private byte[] GetBytes(Station_CurrentRequst current){
        ComputePara compute=new ComputePara();
        byte[] bytes=new byte[24];
        bytes[0]=0x55;
        bytes[1]= (byte) 0xA6;
        bytes[2] = (byte) (current.getEquiomentID()& 0xff);//设备ID号的低8位
        bytes[3] = (byte) ((current.getEquiomentID()>> 8) & 0xff);//设备ID号的高位

        bytes[4]= (byte) ((current.getIDcard()>>16)&0xff);
        bytes[5]= (byte) ((current.getIDcard()>>8)&0xff);
        bytes[6]= (byte) (current.getIDcard()&0xff);

        bytes[7]=current.getLocationWay();
        byte[] data=compute.ComputeFloatTobyte(current.getAbFreq());
        System.arraycopy(data,0,bytes,8,3);

        bytes[11]=current.getIQband_radio();
        bytes[12]= (byte) current.getBlockNum();
        byte[] data1=current.getTime2min();
        System.arraycopy(data1,0,bytes,13,5);

        bytes[21]=0;//校验码
        bytes[23]= (byte) 0xAA;
        return bytes;
    }
}
