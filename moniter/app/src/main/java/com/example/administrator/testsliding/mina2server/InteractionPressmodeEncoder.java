package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.InteractionPressmodeRequest;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/30.
 */
public class InteractionPressmodeEncoder implements MessageEncoder<InteractionPressmodeRequest> {
    private ComputePara compute=new ComputePara();
    @Override
    public void encode(IoSession ioSession, InteractionPressmodeRequest press,
                       ProtocolEncoderOutput out) throws Exception {
        if(press!=null){
            IoBuffer buffer=IoBuffer.allocate(27,true);
            byte[] bytes=GetBytes(press);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("abnormal", Arrays.toString(bytes));
        }

    }

    private byte[] GetBytes(InteractionPressmodeRequest press){
        byte[] data=new byte[27];
        byte[] data1=new byte[3];
        byte[] data2=new byte[3];

        data[0]=0x55;
        data[1]= (byte) 0xAF;
        data[2]=(byte) (press.getEqiupmentID() & 0xff);
        data[3]=(byte) ((press.getEqiupmentID()>>8) & 0xff);
        data[4]=(byte) (press.getIDcard()& 0xff);//指定ID号
        data[5]=(byte) ((press.getIDcard()>>8) & 0xff);//

        data[6]= (byte)press.getFreqNum();
        if(press.getPressFreq1()!=0){
            data1=compute.ComputeFloatTobyte(press.getPressFreq1());
            System.arraycopy(data1, 0, data, 7, 3);
        }

        if(press.getPressFreg2()!=0){
            data2=compute.ComputeFloatTobyte(press.getPressFreg2());
            System.arraycopy(data2, 0, data, 10, 3);
        }
        data[13]= (byte)press.getSendGain();

        data[14]= press.getSendModel();
        data[15]=(byte)((press.getSignalStyle()<<4)&0xff+press.getSignalBand());//信号类型占高四位，信号带宽占低四位
        //四个时间的转换
        data[16]=(byte) ((press.getT1()>>8)&0xff);
        data[17]=(byte) (press.getT1()&0xff);
        data[18]=(byte) ((press.getT2()>>8)&0xff);
        data[19]=(byte) (press.getT2()&0xff);
        data[20]=(byte) ((press.getT3()>>8)&0xff);
        data[21]=(byte) (press.getT3()&0xff);
        data[22]=(byte) ((press.getT4()>>8)&0xff);
        data[23]=(byte) (press.getT4()&0xff);
        data[26]= (byte) 0xAA;
        return data;


    }

}
