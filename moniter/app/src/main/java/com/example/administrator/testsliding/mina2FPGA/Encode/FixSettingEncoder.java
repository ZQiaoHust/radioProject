package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.FixSetting;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.compute.ComputePara;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/24.
 */
public class FixSettingEncoder implements MessageEncoder<FixSetting> {
    private ComputePara computePara=new ComputePara();
    @Override
    public void encode(IoSession ioSession, FixSetting fixsetting, ProtocolEncoderOutput out) throws Exception {

        if(fixsetting!=null) {
            IoBuffer buffer = IoBuffer.allocate(17);
            byte[] bytes=GetBytes(fixsetting);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Constants.sequenceID=0;
        }
    }
    private  byte[] GetBytes(FixSetting fixsetting){
        byte[] data=new byte[17];
        data[0]=0x55;
        data[1]=0x07;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);
        data[4]=IQtoCode((int) (fixsetting.getIQwidth()*10));//带宽和数据率
        data[5]= (byte) fixsetting.getBlockNum();//数据块
        byte[] data1=computePara.Time2Bytes(fixsetting.getTimeString());//时间
        System.arraycopy(data1, 0, data, 6, 5);
        data[16]= (byte) 0xAA;
        return data;

    }
    private byte IQtoCode(int index){
        byte code=0;
        switch(index){
            case 50:
                code=0x11;
                break;
            case 25:
                code=0x22;
                break;
            case 10:
                code=0x33;
                break;
            case 5:
                code=0x44;
                break;
            case 1:
                code=0x55;
                break;
            default:
                break;
        }
        return code;

    }
}
