package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.StationState;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/25.
 */
public class StationStateDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<2){
            return MessageDecoderResult.NEED_DATA;
        }else{
            byte frameHead=in.get();
            if(frameHead==0x55){
                byte functionCode=in.get();
                if(functionCode==0x2c){
                    return MessageDecoderResult.OK;
                }else
                    return MessageDecoderResult.NOT_OK;
            }
            return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
        IoBuffer buffer= IoBuffer.allocate(17);
        StationState stationState=new StationState();
        int receiveCount=0;

        while (in.hasRemaining()){
            byte b=in.get();
            buffer.put(b);
            receiveCount++;

            if(receiveCount==17){
                buffer.flip();
                byte[] accept=new byte[17];
                buffer.get(accept);
                stationState.setEquipmentID((accept[2]&0xff)+((accept[3]&0xff)<<8));
                stationState.setOnNet(accept[4]);
                stationState.setModel(accept[5]);
                stationState.setEastORwest(accept[6]);
                stationState.setLongtitude(accept[7] + accept[8] / 256 + accept[9] / 65536);
                stationState.setNorthORsouth(accept[10] >> 7);
                stationState.setLatitude((accept[10] & 0xef) + accept[11] / 256 + accept[12] / 65536);
                stationState.setIsAboveHrizon(accept[13] >> 7);
                stationState.setAtitude(((accept[13] & 0xef) << 8) + accept[14]);

                out.write(stationState);

                return MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
