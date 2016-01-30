package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.FixCentralFreq;

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
            if(frameHead==(byte)0x55){
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
                fixCentralFreq.setNumber(accept[4]);
                fixCentralFreq.setFix1(getCentralFreq1(accept));
                fixCentralFreq.setFix2(getCentralFreq2(accept));
                fixCentralFreq.setFix3(getCentralFreq3(accept));

                out.write(fixCentralFreq);

                return  MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    //计算第一个中心频率
    private double getCentralFreq1(byte[] bytes){
        int zhenshu=(bytes[5]<<6)+(bytes[6]&0x3f);
        double xiaoshu=(bytes[6]>>6)/4.0+((bytes[7]&0xff)/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }
    //计算第二个中心频率
    private double getCentralFreq2(byte[] bytes){
        int zhenshu=(bytes[8]<<6)+(bytes[9]&0x3f);
        double xiaoshu=(bytes[9]>>6)/4.0+((bytes[10]&0xff)/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }
    //计算第三个中心频率
    private double getCentralFreq3(byte[] bytes){
        int zhenshu=(bytes[11]<<6)+(bytes[12]&0x3f);
        double xiaoshu=(bytes[12]>>6)/4.0+((bytes[13]&0xff)/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }
}
