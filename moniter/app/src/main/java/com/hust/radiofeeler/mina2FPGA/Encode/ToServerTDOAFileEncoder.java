package com.hust.radiofeeler.mina2FPGA.Encode;

import com.hust.radiofeeler.Bean.ToServerPOAFile;
import com.hust.radiofeeler.Bean.ToServerTDOAFile;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;


/**
 * Created by jinaghao on 16/1/13.
 */
public class ToServerTDOAFileEncoder implements MessageEncoder<ToServerTDOAFile> {


    @Override
    public void encode(IoSession ioSession,
                       ToServerTDOAFile message,
                       ProtocolEncoderOutput out) throws Exception {
        CharsetEncoder ce = Charset.forName("UTF-8").newEncoder();

        IoBuffer buffer= IoBuffer.allocate(6000).setAutoExpand(true);
        buffer.put((byte)0x00);
        buffer.put((byte)0xff);
        buffer.putShort((short) message.getFileName().getBytes(Charset.forName("UTF-8")).length);
        buffer.putLong((long) message.getContentLength());
        buffer.putString(message.getFileName(), ce);
        buffer.put(message.getContent());
        buffer.flip();
        out.write(buffer);

    }
}
