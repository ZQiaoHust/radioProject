package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.Threshold;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.packet.SettoFPGAPacket;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class ThresholdEncoder implements MessageEncoder<Threshold> {

    @Override
    public void encode(IoSession ioSession, Threshold message, ProtocolEncoderOutput out) throws Exception {

        byte[] bytes=GetBytes(message);
        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);

    }
    /**
     * 设置设置检测门限的包
     */
    private byte[] GetBytes(Threshold message){

        byte[] data=new byte[17];
        data[0] = 0x55;
        data[1] = 0x06;
        data[2] = (byte) (Constants.ID & 0xff);//设备ID号
        data[3] = (byte) ((Constants.ID >> 8) & 0xff);
        data[4]= (byte) message.getThresholdModel();
        data[5]= FixThresholdChangeToCode(message.getAutoThreshold());//门限类型和自适应门限

        //固定门限转换（固定门限的值不会有16bit）
        data[6] = (byte) ((message.getFixThreshold()>> 8) & 0xff);//固定门限高八位，最高位符号位为0
        data[7]=(byte) (message.getFixThreshold()& 0xff);
        data[16]= (byte) 0xAA;
        return data;

    }
    /**
     * 获取自适应门限的编码
     * @param threshold 自适应门限值
     * @return
     */

    private byte FixThresholdChangeToCode(int threshold){
        byte code=0;

        switch(threshold)
        {
            case 3:
                code = 0x00000000;
                break;
            case 10:
                code = 0x00000001;
                break;
            case 20:
                code = 0x00000002;
                break;
            case 25:
                code = 0x00000003;
                break;
            case 30:
                code = 0x00000004;
                break;
            case 40:
                code = 0x00000005;
                break;
            default:
                break;
        }
        return code;

    }

}
