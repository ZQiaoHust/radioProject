package com.hust.radiofeeler.mina_transmit.server2FPGADecoder;

import android.util.Log;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_FixCentralFreq;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * Created by Administrator on 2016/3/28.
 */


public class FileSettingFixCentralDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte b1 = in.get();
            byte b2 = in.get();
            if ((b1 == 0x66) && (b2 == 0x41)) {
                return MessageDecoderResult.OK;
            } else
                return MessageDecoderResult.NOT_OK;
        }

    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {

        int hasCount=0;
        while (in.hasRemaining()){
            hasCount++;
            if(hasCount==19){
                byte[] bytes=new byte[19];
                in.get(bytes);
                byte[] data=new byte[17];
                data[0]=bytes[0];
                data[1]=0x02;
                System.arraycopy(bytes,2,data,2,12);
                data[14]=bytes[16];
                data[15]=bytes[17];
                data[16]=bytes[18];
                Constants.sequenceID=(bytes[14]&0xff)+((bytes[15]&0xff)<<8);//任务序列号
                Simple_FixCentralFreq fixCentral=new Simple_FixCentralFreq();
                fixCentral.setContent(data);
                out.write(fixCentral);
                Log.d("xyz", Arrays.toString(bytes));
                Log.d("xyz", Arrays.toString(data));
                return MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
