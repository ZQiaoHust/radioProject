package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.IQwave;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/12/1.
 */
public class IQwaveDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte frameHead = in.get();
            if (frameHead == (byte) 0x55) {
                byte functionCode = in.get();
                if (functionCode == 0x0f) {
                    return MessageDecoderResult.OK;

                } else
                    return MessageDecoderResult.NOT_OK;

            } else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(6027);
        IQwave iQwave = new IQwave();
        int receiveCount = 0;

        while (in.hasRemaining()) {
            byte b = in.get();
            buffer.put(b);
            receiveCount++;

            if (receiveCount == 6027) {
                buffer.flip();
                byte[] accept = new byte[6027];
                buffer.get(accept);

                byte[] location=new byte[9];
                System.arraycopy(accept,4,location,0,9);
                iQwave.setLocation(location);

                byte[] time=new byte[5];
                System.arraycopy(accept,13,time,0,5);
                iQwave.setTime(time);

                byte[] para=new byte[5];
                System.arraycopy(accept,18,para,0,5);
                iQwave.setTime(para);

                iQwave.setTotalBands(accept[22]);
                iQwave.setNowNum(accept[23]);

                byte[] IQwave=new byte[6001];
                System.arraycopy(accept, 23, IQwave, 0, 6001);
                iQwave.setIQwave(IQwave);


                out.write(iQwave);
                return MessageDecoderResult.OK;

            }

        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

//    private int[] getIpots(byte[] bytes){
//        int[] Ipots=new int[2000];
//        for(int i=22,j=0;i<6020;i=+3){
//            Ipots[j]=((bytes[i]&0xf0)<<4)+bytes[i+1];
//            j++;
//        }
//
//        return Ipots;
//    }
//
//    private int[] getQpots(byte[] bytes){
//        int[] Qpots=new int[2000];
//        for(int i=22,j=0;i<46;i=+3){
//            Qpots[j]=((bytes[i]&0x0f)<<8)+bytes[i+2];
//            j++;
//        }
//
//
//        return Qpots;
//    }
//
//    private int getYear(byte[] bytes) {
//        int year;
//        year = (bytes[11] << 4) + (bytes[12] >> 4);
//        return year;
//    }
//
//    private int getMonth(byte[] bytes) {
//        int month ;
//        month = (bytes[12]&0x0f);
//        return month;
//    }
//
//    private int getDay(byte[] bytes) {
//        int day ;
//        day = (bytes[13]&0xf8);
//        return day;
//    }
//
//    private int getHour(byte[] bytes) {
//        int hour ;
//        hour = ((bytes[13] &0x07)<<3) + (bytes[14] &0x03);
//        return hour;
//    }
//
//    private int getMin(byte[] bytes) {
//        int min ;
//        min = (bytes[14] & 0xfc);
//        return min;
//    }
}
