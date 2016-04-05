package com.example.administrator.testsliding.mina2FPGA.Encode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.FixCentralFreq;
import com.example.administrator.testsliding.Bean.Press;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressEncoder implements MessageEncoder<Press> {
   private ComputePara computePara=new ComputePara();
    @Override
    public void encode(IoSession ioSession, Press press, ProtocolEncoderOutput out) throws Exception {

        byte[] bytes=GetBytes(press);
        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
        Log.d("Encode", "压制频点"+Arrays.toString(bytes));
    }

    private byte[] GetBytes(Press press){
        byte[] data=new byte[17];
        byte[] data1=new byte[3];
        data[0]=0x55;
        data[1]=0x03;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);
        data[4]= (byte) press.getNumber();
        if(press.getFix1()!=0){
            data1=computePara.ComputeFloatTobyte(press.getFix1());
            System.arraycopy(data1, 0, data, 5, 3);
        }
        if(press.getFix2()!=0){
            data1=computePara.ComputeFloatTobyte(press.getFix2());
            System.arraycopy(data1, 0, data, 8, 3);
        }

        data[16]= (byte) 0xAA;
        return data;
    }
}
