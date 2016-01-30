package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.UploadData;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/29.
 */
public class UploadDataDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if(frameHead==(byte)0x55){
                byte functionCode=in.get();
                if (functionCode==0x2A){
                    return MessageDecoderResult.OK;

                }else
                    return  MessageDecoderResult.NOT_OK;

            }else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(7);
        UploadData uploadData =new UploadData();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b =in.get();
            buffer.put(b);
            receiveCount++;
            if(receiveCount==7){
                buffer.flip();
                byte[] accept=new byte[7];
                buffer.get(accept);
                uploadData.setFunc(accept[1]);
                out.write(uploadData);

                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
