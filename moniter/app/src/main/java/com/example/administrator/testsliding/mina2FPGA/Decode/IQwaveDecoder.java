package com.example.administrator.testsliding.mina2FPGA.Decode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.IQwave;
import com.example.administrator.testsliding.Bean.PowerSpectrumAndAbnormalPonit;
import com.example.administrator.testsliding.GlobalConstants.Constants;
import com.example.administrator.testsliding.GlobalConstants.Context;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jinaghao on 15/12/1.
 */
public class IQwaveDecoder implements MessageDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");

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
        Context ctx = getContext(session);//获取session  的context
        long matchCount = ctx.getMatchLength();//目前已获取的数据
        long length = ctx.getLength();//数据总长度
        IoBuffer buffer = ctx.getBuffer();//数据存入buffer

        matchCount += in.remaining();
        Log.d("abcd", "共收到字节：" + String.valueOf(matchCount));
        ctx.setMatchLength(matchCount);

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
                    long a = System.currentTimeMillis();
//                    IQwave iQwave = byte2Object(b);
//                    Log.d("psap", Arrays.toString(PSAP.getPSpower()));
//
//                    if (PSAP != null) {
//                        TimerTask task = new TimerTask() {
//                            public void run() {
//                                //实现自己的延时执行任务
//                                Constants.FPGAsession.write(mReceiveRight);
//                            }
//                        };
//                        Timer timer = new Timer();
//                        timer.schedule(task, 200);
//
//                        out.write(PSAP);
//                        Log.d("psap", Arrays.toString(b));
//                        Log.d("psap", "当前帧总共段数：" + PSAP.getTotalBand());
//                        Log.d("psap", "当前帧所在序号：" + PSAP.getNumN());
//                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
//                        k++;
//                        Log.d("abcd", "成功次数：" + String.valueOf(k));
//                        System.out.println("fine功率谱和异常频点解码完成.......");
//                    }
//                } else {
//                    Constants.FPGAsession.write(mReceiveWrong);
//                }
                    Constants.ctx.reset();
                    return MessageDecoderResult.OK;
                } else {
                    Constants.ctx.setBuffer(buffer);
                    Constants.NotFill = true;
                    return MessageDecoderResult.NEED_DATA;
                }
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

    /**
     * 定义一个内部类，用来封转当前解码器中的一些公共数据，主要是用于大数据解析
     //     */
    private class Context {
        public IoBuffer buffer;
        public long length = 6027 ;
        public long matchLength = 0;
        public long startTime=0;

        public Context() {
            buffer = IoBuffer.allocate(1024).setAutoExpand(true);
        }

        public void setBuffer(IoBuffer buffer) {
            this.buffer = buffer;
        }

        public void setLength(long length) {
            this.length = length;
        }

        public void setMatchLength(long matchLength) {
            this.matchLength = matchLength;
        }

        public IoBuffer getBuffer() {

            return buffer;
        }

        public long getLength() {
            return length;
        }

        public long getMatchLength() {
            return matchLength;
        }
        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {

            this.startTime = startTime;
        }

        public void reset() {
            this.buffer.clear();
            this.length = 1613;
            this.matchLength = 0;
            this.startTime=0;
        }
    }
}
