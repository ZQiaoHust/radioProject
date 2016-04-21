package com.example.administrator.testsliding.mina2server.Encoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.LocationAbnormalRequest;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/27.
 */
public class LocationAbnormalEncoder implements MessageEncoder<LocationAbnormalRequest> {
    @Override
    public void encode(IoSession ioSession, LocationAbnormalRequest location,
                       ProtocolEncoderOutput out) throws Exception {
        if(location!=null){
            IoBuffer buffer=IoBuffer.allocate(21,true);
            byte[] bytes=GetBytes(location);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("abnormal", Arrays.toString(bytes));

        }

    }
    private byte[] GetBytes(LocationAbnormalRequest location){
        ComputePara compute=new ComputePara();
        byte[] bytes=new byte[21];
        bytes[0]=0x55;
        bytes[1]= (byte) 0xA4;
        bytes[2] = (byte) (location.getEquiomentID()& 0xff);//设备ID号的低8位
        bytes[3] = (byte) ((location.getEquiomentID()>> 8) & 0xff);//设备ID号的高位


        bytes[4]=location.getLocationWay();
        byte[] data=compute.ComputeFloatTobyte(location.getAbFreq());
        System.arraycopy(data,0,bytes,5,3);

        bytes[8]=location.getIQband_radio();
        bytes[9]= (byte) location.getBlockNum();
        byte[] data1=location.getTime2min();
        System.arraycopy(data1,0,bytes,10,5);

        bytes[18]=0;//校验码
        bytes[20]= (byte) 0xAA;
        return bytes;
    }
}
