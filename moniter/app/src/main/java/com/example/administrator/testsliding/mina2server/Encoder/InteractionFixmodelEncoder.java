package com.example.administrator.testsliding.mina2server.Encoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.InteractionFixmodeRequest;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/30.
 */
public class InteractionFixmodelEncoder implements MessageEncoder<InteractionFixmodeRequest> {
    private ComputePara compute=new ComputePara();
    @Override
    public void encode(IoSession ioSession, InteractionFixmodeRequest fix,
                       ProtocolEncoderOutput out) throws Exception {

        if(fix!=null){
            IoBuffer buffer=IoBuffer.allocate(30,true);
            byte[] bytes=GetBytes(fix);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("abnormal", Arrays.toString(bytes));

        }

    }

    private byte[] GetBytes(InteractionFixmodeRequest fix){
        byte[] data=new byte[30];
        byte[] data1=new byte[3];
        byte[] data2=new byte[3];
        byte[] data3=new byte[3];
        byte[] data4=new byte[5];
        data[0]=0x55;
        data[1]= (byte) 0xAE;
        data[2]=(byte) (fix.getEqiupmentID() & 0xff);
        data[3]=(byte) ((fix.getEqiupmentID()>>8) & 0xff);
        data[4]=(byte) (fix.getIDcard()&0xff);
        data[5]=(byte) ((fix.getIDcard()>>8) & 0xff);

        data[6]= (byte) fix.getFreqNum();
        if(fix.getFix1()!=0){
            data1=compute.ComputeFloatTobyte(fix.getFix1());
            System.arraycopy(data1, 0, data, 7, 3);
        }

        if(fix.getFix2()!=0){
            data2=compute.ComputeFloatTobyte(fix.getFix2());
            System.arraycopy(data2, 0, data, 10, 3);
        }
        if(fix.getFix3()!=0){
            data3=compute.ComputeFloatTobyte(fix.getFix3());
            System.arraycopy(data3, 0, data,13, 3);
        }
        data[16]= (byte) (fix.getRecvGain()+3);
        data[17]=fix.getIQband_ratio();
        data[18]=(byte)fix.getBlockNum();
        data4=fix.getTime();
        System.arraycopy(data4, 0, data,19,5 );

        data[29]= (byte) 0xAA;

        return data;
    }
}
