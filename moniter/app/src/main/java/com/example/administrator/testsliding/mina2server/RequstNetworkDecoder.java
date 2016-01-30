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
        byte [] bytes=null ;
        byte[] byte1=new byte[4];
        byte[] byte2=new byte[2];
        in.get(bytes);
        reply.setEquipmentID((bytes[2] & 0xff) + ((bytes[3] & 0xff) << 8));
        reply.setIsagreen(bytes[4]);
        reply.setStyle(bytes[5]);
        //取经度
        System.arraycopy(bytes,6,byte1,0,4);
        reply.setLongtitude(byte1);

        System.arraycopy(bytes, 10, byte1, 0, 4);
        reply.setLatitude(byte1);

        System.arraycopy(bytes,2,byte2,0,2);
        reply.setHeight(byte1);

        out.write(reply);
        return MessageDecoderResult.OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
