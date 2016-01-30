package com.example.administrator.testsliding.mina2FPGA.Decode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.InGain;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/18.
 */
public class InGainDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        Log.d("abcd", "尝试ingain解码器");
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {

            in.position(1);
            byte b = in.get();
            if (b == 0x24) {
                return MessageDecoderResult.OK;
            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        InGain inGain = new InGain();
        IoBuffer buffer = IoBuffer.allocate(17);
        int receiveCount = 0;

        while (in.hasRemaining()) {

            receiveCount++;
            byte b = in.get();
            buffer.put(b);

            if (receiveCount == 17) {
                buffer.flip();
                byte[] accept = new byte[17];
                buffer.get(accept);
                inGain.setIngain(accept[4] & 0xff);
                out.write(inGain);
                return MessageDecoderResult.OK;
            }

        }

        return MessageDecoderResult.NOT_OK;


    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
