package com.hust.radiofeeler.mina_transmit.server2FPGADecoder;

import android.util.Log;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.SweepRangeInfo;
import com.hust.radiofeeler.bean2server.POA;
import com.hust.radiofeeler.bean2server.TDOA;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * 启动异常辐射源TDOA定位信令帧,中心站转发至FPGA
 * 与POA共用一个对象
 * Created by Administrator on 2016/7/12
 */


public class TDOAsettingDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte b1 = in.get();
            byte b2 = in.get();
            if (((b1 == 0x66)||b1==0x55 )&& (b2 == 0x32)) {
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
            if(hasCount==17){
                byte[] bytes=new byte[19];
                in.get(bytes);
                TDOA td=new TDOA();//余POA公用
                td.setContent(bytes);
                out.write(td);
                Log.d("TDOAsettingDecoder", Arrays.toString(bytes));
                return MessageDecoderResult.OK;

            }
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


}
