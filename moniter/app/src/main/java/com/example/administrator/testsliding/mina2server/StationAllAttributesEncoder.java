package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.TerminalAttributes_All;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/24.
 */
public class StationAllAttributesEncoder implements MessageEncoder<TerminalAttributes_All> {
    @Override
    public void encode(IoSession session, TerminalAttributes_All terminal, ProtocolEncoderOutput out)
            throws Exception {

        if(terminal!=null){
            byte[] bytes1 = GetBytes(terminal);
//            int capacity = bytes1.length;
//            IoBuffer buffer = IoBuffer.allocate(capacity, false);
//            buffer.setAutoExpand(true);
            IoBuffer buffer=IoBuffer.allocate(11);
            buffer.put(bytes1);
            buffer.flip();
            out.write(buffer);
            Log.d("axy", Arrays.toString(bytes1));
        }

    }


    /**
     * 全部台站属性
     *
     * @return
     */
    private byte[] GetBytes(TerminalAttributes_All terminal) {

        byte[] data = new byte[11];
        data[0] = 0x55;
        data[1] = (byte) 0xA8;
        data[2] = (byte) (terminal.getEquipmentID() & 0xff);//设备ID号的低8位
        data[3] = (byte) ((terminal.getEquipmentID()>> 8) & 0xff);//设备ID号的高位

        data[4] = (byte) ((terminal.getStartFreq() >> 8) & 0xff);//整数部分高八位
        data[5] = (byte) (terminal.getStartFreq()& 0xff);
        data[6] = (byte) ((terminal.getEndFreq() >> 8) & 0xff);//整数部分高八位
        data[7] = (byte) (terminal.getEndFreq()  & 0xff);
        data[10] = (byte) 0xAA;
        return data;
    }

}



