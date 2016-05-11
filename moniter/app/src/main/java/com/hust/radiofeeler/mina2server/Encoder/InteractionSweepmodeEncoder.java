package com.hust.radiofeeler.mina2server.Encoder;

import android.util.Log;

import com.hust.radiofeeler.bean2server.InteractionSweepModeRequest;
import com.hust.radiofeeler.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import java.util.Arrays;

/**
 * Created by Administrator on 2015/11/29.
 */
public class InteractionSweepmodeEncoder  implements MessageEncoder<InteractionSweepModeRequest> {
    private ComputePara compute=new ComputePara();
    @Override
    public void encode(IoSession ioSession, InteractionSweepModeRequest sweep,
                       ProtocolEncoderOutput out) throws Exception {

        if(sweep!=null){
            IoBuffer buffer=IoBuffer.allocate(24,true);
            byte[] bytes=GetBytes(sweep);
            buffer.put(bytes);
            buffer.flip();
            out.write(buffer);
            Log.d("abnormal", Arrays.toString(bytes));
        }

    }

    private byte[] GetBytes(InteractionSweepModeRequest sweep){
        byte[] data=new byte[24];
        int startOffset=compute.ComputeSegOffset(sweep.getStartFreq());//起点段内偏移量，偏移量最多只占10位
        int endOffset=compute.ComputeSegOffset(sweep.getEndFreq());
        int thred=Math.abs(sweep.getFixThreshold());//固定门限是有符号的数
        data[0]=0x55;
        data[1]= (byte) 0xAD;
        data[2]=(byte) (sweep.getEquipmentID() & 0xff);
        data[3]=(byte) ((sweep.getEquipmentID()>>8) & 0xff);
        data[4]=(byte) (sweep.getIDcard()& 0xff);//指定ID号的低8位
        data[5]=(byte) ((sweep.getIDcard()>>8) & 0xff);

        data[6]= (byte) (((sweep.getSweepMode()<<2)&0xff)+(sweep.getSendFilemode()&0xff));     //扫频模式+上传模式
        data[7]= (byte) (sweep.getTotalBand()&0xff);//针对离散和多频段扫频的频段总数(N)
        data[8]= (byte) (sweep.getBandNum()&0xff);//频段序号(1~N)

        data[9]= (byte) compute.ComputeSegNumber(sweep.getStartFreq());
        //偏移量至多只占10bit，因此只取前两个字节即可
        data[10]=(byte) ((startOffset >> 8) & 0xff); //偏移量高位，
        data[11]=(byte) (startOffset & 0xff);//偏移量低8位

        data[12]= (byte) compute.ComputeSegNumber(sweep.getEndFreq());
        data[13]=(byte) ((endOffset >> 8) & 0xff); //偏移量高位，
        data[14]=(byte) (endOffset & 0xff);//偏移量低8位

        data[15]= (byte) ((sweep.getJudgeThreshold()<<6)+(sweep.getExtractionRatio()&0xff));//判定门限+抽取倍率
        data[16]= (byte) (sweep.getRecvGain()+3);
        //gateStyle为0代表自适应门限，1代表固定门限
        data[17]= (byte) sweep.getThresholdStyle();
        data[18]= (byte) sweep.getAdapThreshold();//自适应门限

        //固定门限转换（固定门限的值不会有16bit）
        if(sweep.getFixThreshold()>0){
            data[19] = (byte) ((thred>> 8) & 0xff);//固定门限高八位，最高位符号位为0
        }else {
            data[19]= (byte) (128+((thred>> 8) & 0xff));//最高位符号位为1
        }
        data[20]= (byte) (thred & 0xff);

        data[23]= (byte) 0xAA;
        return data;

    }
}
