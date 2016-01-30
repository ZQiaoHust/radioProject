package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.FixSetting;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/24.
 */
public class FixSettingDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte frameHead = in.get();
            if (frameHead == (byte) 0x55) {
                byte functionCode = in.get();
                if (functionCode == 0x27) {
                    return MessageDecoderResult.OK;

                } else
                    return MessageDecoderResult.NOT_OK;

            } else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(17);
        FixSetting fixSetting = new FixSetting();
        int receiveCount = 0;

        while (in.hasRemaining()) {
            byte b = in.get();
            buffer.put(b);
            receiveCount++;
            if (receiveCount == 17) {
                buffer.flip();
                byte[] accept = new byte[17];
                buffer.get(accept);
                fixSetting.setIQwidth(getIQwidth(accept));
                fixSetting.setBlockNum(accept[5]);
                fixSetting.setYear(getYear(accept));
                fixSetting.setDay(getDay(accept));
                fixSetting.setMonth(getMonth(accept));
                fixSetting.setHour(getHour(accept));
                fixSetting.setMinute(getMin(accept));
                fixSetting.setSecond(getSecond(accept));

                out.write(fixSetting);
                return MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


    /**
     * IQ复信号带宽和复信号数据率转换编码
     *
     * @param bytes
     * @return
     */

    private double getIQwidth(byte[] bytes) {
        double IQwidth = 0;
        switch (bytes[4]) {
            case 0x11:
                IQwidth = 5;
                break;
            case 0x22:
                IQwidth = 2.5;
                break;
            case 0x33:
                IQwidth = 1;
                break;
            case 0x44:
                IQwidth = 0.5;
                break;
            case 0x55:
                IQwidth = 0.1;
                break;
        }
        return IQwidth;

    }

    private int getYear(byte[] bytes) {
        int year;
        year = (bytes[6] << 4) + (bytes[7] >> 4);
        return year;
    }

    private int getMonth(byte[] bytes) {
        int month ;
        month = (bytes[7]&0x0f);
        return month;
    }

    private int getDay(byte[] bytes) {
        int day ;
        day = (bytes[8]&0xf8);
        return day;
    }

    private int getHour(byte[] bytes) {
        int hour ;
        hour = ((bytes[8] &0x07)<<3) + (bytes[9] &0x03);
        return hour;
    }

    private int getMin(byte[] bytes) {
        int min ;
        min = (bytes[9] & 0xfc);
        return min;
    }

    private int getSecond(byte[] bytes) {
        int second ;
        second = (bytes[10])&0xff;
        return second;
    }
}
