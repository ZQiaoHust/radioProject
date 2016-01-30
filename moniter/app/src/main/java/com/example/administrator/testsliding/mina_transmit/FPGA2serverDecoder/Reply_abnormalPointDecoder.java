package com.example.administrator.testsliding.mina_transmit.FPGA2serverDecoder;

import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Spectrum;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_abnormalPoint;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/12/3.
 */
public class Reply_abnormalPointDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else {
            byte b1=in.get();
            byte b2=in.get();
            if(b1==0x66&&(b2==0x0E)){
                return MessageDecoderResult.OK;
            }else {
                return  MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        int hascount=0;

        while (in.hasRemaining()){
            hascount++;
            if(hascount==53){
                byte[] bytes=new byte[53];
                Reply_abnormalPoint point=new Reply_abnormalPoint();
                point=byte2Object(bytes);
                if (point!=null) {
                    out.write(point);
                }
            }
        }
        return null;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


    private Reply_abnormalPoint byte2Object(byte[] bytes){
        Reply_abnormalPoint point =new Reply_abnormalPoint();
        if(bytes.length>=53) {
            point.setBandNum((bytes[18] & 0xff));
            point.setNum((bytes[19] & 0xff));
            byte[] b2 = new byte[30];
            System.arraycopy(bytes, 20, b2, 0, 30);
           point.setPower(b2);
        }
        return point;
    }
}
