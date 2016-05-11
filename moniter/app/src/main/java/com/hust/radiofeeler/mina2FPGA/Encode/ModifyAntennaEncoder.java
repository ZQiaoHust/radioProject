package com.hust.radiofeeler.mina2FPGA.Encode;

import com.hust.radiofeeler.bean2server.File_ModifyAntenna;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyAntennaEncoder implements MessageEncoder<File_ModifyAntenna> {
    @Override
    public void encode(IoSession ioSession, File_ModifyAntenna modify,
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
