package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.Bean.IQwave;
import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.IQContext;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jinaghao on 15/12/1.
 */
public class IQwaveDecoder implements MessageDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private ReceiveRight mReceiveRight = new ReceiveRight();
    private ReceiveWrong mReceiveWrong = new ReceiveWrong();
    private  int k;

    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte frameHead = in.get();
            if (frameHead == (byte) 0x55) {
                byte functionCode = in.get();
                if (functionCode == 0x54) {
                    return MessageDecoderResult.OK;
                } else
                    return MessageDecoderResult.NOT_OK;
            } else
                return MessageDecoderResult.NOT_OK;
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        Constants.ctxIQ  = getContext(session);//获取session  的context
        long matchCount = Constants.ctxIQ.getMatchLength();//目前已获取的数据
        long length =  Constants.ctxIQ.getLength();//数据总长度
        IoBuffer buffer =  Constants.ctxIQ.getBuffer();//数据存入buffer

        matchCount += in.remaining();
        Log.d("IQ", "共收到字节：" + String.valueOf(matchCount));
        Constants.ctxIQ.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            if (matchCount < length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                final byte[] b = new byte[(int) length];
                byte[] temp = new byte[(int) length];
                in.get(temp, 0, (int) (length - buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                if (b[1] == (byte) 0x54 && b[6026] == (byte) 0xaa) {
                    IQwave iQwave = byte2Object(b);
                    if (iQwave != null) {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                //实现自己的延时执行任务
                                Constants.FPGAsession.write(mReceiveRight);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 200);
                        out.write(iQwave);
                        Log.d("IQ", "当前帧总共段数：" + iQwave.getTotalBands());
                        Log.d("IQ", "当前帧所在序号：" + iQwave.getNowNum());
                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
                        k++;
                        Log.d("IQ", "成功次数：" + String.valueOf(k));
                        System.out.println("IQ波解码完成.......");
                    }
                } else {
                    Constants.FPGAsession.write(mReceiveWrong);
                    Constants.failCount++;
                }
                    Constants.ctxIQ.reset();
                    return MessageDecoderResult.OK;
                } else {
                    Constants.ctxIQ.setBuffer(buffer);
                    Constants.NotFill = true;
                    return MessageDecoderResult.NEED_DATA;
                }
            }
            return MessageDecoderResult.NEED_DATA;

    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }


    //获取session的context
    public IQContext getContext(IoSession session) {
        IQContext ctx = (IQContext) session.getAttribute(CONTEXT);
        if (ctx == null) {
            ctx = new IQContext();
            session.setAttribute(CONTEXT, ctx);
        }
        return ctx;
    }

    private IQwave byte2Object(byte[] b){
        IQwave wave=new IQwave();
        //地理位置
        byte[] data1=new byte[9];
        System.arraycopy(b,4,data1,0,9);
        wave.setLocation(data1);
        //时间
        byte[] data2=new byte[5];
        System.arraycopy(b,13,data2,0,5);
        wave.setTime(data2);
        //IQ参数
        byte[] data3=new byte[5];
        System.arraycopy(b,18,data3,0,5);
        wave.setIQpara(data3);
        //数据块总个数
        wave.setTotalBands(b[22]&0xff);
        //数据块序号
        wave.setNowNum(b[23]&0xff);
        //数据块信息，包括序号
        byte[] data4=new byte[6001];
        System.arraycopy(b,23,data4,0,6001);
        wave.setIQwave(data4);
        return wave;
    }
}
