package com.hust.radiofeeler.mina2server.Encoder;

import com.hust.radiofeeler.bean2server.RequestStationRegisterAttributes;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**台站属性查询
 * Created by Administrator on 2015/11/23.
 */
public class RequestStationRegisterAttributeEncoder
        implements MessageEncoder<RequestStationRegisterAttributes> {
    @Override
    public void encode(IoSession session, RequestStationRegisterAttributes request,
                       ProtocolEncoderOutput out) throws Exception {

        if (request != null) {
            byte[] bytes1 = GetBytes(request);
//            int capacity = bytes1.length;
//            IoBuffer buffer = IoBuffer.allocate(capacity, false);
//            buffer.setAutoExpand(true);
            IoBuffer buffer=IoBuffer.allocate(11);
            buffer.put(bytes1);
            buffer.flip();
            out.write(buffer);
        }

    }


    /**
     * 台站属性查询
     *
     * @return
     */
    private byte[] GetBytes(RequestStationRegisterAttributes request) {

        byte[] data = new byte[11];
        data[0] = 0x55;
        data[1] = (byte) 0xA8;
        data[2] = (byte) (request.getEquipmentID() & 0xff);//设备ID号的低8位
        data[3] = (byte) ((request.getEquipmentID()>> 8) & 0xff);//设备ID号的高位

        data[4] = (byte) ((request.getStartFreq() >> 8) & 0xff);//整数部分高八位
        data[5] = (byte) (request.getStartFreq()& 0xff);
        data[6] = (byte) ((request.getEndFreq() >> 8) & 0xff);//整数部分高八位
        data[7] = (byte) (request.getEndFreq()  & 0xff);
        data[10] = (byte) 0xAA;
        return data;
    }

}
