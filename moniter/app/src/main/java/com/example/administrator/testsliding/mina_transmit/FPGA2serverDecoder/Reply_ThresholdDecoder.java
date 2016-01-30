package com.example.administrator.testsliding.mina_transmit.FPGA2serverDecoder;

import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_OutGain;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Threshold;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_ThresholdDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else {
            byte b1=in.get();
            byte b2=in.get();
            if((b1==0x66)&&(b2==0x26)){
                return MessageDecoderResult.OK;
            }else
                return  MessageDecoderResult.NOT_OK;
        }

    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {

        int hasCount=0;
        while (in.hasRemaining()){
            hasCount++;
            if(hasCount==17){
                 byte bytes[]=new byte[17];
                in.get(bytes);
                Reply_Threshold fix=new Reply_Threshold();
                fix.setContent(bytes);
                out.write(fix);
                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
