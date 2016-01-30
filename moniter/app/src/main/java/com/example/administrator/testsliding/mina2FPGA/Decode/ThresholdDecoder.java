package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.Threshold;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/24.
 */
public class ThresholdDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if(frameHead==(byte)0x55){
                byte functionCode=in.get();
                if (functionCode==0x26){
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
        Threshold threshold=new Threshold();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b =in.get();
            buffer.put(b);
            receiveCount++;
            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                threshold.setThresholdModel(accept[4]);
                threshold.setAutoThreshold(accept[5]);
                threshold.setFixThreshold(getFixTheshold(accept));

                out.write(threshold);

                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    private int getFixTheshold(byte[] bytes){

        int fixThreshold;
        fixThreshold=(bytes[6]<<24)+(bytes[7]<<16)+(bytes[8]<<8)+bytes[9];
        return fixThreshold;
    }
}
