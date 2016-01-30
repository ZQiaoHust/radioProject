package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.PressSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/25.
 */
public class PressSettingDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if(frameHead==0x55){
                byte functionCode=in.get();
                if(functionCode==0x28){
                    return MessageDecoderResult.OK;
                }else
                    return MessageDecoderResult.NOT_OK;
            }
            return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        PressSetting pressSetting=new PressSetting();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b=in.get();
            buffer.put(b);
            receiveCount++;

            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                pressSetting.setPressMode(accept[4]);
                pressSetting.setStyle(accept[5] >> 4);
                pressSetting.setBand(accept[5] & 0x0f);
                pressSetting.setT1((accept[6] << 8) + accept[7]);
                pressSetting.setT2((accept[8] << 8) + accept[9]);
                pressSetting.setT3((accept[10] << 8) + accept[11]);
                pressSetting.setT4((accept[12] << 8) + accept[13]);

                out.write(pressSetting);
//                Constants.FPGAsession.write()

                return MessageDecoderResult.NOT_OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
