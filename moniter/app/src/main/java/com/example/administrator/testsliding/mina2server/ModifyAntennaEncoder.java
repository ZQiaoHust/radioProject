package com.example.administrator.testsliding.mina2server;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.ModifyAntenna;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.lang.reflect.Array;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyAntennaEncoder implements MessageEncoder<ModifyAntenna> {
    @Override
    public void encode(IoSession ioSession, ModifyAntenna modify,
                       ProtocolEncoderOutput out) throws Exception {
        if(modify!=null){
            IoBuffer buffer=IoBuffer.allocate(9);
            byte[] bytes=new byte[9];
            bytes[0]=0x55;
            bytes[1]= (byte) 0xC2;
            bytes[2]= (byte) (modify.getEquipmentID()&0xff);
            bytes[3]= (byte) ((modify.getEquipmentID()>>8)&0xff);
            bytes[4]= (byte) ((modify.getFPGAID()>>8)&0xff);
            bytes[5]= (byte) (modify.getFPGAID()&0xff);
            bytes[8]= (byte) 0xAA;

            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);


        }

    }
}
