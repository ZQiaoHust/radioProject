package com.example.administrator.testsliding.mina2server.Decoder;

import android.util.Log;

import com.example.administrator.testsliding.bean2server.File_ModifyIngain;
import com.example.administrator.testsliding.bean2server.ModifyIngainView;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/12/16.
 */
public class ModifyInGainDecoder implements MessageDecoder {
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<10){
            return MessageDecoderResult.NEED_DATA;
        }else {
            in.position(8);
            byte b1=in.get();
            byte b2=in.get();
            if(((b1==0x55)||(b1==0x66))&&(b2==(byte)0xD1)){
                return MessageDecoderResult.OK;
            }else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        int hascount=0;
        long length=0;
        while (in.hasRemaining()){
            hascount++;
            if(hascount==8) {
                length = in.getLong();
            }
            if(hascount==length){
                byte[] bytes=new byte[(int)length];
                in.get(bytes);
               File_ModifyIngain modify=new File_ModifyIngain();
                modify.setFileContent(bytes);
                out.write(modify);
                Log.d("xyz", Arrays.toString(bytes));
                return MessageDecoderResult.OK;
            }
        }
        return MessageDecoderResult.NOT_OK;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


}
