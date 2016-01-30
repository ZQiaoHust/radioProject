package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.Query;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/18.
 */
public class QueryEncoder implements MessageEncoder<Query> {
    @Override
    public void encode(IoSession ioSession, Query message,
                       ProtocolEncoderOutput out) throws Exception {

        byte[] b=new byte[7];

        b[0]=0x55;
        b[1]=message.getFuncID();
        b[2]= (byte) (message.getequipmentID()&0xff);
        b[3]= (byte) ((message.getequipmentID()>>8)&0xff);
        b[4]=0;
        b[5]=0;
        b[6]= (byte) 0xAA;


        IoBuffer buffer = IoBuffer.allocate(7);
        buffer.put(b);
        buffer.flip();
        out.write(buffer);

    }
}
