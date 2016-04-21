package com.example.administrator.testsliding.mina2server.Decoder;

import com.example.administrator.testsliding.bean2server.FileToServerReply;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by Administrator on 2016/4/19.
 */
public class FileToServerReplyDecoder implements MessageDecoder{
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }
        else{
            byte b1=in.get();
            byte b2=in.get();
            if((b1==(byte)0xff)&&(b2==0x00)){
                return MessageDecoderResult.OK;
            }
            else {
                return  MessageDecoderResult.NOT_OK;
            }
        }

    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        CharsetDecoder ce = Charset.forName("UTF-8").newDecoder();

        IoBuffer buffer = IoBuffer.allocate(50).setAutoExpand(true);
        if (in.hasRemaining()) {
            in.position(2);
            short b = in.getShort();//文件名长度
            FileToServerReply reply=new FileToServerReply();
            String name= in.getString(b, ce);
            reply.setFileName(name);
            out.write(reply);
            return MessageDecoderResult.OK;

        }
        return MessageDecoderResult.NOT_OK;

    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
