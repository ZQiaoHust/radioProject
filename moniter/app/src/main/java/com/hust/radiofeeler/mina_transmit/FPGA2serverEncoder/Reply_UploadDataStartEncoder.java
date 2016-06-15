package com.hust.radiofeeler.mina_transmit.FPGA2serverEncoder;

import com.hust.radiofeeler.Bean.UploadData;
import com.hust.radiofeeler.GlobalConstants.Constants;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

/**查询回应 功率谱异常频点上传
 * Created by Administrator on 2015/12/1.
 */
public class Reply_UploadDataStartEncoder implements MessageEncoder<UploadData> {
    @Override
    public void encode(IoSession ioSession, UploadData data,
                       ProtocolEncoderOutput out) throws Exception {

        if(data!=null){
            IoBuffer buffer=IoBuffer.allocate(7,true);
           // byte[] bytes=data.getContent();
            ///自己组帧回给中心站
            byte[] bytes=new byte[7];
            bytes[0]=0x66;
            if(Constants.isUpload==0)
                bytes[1]=0x2A;
            else
                bytes[1]=0x2B;
            bytes[2]= (byte) (Constants.ID&0xff);
            bytes[3]= (byte) ((Constants.ID>>8)&0xff);
            bytes[6]= (byte) 0xAA;
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
        }

    }
}
