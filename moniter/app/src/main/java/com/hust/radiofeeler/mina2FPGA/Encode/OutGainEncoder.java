package com.hust.radiofeeler.mina2FPGA.Encode;

import com.hust.radiofeeler.Bean.OutGain;
import com.hust.radiofeeler.GlobalConstants.Constants;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by jinaghao on 15/11/23.
 */
public class OutGainEncoder implements MessageEncoder<OutGain> {

    @Override
    public void encode(IoSession ioSession, OutGain gain,
                       ProtocolEncoderOutput out) throws Exception {

        if (gain != null) {
            byte[] bytes = GetBytes(gain);

            IoBuffer buffer = IoBuffer.allocate(17);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }
    }

    private byte[] GetBytes(OutGain gain) {
        byte[] data = new byte[17];
        data[0] = 0x55;
        data[1] = 0x05;
        data[2] = (byte) (Constants.ID & 0xff);//设备ID号
        data[3] = (byte) ((Constants.ID >> 8) & 0xff);
        data[4] = (byte) gain.getOutGain();

        data[16] = (byte) 0xAA;
        return data;
    }
}
