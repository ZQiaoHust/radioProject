package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
import com.hust.radiofeeler.Bean.TDOAdata;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.GlobalConstants.Context;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TDOA数据帧,与iq真一样
 */
public class TDOAdataDecoder implements MessageDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private ReceiveRight mReceiveRight = new ReceiveRight();
    private ReceiveWrong mReceiveWrong = new ReceiveWrong();


    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte frameHead = in.get();
            byte functionCode = in.get();
            if (((frameHead == (byte) 0x55)||(frameHead == (byte) 0x66))&&(functionCode == 0x42)) {

                    return MessageDecoderResult.OK;
                } else
                    return MessageDecoderResult.NOT_OK;

        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        Constants.ctx = getContext(session);//获取session  的context
        long matchCount = Constants.ctx.getMatchLength();//目前已获取的数据
        long length =  Constants.ctx.getLength();//数据总长度
        IoBuffer buffer =  Constants.ctx.getBuffer();//数据存入buffer
        //第一次取数据
        if (length == 0) {
            length =6031;
            //保存第一次获取的长度
            Constants.ctx.setLength(length);
            matchCount = in.remaining();
        } else {
            matchCount += in.remaining();
        }
        Log.d("TDOA", "共收到字节：" + String.valueOf(matchCount));
        Constants.ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            if (matchCount < length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
               byte[] b = new byte[(int) length];
                byte[] temp = new byte[(int) length];
                in.get(temp, 0, (int) (length - buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                if (b[1] == (byte) 0x42 && b[6030] == (byte) 0xaa) {
                    TDOAdata td = byte2Object(b);
                    if (td != null) {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                //实现自己的延时执行任务
                                Constants.FPGAsession.write(mReceiveRight);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 20);
                        out.write(td);
                        Log.d("TDOA", "当前帧总共段数：" + td.getTotalBands());
                        Log.d("TDOA", "当前帧所在序号：" + td.getNowNum());
                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
                    }
                } else {
                    Constants.FPGAsession.write(mReceiveWrong);
                    Constants.failCount++;
                }
                    Constants.ctx.reset();
                    return MessageDecoderResult.OK;
                } else {
                    Constants.ctx.setBuffer(buffer);
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
    public Context getContext(IoSession session) {
        Context ctx = (Context) session.getAttribute(CONTEXT);
        if (ctx == null) {
            ctx = new Context();
            session.setAttribute(CONTEXT, ctx);
        }
        return ctx;
    }

    private TDOAdata byte2Object(byte[] b){
        TDOAdata wave=new TDOAdata();
        //地理位置
        byte[] data1=new byte[9];
        System.arraycopy(b,4,data1,0,9);
        wave.setLocation(data1);
        //时间
        byte[] data2=new byte[9];
        System.arraycopy(b,13,data2,0,9);
        wave.setTime(data2);
        //IQ参数
        byte[] data3=new byte[5];
        System.arraycopy(b,22,data3,0,5);
        wave.setIQpara(data3);
        //数据块总个数
        wave.setTotalBands(b[26]&0xff);
        //数据块序号
        wave.setNowNum(b[27]&0xff);
        //数据块信息，包括序号
        byte[] data4=new byte[6001];
        System.arraycopy(b,27,data4,0,6001);
        wave.setIQwave(data4);
        return wave;
    }
}
