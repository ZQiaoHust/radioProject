package com.example.administrator.testsliding.mina2FPGA.Decode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.PowerSpectrumAndAbnormalPonit;
import com.example.administrator.testsliding.Bean.ReceiveRight;
import com.example.administrator.testsliding.Bean.ReceiveWrong;
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
 * Created by jinaghao on 15/12/24.
 */
public class PowerSpectrumAndAbnormalPonitFineDecoder implements MessageDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private int k;
    private ReceiveRight mReceiveRight = new ReceiveRight();
    private ReceiveWrong mReceiveWrong = new ReceiveWrong();

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        Log.d("abcd", "尝试实时功率谱解码器");
        if(Constants.flag ) {
            Constants.buffer.flip();
            Constants.buffer.limit(Constants.positionValue);
            byte headtail = Constants.buffer.get();
//            while (headtail != (byte) 0x55) {
//                i++;
//                headtail = in.get();
//                Log.d("abcd", "Constants.buffer实时功率谱解码器找帧头" + headtail);
//                if (i >= in.remaining()) {
//                    break;
//                }
//            }
            byte functionCode = Constants.buffer.get();
            Log.d("abcd", "Constants.buffer实时功率谱解码器functionCode" + functionCode);
            if (functionCode == 0x51 || functionCode == 0x53) {
                Constants.Isstop=false;
                return MessageDecoderResult.OK;

            } else {
                Constants.Isstop=true;
                return MessageDecoderResult.NOT_OK;
            }
        }else {
            if (in.remaining() < 2) {
                return MessageDecoderResult.NEED_DATA;
            } else {
                byte head=in.get();
                Log.d("abcd", "实时功率谱解码器找帧头" + head);
//                while (head != (byte) 0x55) {
//                    i++;
//                    head=in.get();
//                    Log.d("abcd", "实时功率谱解码器找帧头" + head);
//                    if (i >= in.remaining()) {
//                        break;
//                    }
//                }
                byte functionCode = in.get();
                Log.d("abcd", "实时功率谱解码器functionCode" + functionCode);
                if (functionCode == 0x51 || functionCode == 0x53) {
                    Constants.Isstop=false;
                    return MessageDecoderResult.OK;
                } else {
                    Constants.Isstop=true;
                    return MessageDecoderResult.NOT_OK;
                }
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       final ProtocolDecoderOutput out) throws Exception {
        Constants.ctx = getContext(session);//获取session  的context
        long matchCount = Constants.ctx.getMatchLength();//目前已获取的数据
        long length = Constants.ctx.getLength();//数据总长度
        IoBuffer buffer = Constants.ctx.getBuffer();//数据存入buffer

        if (Constants.flag) {
            Constants.buffer.flip();
            Constants.buffer.limit(Constants.positionValue);
            buffer.put(Constants.buffer);
            matchCount=Constants.positionValue;
            Constants.buffer.clear();
            Constants.flag=false;
            Constants.positionValue=0;
        }

///////////////////////////////////////////////////
        matchCount += in.remaining();
        Log.d("abcd", "共收到字节：" + String.valueOf(matchCount));
        Constants.ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            if(matchCount< length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                final byte[] b = new byte[1613];
                byte[] temp = new byte[1613];
                in.get(temp,0, (int) (length-buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                if (b[1] == (byte) 0x51 && b[1612] == (byte) 0xaa) {
                    long a = System.currentTimeMillis();
                    PowerSpectrumAndAbnormalPonit PSAP = byte2Object(b);
                    Log.d("psap", Arrays.toString(PSAP.getPSpower()));

                    if (PSAP != null) {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                //实现自己的延时执行任务
                                Constants.FPGAsession.write(mReceiveRight);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 200);

                        out.write(PSAP);
                        Log.d("psap", Arrays.toString(b));
                        Log.d("psap", "当前帧总共段数：" + PSAP.getTotalBand());
                        Log.d("psap", "当前帧所在序号：" + PSAP.getNumN());
                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
                        k++;
                        Log.d("sucess", "成功次数：" + String.valueOf(k));
                        System.out.println("fine功率谱和异常频点解码完成.......");
                    }
                } else {
                    Constants.failCount++;
                    Log.d("fail", "重传次数：" +  Constants.failCount);
                    Constants.FPGAsession.write(mReceiveWrong);
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

    private PowerSpectrumAndAbnormalPonit byte2Object(byte[] bytes) {

        PowerSpectrumAndAbnormalPonit PSAP = new PowerSpectrumAndAbnormalPonit();

        //功率谱
        PSAP.setFileSendmodel(0);//0表示fine
        byte[] b1 = new byte[14];
        System.arraycopy(bytes, 4, b1, 0, 14);
        PSAP.setLocationandTime(b1);
        PSAP.setSweepModel(((bytes[18] >> 6) & 0x03));
        PSAP.setFileSendmodel(((bytes[18] >> 4) & 0x03));
        PSAP.setIsChange((bytes[18] & 0x0f));
        PSAP.setTotalBand(((bytes[19] >> 4) & 0x0f));
        PSAP.setNumN((bytes[19] & 0x0f));//扫频总段数的序号
        PSAP.setPSbandNum((bytes[20] & 0xff));
        byte[] b2 = new byte[1536];
        System.arraycopy(bytes, 21, b2, 0, 1536);
        PSAP.setPSpower(b2);


        //异常频点
        PSAP.setAPbandNum((bytes[1578] & 0xff));
        PSAP.setAPnum((bytes[1579] & 0xff));
        byte[] b4 = new byte[30];
        System.arraycopy(bytes, 1580, b4, 0, 30);
        PSAP.setAPpower(b4);

        return PSAP;
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
//    private class Context {
//        public IoBuffer buffer;
//        public long length = 1613 ;
//        public long matchLength = 0;
//        public long startTime=0;
//
//        public Context() {
//            buffer = IoBuffer.allocate(1024).setAutoExpand(true);
//        }
//
//        public void setBuffer(IoBuffer buffer) {
//            this.buffer = buffer;
//        }
//
//        public void setLength(long length) {
//            this.length = length;
//        }
//
//        public void setMatchLength(long matchLength) {
//            this.matchLength = matchLength;
//        }
//
//        public IoBuffer getBuffer() {
//
//            return buffer;
//        }
//
//        public long getLength() {
//            return length;
//        }
//
//        public long getMatchLength() {
//            return matchLength;
//        }
//        public long getStartTime() {
//            return startTime;
//        }
//
//        public void setStartTime(long startTime) {
//
//            this.startTime = startTime;
//        }

//        public void reset() {
//            this.buffer.clear();
//            this.length = 1613;
//            this.matchLength = 0;
//            this.startTime=0;
//        }
//    }
}
