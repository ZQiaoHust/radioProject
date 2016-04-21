package com.example.administrator.testsliding.mina2server.Encoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.MapRoute;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapRouteEncoder implements MessageEncoder<MapRoute> {
    @Override
    public void encode(IoSession ioSession, MapRoute map,
                       ProtocolEncoderOutput out) throws Exception {

        if(map!=null){
            IoBuffer buffer=IoBuffer.allocate(19,false);
            byte[] bytes=GetBytes(map);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("map", Arrays.toString(bytes));
        }

    }

    //请求数据帧
    private byte[] GetBytes(MapRoute map){
        byte[] b=new byte[19];
        b[0]=0x55;
        b[1]= (byte) 0xA3;
        b[2]= (byte) (map.getEquipmentID()&0xff);
        b[3]= (byte) ((map.getEquipmentID()>>8)&0xff);
        b[5]= (byte) ((map.getCentralFreq()>>8)&0xff);//中心频率
        b[6]= (byte) (map.getCentralFreq()&0xff);
        b[7]= (byte) (map.getBand()&0xff);
        ;
        byte[] byte1=map.getStartTime();
        System.arraycopy(byte1,0,b,8,4);
        byte[] byte2=map.getEndTime();
        System.arraycopy(byte2,0,b,12,4);
        b[18]= (byte) 0xAA;
        return  b;

    }
}
