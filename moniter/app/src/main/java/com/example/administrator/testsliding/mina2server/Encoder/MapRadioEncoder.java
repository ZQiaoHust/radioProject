package com.example.administrator.testsliding.mina2server.Encoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.MapRadio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapRadioEncoder implements MessageEncoder<MapRadio> {
    @Override
    public void encode(IoSession ioSession, MapRadio map,
                       ProtocolEncoderOutput out) throws Exception {
        if(map!=null){
            IoBuffer buffer=IoBuffer.allocate(21,false);
            byte[] bytes=GetBytes(map);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }
    }

    //电磁分布态势请求数据帧
    private byte[] GetBytes(MapRadio map){
        byte[] b=new byte[21];
        b[0]=0x55;
        b[1]= (byte) 0xA2;
        b[2]= (byte) (map.getEquipmentID()&0xff);
        b[3]= (byte) ((map.getEquipmentID()>>8)&0xff);
        b[4]= (byte) ((map.getCentralFreq()>>8)&0xff);//中心频率
        b[5]= (byte) (map.getCentralFreq()&0xff);
        b[6]= (byte) (map.getBand()&0xff);
        b[7]= (byte) (map.getRadius()&0xff);
        int aa= (int) map.getDieta();
        int  xx= (int) ((map.getDieta()-aa)*8);
        b[8]= (byte) (((aa&0xff)<<3)+(xx&0x07));//分辨率，低三位是小数
        b[9]= (byte) (map.getFreshtime()&0xff);
        byte[] byte1=map.getStartTime();
        System.arraycopy(byte1,0,b,10,4);
        byte[] byte2=map.getEndTime();
        System.arraycopy(byte2,0,b,14,4);
        b[20]= (byte) 0xAA;
        return  b;

    }
}
