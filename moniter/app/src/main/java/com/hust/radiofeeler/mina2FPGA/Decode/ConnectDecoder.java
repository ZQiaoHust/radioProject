package com.hust.radiofeeler.mina2FPGA.Decode;

import com.hust.radiofeeler.Bean.Connect;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/30.
 */
public class ConnectDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if((frameHead==(byte)0x55)||(frameHead==(byte)0x66)){
                byte functionCode=in.get();
                if (functionCode==0x29){
                    return MessageDecoderResult.OK;

                }else
                    return  MessageDecoderResult.NOT_OK;

            }else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        Connect connect =new Connect();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b =in.get();
            buffer.put(b);
            receiveCount++;
            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                connect.setPacketHead(accept[0]);
                connect.setConn(accept[4]);
                connect.setContent(accept);
                out.write(connect);


                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
