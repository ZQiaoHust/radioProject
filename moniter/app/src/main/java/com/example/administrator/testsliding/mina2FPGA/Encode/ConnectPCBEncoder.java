package com.example.administrator.testsliding.mina2FPGA.Encode;

import com.example.administrator.testsliding.Bean.Connect;
import com.example.administrator.testsliding.GlobalConstants.Constants;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**硬件接入方式
 * Created by jinaghao on 15/11/25.
 */
public class ConnectPCBEncoder implements MessageEncoder<Connect> {
    @Override
    public void encode(IoSession ioSession, Connect connect,
                       ProtocolEncoderOutput out) throws Exception {

        byte[] bytes=GetBytes(connect);
        IoBuffer buffer= IoBuffer.allocate(17);
        buffer.put(bytes);
        buffer.flip();
        out.write(buffer);
    }

    private byte[] GetBytes(Connect connect){
        byte[] data=new byte[17];
        data[0]=0x55;
        data[1]=0x09;
        data[2]= (byte) (Constants.ID&0xff);//设备ID号
        data[3]= (byte) ((Constants.ID>>8)&0xff);
        data[4]= (byte) connect.getConn();
        data[16]= (byte) 0xAA;
        return data;
    }
}
