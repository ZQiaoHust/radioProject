package com.example.administrator.testsliding.mina2FPGA.Encode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.SweepRange;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by jinaghao on 15/11/23.
 */
public class SweepRangeEncoder implements MessageEncoder<SweepRange> {

    private ComputePara computePara=new ComputePara();
    @Override
    public void encode(IoSession ioSession, SweepRange sweep,
                       ProtocolEncoderOutput out) throws Exception {

        byte[] bytes=GetBytes(sweep);
        IoBuffer buffer = IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
        Log.d("Encode","扫频："+ Arrays.toString(bytes));

    }
    private byte[] GetBytes(SweepRange sweep){
        byte[] data=new byte[17];
        int startOffset=computePara.ComputeSegOffset(sweep.getStartFrequence());//起点段内偏移量，偏移量最多只占10位
        int endOffset=computePara.ComputeSegOffset(sweep.getEndFrequence());
        data[0]=0x55;
        data[1]=0x01;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);

        data[4]= (byte) ((sweep.getaSweepMode()<<2)+sweep.getaSendMode());     //扫频模式+长传模式
        data[5]= (byte) sweep.getaTotalOfBands();//针对离散和多频段扫频的频段总数(N)
        data[6]= (byte) sweep.getaBandNumber();//频段序号(1~N)

        data[7]= (byte) computePara.ComputeSegNumber(sweep.getStartFrequence());
        //偏移量至多只占10bit，因此只取前两个字节即可
        data[8]=(byte) ((startOffset >> 8) & 0xff); //偏移量高位，
        data[9]=(byte) (startOffset & 0xff);//偏移量低8位

        data[10]= (byte) computePara.ComputeSegNumber(sweep.getEndFrequence());
        data[11]=(byte) ((endOffset >> 8 )& 0xff); //偏移量高位，
        data[12]=(byte) (endOffset & 0xff);//偏移量低8位
        data[13]= (byte) ((sweep.getGate()<<6)+sweep.getaSelect());//判定门限+抽取倍率
        data[16]= (byte) 0xAA;
        return data;

    }
}
