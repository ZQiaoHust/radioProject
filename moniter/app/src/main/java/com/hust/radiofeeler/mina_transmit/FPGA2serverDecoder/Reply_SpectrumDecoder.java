package com.hust.radiofeeler.mina_transmit.FPGA2serverDecoder;

import com.hust.radiofeeler.bean2Transmit.FPGA2server.Reply_Spectrum;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/12/3.
 */
public class Reply_SpectrumDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else {
            byte b1=in.get();
            byte b2=in.get();
            if((b1==0x66)&&(b2==0x0D||b2==0x1D)){
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
            if(hascount==1560){
                byte[] bytes=new byte[1560];
                Reply_Spectrum spec=new Reply_Spectrum();
                spec=byte2Object(bytes);
                if (spec!=null) {
                    out.write(spec);
                }
            }
        }
        return null;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


    private Reply_Spectrum byte2Object(byte[] bytes){
        Reply_Spectrum spe =new Reply_Spectrum();
        if(bytes.length>=1560) {
            spe.setFunctionID(bytes[1]);
            //位置信息和时间
            byte[] b1 = new byte[14];
            System.arraycopy(bytes, 4, b1, 0, 14);
            spe.setLocation(b1);
            //时间
//            int year = ((bytes[13] & 0xff) << 4) + ((bytes[14] >> 4) & 0xff);
//            spe.setYear(year);
//            int month = bytes[14] & 0x0f;
//            spe.setMonth(month);
//            int day = ((bytes[15] >> 3) & 0xff);
//            spe.setHour(day);
//            int hour = (((bytes[15] & 0x07) << 2) + bytes[16] & 0x03) & 0xff;
//            spe.setHour(hour);
//            int min = ((bytes[16] >> 2) & 0xff);
//            spe.setMinite(min);
//            spe.setMius((bytes[17] & 0xff));

            spe.setSweepModel(((bytes[18] >> 6) & 0xff));
            spe.setFileSendmodel(((bytes[18] >> 4) & 0x03));
            spe.setIsChange((bytes[18] & 0x0f));
            spe.setTotalBand((bytes[19] & 0xff));
            spe.setBandNum((bytes[19] & 0xff));
            byte[] b3 = new byte[3];
            System.arraycopy(bytes, 18, b3, 0, 3);
            spe.setSweepModel2bandNum(b3);
            byte[] b2 = new byte[1536];
            System.arraycopy(bytes, 21, b2, 0, 1536);
            spe.setPower(b2);
        }
        return spe;
    }
}
