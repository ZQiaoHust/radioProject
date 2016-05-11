package com.hust.radiofeeler.mina2FPGA.Decode;

import com.hust.radiofeeler.Bean.FixCentralFreq;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/23.
 */
public class FixCentralFreqDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if((frameHead==(byte)0x55)||(frameHead==(byte)0x66)){
                byte functionCode=in.get();
                if (functionCode==0x22){
                    return MessageDecoderResult.OK;

                }else
                    return  MessageDecoderResult.NOT_OK;

            }else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        FixCentralFreq fixCentralFreq=new FixCentralFreq();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b =in.get();
            buffer.put(b);
            receiveCount++;
            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                fixCentralFreq.setPacketHead(accept[0]);
                fixCentralFreq.setNumber(accept[4]);
                fixCentralFreq.setFix1(getCentralFreq(accept,5,6,7));
                fixCentralFreq.setFix2(getCentralFreq(accept,8,9,10));
                fixCentralFreq.setFix3(getCentralFreq(accept,11,12,13));
                fixCentralFreq.setContent(accept);
                out.write(fixCentralFreq);

                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    //计算中心频率
    private double getCentralFreq(byte[] bytes,int a,int b,int c){
        int zhenshu=(bytes[a]<<6)+(bytes[b]&0x3f);
        double xiaoshu=(bytes[b]>>6)/4.0+((bytes[c]&0xff)/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }

}
