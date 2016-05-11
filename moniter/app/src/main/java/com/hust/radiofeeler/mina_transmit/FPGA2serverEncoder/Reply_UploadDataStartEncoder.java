package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.Bean.UploadData;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_UploadDataStartEncoder implements MessageEncoder<UploadData> {
    @Override
    public void encode(IoSession ioSession, UploadData data,
                       ProtocolEncoderOutput out) throws Exception {

        if(data!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
            byte[] bytes=data.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
