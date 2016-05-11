package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.Bean.StationState;
import com.hust.radiofeeler.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * Created by jinaghao on 15/11/25.
 */
public class StationStateDecoder implements MessageDecoder {
    private ComputePara computePara=new ComputePara();
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte frameHead = in.get();
            if ((frameHead == (byte) 0x55) || (frameHead == (byte) 0x66)) {
                byte functionCode = in.get();
                if (functionCode == 0x2c) {
                    return MessageDecoderResult.OK;
                } else
                    return MessageDecoderResult.NOT_OK;
            }
            return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer = IoBuffer.allocate(17);
        StationState stationState = new StationState();
        int receiveCount = 0;

        while (in.hasRemaining()) {
            byte b = in.get();
            buffer.put(b);
            receiveCount++;

            if (receiveCount == 17) {
                buffer.flip();
                byte[] accept = new byte[17];
                buffer.get(accept);
                stationState.setPacketHead(accept[0]);
                stationState.setEquipmentID((accept[2] & 0xff) + ((accept[3] & 0xff) << 8));
                stationState.setOnNet(accept[4]);
                stationState.setModel(accept[5]);
                stationState.setEastORwest(accept[6]);
                float longtitude= (float) ((accept[7] &0xff)+((accept[8]>>2)&0x3f)/60.0+
                                        (((accept[8]&0x03)<<8)+(accept[9]&0xff))/60000.0);
                        //computePara.BitDecimal2float(accept[8] ,accept[9] );
                stationState.setLongtitude(longtitude);
                stationState.setNorthORsouth(accept[10] >> 7);
                float latitude= (float) ((accept[10]&0x7f)+((accept[11]>>2)&0x3f)/60.0+
                                        (((accept[11]&0x03)<<8)+(accept[12]&0xff))/60000.0);
                        //computePara.BitDecimal2float( accept[11],accept[12]);
                stationState.setLatitude(latitude);
                stationState.setIsAboveHrizon(accept[13] >> 7);
                stationState.setAtitude(((accept[13] & 0x7f) << 8) + accept[14]);
                stationState.setContent(accept);
                out.write(stationState);
                Log.d("query","终端是否在网应答"+ Arrays.toString(accept));
                return MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
