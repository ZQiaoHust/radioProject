package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.Press;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if(frameHead==0x55){
                byte functionCode=in.get();
                if(functionCode==0x23){
                    return MessageDecoderResult.OK;
                }else{
                    return MessageDecoderResult.NOT_OK;
                }
            }else{
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        Press press=new Press();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b=in.get();
            buffer.put(b);
            receiveCount++;

            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                press.setNumber(accept[4]);
                press.setFix1(getPressFreq1(accept));
                press.setFix2(getPressFreq2(accept));

                out.write(press);

                return MessageDecoderResult.OK;
            }
        }

        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    //计算第一个中心频率
    private double getPressFreq1(byte[] bytes){
        int zhenshu=(bytes[5]<<6)+(bytes[6]&0x3f);
        double xiaoshu=(bytes[6]>>6)/4.0+(bytes[7]/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }

    //计算第二个中心频率
    private double getPressFreq2(byte[] bytes){
        int zhenshu=(bytes[8]<<6)+(bytes[9]&0x3f);
        double xiaoshu=(bytes[9]>>6)/4.0+(bytes[10]/1024.0);
        double centralFreq=zhenshu+xiaoshu;
        return centralFreq;
    }
}
