package com.hust.radiofeeler.mina2FPGA.Encode;

import android.util.Log;

import com.hust.radiofeeler.Bean.PressSetting;
import com.hust.radiofeeler.GlobalConstants.Constants;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressSettingEncoder implements MessageEncoder<PressSetting> {


    @Override
    public void encode(IoSession ioSession, PressSetting press,
                       ProtocolEncoderOutput out) throws Exception {

        byte[] bytes=GetBytes(press);
        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
        Log.d("Encode", "压制参数："+ Arrays.toString(bytes));
    }

    /**
     * 压制发射参数设置数据帧
     *
     */
    private byte[] GetBytes(PressSetting press){
        byte[] data=new byte[17];
        data[0]=0x55;
        data[1]=0x08;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);
        data[4]= (byte) press.getPressMode();
        data[5]=(byte) ((press.getStyle()<<4)+press.getBand());//信号类型占高四位，信号带宽占低四位
        //四个时间的转换
        data[6]=(byte) ((press.getT1()>>8)&0xff);
        data[7]=(byte) (press.getT1()&0xff);
        data[8]=(byte) ((press.getT2()>>8)&0xff);
        data[9]=(byte) (press.getT2()&0xff);
        data[10]=(byte) ((press.getT3()>>8)&0xff);
        data[11]=(byte) (press.getT3()&0xff);
        data[12]=(byte) ((press.getT3()>>8)&0xff);
        data[13]=(byte) (press.getT3()&0xff);
        data[16]= (byte) 0xAA;
        return data;
    }
}
