package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.Station_RegisterRequst;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/26.
 */
public class StationRegisterEncoder implements MessageEncoder<Station_RegisterRequst> {
    @Override
    public void encode(IoSession session, Station_RegisterRequst station, ProtocolEncoderOutput out)
            throws Exception {


        if(station!=null){
            byte[] bytes1 = GetBytes(station);
            IoBuffer buffer=IoBuffer.allocate(11);
            buffer.put(bytes1);
            buffer.flip();
            out.write(buffer);
            Log.d("register", Arrays.toString(bytes1));
        }

    }


    /**
     * 台站登记属性
     *
     * @return
     */
    private byte[] GetBytes( Station_RegisterRequst station) {

        byte[] data = new byte[11];
        data[0] = 0x55;
        data[1] = (byte) 0xA5;
        data[2] = (byte) (station.getEquipmentID() & 0xff);//设备ID号的低8位
        data[3] = (byte) ((station.getEquipmentID()>> 8) & 0xff);//设备ID号的高位

        data[4] = (byte) ((station.getStartFreq() >> 8) & 0xff);//整数部分高八位
        data[5] = (byte) (station.getStartFreq()& 0xff);
        data[6] = (byte) ((station.getEndFreq() >> 8) & 0xff);//整数部分高八位
        data[7] = (byte) (station.getEndFreq()  & 0xff);
        data[10] = (byte) 0xAA;
        return data;
    }
}
