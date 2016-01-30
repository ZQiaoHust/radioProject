package com.example.administrator.testsliding.mina2server;


import com.example.administrator.testsliding.bean2server.RequstNetwork;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**
 * Created by Administrator on 2015/11/18.
 */
public class RequstNetworkEncoder implements MessageEncoder<RequstNetwork>{
    @Override
    public void encode(IoSession ioSession, RequstNetwork requstNetwork, ProtocolEncoderOutput out)
            throws Exception {
        if (requstNetwork != null) {
            byte[] bytes1 = GetBytes(requstNetwork);
            int capacity = bytes1.length;
            IoBuffer buffer = IoBuffer.allocate(capacity, false);
            buffer.setAutoExpand(true);
            buffer.put(bytes1);

            buffer.flip();
            out.write(buffer);
        }
    }

    /**
     * 终端入网请求数据帧
     * @return
     */
    private byte[] GetBytes(RequstNetwork net){
        byte[] data=new byte[17];
        byte[] bytes;
        byte[] byte1;
        data[0]=0x55;
        data[1]= (byte) 0xA1;
        data[2]= (byte) (net.getEquipmentID()&0xff);  //ID号低字节
        data[3]= (byte) ((net.getEquipmentID()>>8)&0xff);
        data[4]=net.getStyle();
        bytes=net.getLongtitude();
        System.arraycopy(bytes, 0, data, 5, 4);

        bytes=net.getLatitude();
        System.arraycopy(bytes, 0, data, 9, 3);

        byte1=net.getHeight();
        System.arraycopy(byte1,0,data,12,2);

        data[16]= (byte) 0xAA;//帧尾
        return data;
    }
}
