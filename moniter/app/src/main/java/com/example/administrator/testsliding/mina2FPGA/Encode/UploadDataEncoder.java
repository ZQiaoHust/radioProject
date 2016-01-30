package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.UploadData;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/29.
 */
public class UploadDataEncoder implements MessageEncoder<UploadData> {
    @Override
    public void encode(IoSession ioSession, UploadData message, ProtocolEncoderOutput out) throws Exception {
        byte[] bytes=new byte[7];

        bytes[0]=0x55;
        bytes[1]= (byte) message.getFunc();
        bytes[6]= (byte) 0xaa;



        IoBuffer buffer= IoBuffer.allocate(7);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
    }


}
