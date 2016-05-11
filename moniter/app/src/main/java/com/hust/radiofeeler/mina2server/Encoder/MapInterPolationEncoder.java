package com.hust.radiofeeler.mina2server.Encoder;

import android.util.Log;

import com.hust.radiofeeler.bean2server.MapInterpolation;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MapInterPolationEncoder implements MessageEncoder<MapInterpolation> {
    @Override
    public void encode(IoSession ioSession, MapInterpolation map,
                       ProtocolEncoderOutput out) throws Exception {
        if(map!=null){
            IoBuffer buffer=IoBuffer.allocate(21,false);
            byte[] bytes=GetBytes(map);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("interpolation","请求帧："+ Arrays.toString(bytes));
        }
    }

    //电磁分布态势请求数据帧
    private byte[] GetBytes(MapInterpolation map){
        byte[] b=new byte[21];
        b[0]=0x55;
        b[1]= (byte) 0xC3;
        b[2]= (byte) (map.getEquipmentID()&0xff);
        b[3]= (byte) ((map.getEquipmentID()>>8)&0xff);
        b[4]= (byte) ((map.getCentralFreq()>>8)&0xff);//中心频率
        b[5]= (byte) (map.getCentralFreq()&0xff);
        b[6]= (byte) (map.getBand()&0xff);
        b[7]= (byte) getRaidus(map.getRadius());
        b[8]= (byte) getDeita((int) (map.getDieta()*10));
        b[9]= (byte) (map.getFreshtime()&0xff);
        byte[] byte1=map.getStartTime();
        System.arraycopy(byte1,0,b,10,4);
        byte[] byte2=map.getEndTime();
        System.arraycopy(byte2,0,b,14,4);
        b[20]= (byte) 0xAA;
        return  b;
    }

    private int getRaidus(int radius){
        int data=0;
        switch(radius){
            case 5:
                data=1;
                break;
            case 10:
                data=2;
                break;
            case 20:
                data=3;
                break;
            case 50:
                data=4;
                break;
            case 100:
                data=5;
                break;
            case 200:
                data=6;
                break;
            default:
                break;
        }
        return data;
    }

    private int getDeita(int deita){
        int data=0;
        switch(deita){
            case 1:
                data=1;
                break;
            case 2:
                data=2;
                break;
            case 5:
                data=3;
                break;
            case 10:
                data=4;
                break;
            case 20:
                data=5;
                break;
            case 50:
                data=6;
                break;
            case 100:
                data=7;
                break;
            default:
                break;
        }
        return data;
    }
}
