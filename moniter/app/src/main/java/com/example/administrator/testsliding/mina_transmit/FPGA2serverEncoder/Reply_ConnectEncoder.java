package com.example.administrator.testsliding.mina_transmit.FPGA2serverEncoder;

import android.util.Log;

import com.example.administrator.testsliding.Bean.Connect;
import com.example.administrator.testsliding.bean2Transmit.FPGA2server.Reply_Connect;
import com.example.administrator.testsliding.bean2Transmit.server2FPGAQuery.Query_Connect;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/1.
 */
public class Reply_ConnectEncoder implements MessageEncoder<Connect> {
    @Override
    public void encode(IoSession ioSession, Connect connect,
                       ProtocolEncoderOutput out) throws Exception {

        if(connect!=null){
            IoBuffer buffer=IoBuffer.allocate(17,true);
            byte[] bytes=connect.getContent();
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("trans","转发查询响应"+ Arrays.toString(bytes));
        }

    }
}
