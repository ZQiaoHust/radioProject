package com.hust.radiofeeler.mina_transmit.server2FPGADecoder;

import android.util.Log;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.SweepRangeInfo;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_FixSetting;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_SweepRange;
import com.hust.radiofeeler.bean2server.POA;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * 启动异常辐射源POA定位信令帧,中心站转发至FPGA
 * Created by Administrator on 2016/7/12
 */


public class POAsettingDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte b1 = in.get();
            byte b2 = in.get();
            if (((b1 == 0x66)||b1==0x55 )&& (b2 == 0x31)) {
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
                byte[] bytes=new byte[17];
                in.get(bytes);
                POA poa=new POA();
                poa.setContent(bytes);
                HandleSweepInfo(bytes);
                out.write(poa);
                Log.d("POAsettingDecoder", Arrays.toString(bytes));
                return MessageDecoderResult.OK;

            }
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    private void HandleSweepInfo(byte[] bytes){
        SweepRangeInfo sweepRangeInfo=new SweepRangeInfo();

        double freq1=((bytes[5]&0xff)-1)*25+(((bytes[6]&0x03)<<8)+(bytes[7]&0xff))*25/1024.0+70;
        double freq2=((bytes[8]&0xff)-1)*25+(((bytes[9]&0x03)<<8)+(bytes[10]&0xff))*25/1024.0+70;

        sweepRangeInfo.setSegStart(freq1);
        sweepRangeInfo.setSegEnd(freq2);
        sweepRangeInfo.setStartNum((bytes[5]&0xff));
        sweepRangeInfo.setEndNum((bytes[8]&0xff));

            //不是多频段扫描
            Constants.SweepParaList.clear();
            Constants.SweepParaList.add(sweepRangeInfo);
    }
}
