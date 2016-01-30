package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.Send_ServiceRadio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/16.
 */
public class RequestWirlessPlanEncode2 extends ProtocolEncoderAdapter {

    @Override
    public void encode(IoSession ioSession, Object message, ProtocolEncoderOutput out)
            throws Exception {
        Send_ServiceRadio radio = null;
        if (message instanceof Send_ServiceRadio) {
            radio = (Send_ServiceRadio) message;
        }
        if (radio != null) {
            byte[] bytes1 = GetBytes(radio);
            int capacity = bytes1.length;
            IoBuffer buffer = IoBuffer.allocate(capacity, false);
            buffer.setAutoExpand(true);
            buffer.put(bytes1);
            buffer.flip();
            out.write(buffer);
            Log.d("axy", "发送成功:" + capacity);
            Log.d("axy", Arrays.toString(bytes1));
        }

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }

    /**
     * 国家无线电频率规划查询
     *
     * @return
     */
    private byte[] GetBytes(Send_ServiceRadio radio) {

        byte[] data = new byte[13];
        data[0] = 0x55;
        data[1] = (byte) 0xA7;
        data[2] = (byte) (radio.getEquipmentID() & 0xff);//设备ID号的低8位
        data[3] = (byte) ((radio.getEquipmentID()>> 8) & 0xff);//设备ID号的高位
        data[4] = (byte) ((radio.getStartFrequency() >> 16) & 0xff);
        data[5] = (byte) ((radio.getStartFrequency() >> 8) & 0xff);//整数部分高八位
        data[6] = (byte) (radio.getStartFrequency()& 0xff);
        data[7] = (byte) ((radio.getEndFrequency() >> 16) & 0xff);
        data[8] = (byte) ((radio.getEndFrequency() >> 8) & 0xff);//整数部分高八位
        data[9] = (byte) (radio.getEndFrequency()  & 0xff);
        data[12] = (byte) 0xAA;
        return data;
    }
}
