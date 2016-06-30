package com.hust.radiofeeler.mina_transmit.server2FPGADecoder;

import android.util.Log;

import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.SweepRangeInfo;
import com.hust.radiofeeler.bean2Transmit.server2FPGASetting.Simple_SweepRange;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Simple_SweepRangeDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else {
            byte b1=in.get();
            byte b2=in.get();
            if((b1==0x66)&&(b2==0x01)){
                return MessageDecoderResult.OK;
            }else
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
               Simple_SweepRange sweep=new Simple_SweepRange();
               sweep.setContent(bytes);
               HandleSweepInfo(bytes);
               out.write(sweep);
               Log.d("xyz", Arrays.toString(bytes));
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
        int sendMode=bytes[4]&0x03;//文件上传模式
        Constants.sendMode=sendMode;
        int total=bytes[5]&0xff;
        int select=bytes[13]&0x3f;
        Constants.selectRate=select;
        double freq1=((bytes[7]&0xff)-1)*25+(((bytes[8]&0x03)<<8)+(bytes[9]&0xff))*25/1024.0+70;
        double freq2=((bytes[10]&0xff)-1)*25+(((bytes[11]&0x03)<<8)+(bytes[12]&0xff))*25/1024.0+70;

        sweepRangeInfo.setSegStart(freq1);
        sweepRangeInfo.setSegEnd(freq2);
        sweepRangeInfo.setStartNum((bytes[7]&0xff));
        sweepRangeInfo.setEndNum((bytes[10]&0xff));
        if(total==1){
            //不是多频段扫描
            Constants.SweepParaList.clear();
            Constants.SweepParaList.add(sweepRangeInfo);
        }else {
            //多频段扫描
            if(Constants.serverSweepCount==0){
                //第一次
                Constants.SweepParaList.clear();
                Constants.serverSweepCount++;
            }else {
                if(Constants.serverSweepCount==total){
                    //最后一次
                    Constants.serverSweepCount=0;
                }else {
                    Constants.serverSweepCount++;
                }
            }
            Constants.SweepParaList.add(sweepRangeInfo);
        }

    }
}
