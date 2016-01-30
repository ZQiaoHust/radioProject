package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.HistoryIQRequest;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/30.
 */
public class HistoryIQEncoder implements MessageEncoder<HistoryIQRequest> {
    @Override
    public void encode(IoSession ioSession, HistoryIQRequest IQ,
                       ProtocolEncoderOutput out) throws Exception {

        if(IQ!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=GetBytes(IQ);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("axy", Arrays.toString(bytes));
        }

    }

    private byte[] GetBytes(HistoryIQRequest IQ){
        byte[] data=new byte[17];
        byte[] data1=new byte[4];

        data[0]=0x55;
        data[1]= (byte) 0xAC;
        data[2]=(byte) (IQ.getEqiupmentID() & 0xff);
        data[3]=(byte) ((IQ.getEqiupmentID()>>8) & 0xff);
        data[4]=(byte) (IQ.getIDcard() & 0xff);
        data[5]=(byte) ((IQ.getIDcard()>>8) & 0xff);
        data1= IQ.getStartTime();
        System.arraycopy(data1, 0, data, 6, 4);

        data1=IQ.getEndTime();
        System.arraycopy(data1, 0, data, 10, 4);

        data[16]= (byte) 0xAA;

        return data;

    }
}
