package com.hust.radiofeeler.mina2FPGA.Decode;

import com.hust.radiofeeler.Bean.SweepRange;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/23.
 */
public class SweepRangeDecoder implements MessageDecoder {

    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if((frameHead==(byte)0x55)||(frameHead==(byte)0x66)){
                byte functionCode=in.get();
                if (functionCode==0x21){
                    return MessageDecoderResult.OK;

                }else
                    return  MessageDecoderResult.NOT_OK;

            }else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        SweepRange sweepRange =new SweepRange();
        int receiveCount=0;

        while(in.hasRemaining())
        {
            byte b=in.get();
            buffer.put(b);
            receiveCount++;

            if(receiveCount==17)
            {
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                sweepRange.setPacketHead(accept[0]);
                sweepRange.setaSweepMode((accept[4] & 0xff) >> 2);
                sweepRange.setaSendMode(accept[4] & 0x03);
                sweepRange.setaTotalOfBands(accept[5] & 0xff);

                sweepRange.setaBandNumber(accept[6] & 0xff);
                double start=Math.ceil(((accept[7]&0xff)-1)*25+70+(((accept[8]&0x03)<<8)+(accept[9]&0xff))*25/1024.0);
                sweepRange.setStartFrequence(start);
                double end=((accept[10]&0xff)-1)*25+70+(((accept[11]&0x03)<<8)+(accept[12]&0xff))*25/1024.0;
                sweepRange.setEndFrequence(end);
                sweepRange.setGate(accept[13] & 0xff >> 6);
                sweepRange.setaSelect(accept[13] & 0x3f);
                sweepRange.setContent(accept);
                out.write(sweepRange);
                return MessageDecoderResult.OK;

            }

        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
