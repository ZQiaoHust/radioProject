package com.example.administrator.testsliding.mina2server;

import com.example.administrator.testsliding.bean2server.RequstNetworkReply;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetworkDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
        if(ioBuffer.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }
        else{
            byte b1=ioBuffer.get();
            byte b=ioBuffer.get();
            if ((b1==0x55)&&(b==(byte)0xB1)){
                return  MessageDecoderResult.OK;
            }
            else{
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        RequstNetworkReply reply=new RequstNetworkReply();
        IoBuffer buffer = IoBuffer.allocate(18);
        int receiveCount = 0;

        while (in.hasRemaining()) {
            byte b = in.get();
            buffer.put(b);
            receiveCount++;
            if (receiveCount == 18) {
                buffer.flip();
                byte [] bytes=new byte[18] ;
                buffer.get(bytes);
                reply.setIsagreen(bytes[4]);
                out.write(reply);
                return MessageDecoderResult.OK;

            }
        }
        return MessageDecoderResult.NOT_OK;


    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
