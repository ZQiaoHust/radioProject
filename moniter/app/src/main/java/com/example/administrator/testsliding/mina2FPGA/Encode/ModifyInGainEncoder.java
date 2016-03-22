package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.bean2server.File_ModifyIngain;
import com.example.administrator.testsliding.bean2server.ModifyInGain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyInGainEncoder implements MessageEncoder<File_ModifyIngain> {
    @Override
    public void encode(IoSession ioSession, File_ModifyIngain modify,
                       ProtocolEncoderOutput out) throws Exception {
        if(modify!=null){
            IoBuffer buffer=IoBuffer.allocate(100,true);
            byte[] bytes=modify.getFileContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);

        }

    }
}
