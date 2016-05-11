package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.GlobalConstants.Constants;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**没有找到他之前的解码器时，次解码作为清空iobuffer的操作
 * Created by Administrator on 2016/3/9.
 */
public class CllearDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer ioBuffer) {
        if(Constants.Isstop) {
            ioBuffer.sweep();
           // ioBuffer=null;
           int n=ioBuffer.remaining();
            Constants.buffer.sweep();//buffer中的是错误帧，此时也找不到解码器
            Constants.flag=false;
            Log.d("abcd", "clear解码器清空数据");
        }
        return MessageDecoderResult.OK;
    }

    @Override
    public MessageDecoderResult decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {
        return null;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
}
