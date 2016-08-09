package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.Bean.PowerSpectrumAndAbnormalPonit;
import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
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
        Log.d("abcd", "尝试PowerSpectrumAndAbnormalPonitFineDecoder谱解码器");
        if (Constants.flag) {
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
            if (functionCode == 0x51 || functionCode == 0x53) {
                Constants.Isstop = false;
                Constants.flag=false;
                return MessageDecoderResult.OK;

            } else {
                Constants.Isstop = true;
                return MessageDecoderResult.NOT_OK;
            }
        } else {
            if (in.remaining() < 2) {
                return MessageDecoderResult.NEED_DATA;
            } else {
                byte head = in.get();
//                while (head != (byte) 0x55) {
//                    i++;
//                    head=in.get();
//                    Log.d("abcd", "实时功率谱解码器找帧头" + head);
//                    if (i >= in.remaining()) {
//                        break;
//                    }
//                }
                byte functionCode = in.get();
                if (functionCode == 0x51 || functionCode == 0x53) {
                    Constants.Isstop = false;
                    Constants.flag=false;
                    return MessageDecoderResult.OK;
                } else {
                    Constants.Isstop = true;
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
            matchCount = Constants.positionValue;
            Constants.buffer.sweep();
            Constants.flag = false;
            Constants.positionValue = 0;
        }

///////////////////////////////////////////////////
        if(length==0){
            length=1614;
            Constants.ctx.setLength(length);
            matchCount = in.remaining();
        }else {
            matchCount += in.remaining();
        }
        Constants.ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            if (matchCount < length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[1614];
                byte[] temp = new byte[1614];
                in.get(temp, 0, (int) (length - buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                if (b[1] == (byte) 0x51 && b[1613] == (byte) 0xaa) {

                    PowerSpectrumAndAbnormalPonit PSAP = byte2Object(b);
                    if (PSAP != null) {
                        TimerTask task = new TimerTask() {
                            public void run() {
                                //实现自己的延时执行任务
                                Constants.FPGAsession.write(mReceiveRight);
                            }
                        };
                        Timer timer = new Timer();
                        timer.schedule(task, 20);
                        out.write(PSAP);
                        Log.d("file", "当前帧总共段数：" + PSAP.getTotalBand());
                        Log.d("file", "当前帧所在序号：" + PSAP.getNumN());

                        //如果是抽取上传模式，则需要计数
                        if (PSAP.getNumN() == 1 && Constants.sendMode == 3) {
                            if (Constants.SELECT_COUNT == Constants.selectRate)
                                Constants.SELECT_COUNT = 0;
                            Constants.SELECT_COUNT++;
                            long a = System.currentTimeMillis();
                            Log.d("file", "收到第一段时间：" + a);
                        }
                        if (PSAP.getTotalBand() == PSAP.getNumN()) {
                            long t1 = System.currentTimeMillis();
                            Log.d("file", "收齐数据段的时刻：" + t1);

                        }
                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
                        k++;
                        Log.d("sucess", "成功次数：" + String.valueOf(k));
                    }
                } else {
                    Constants.failCount++;
                    Log.d("fail", "重传次数：" + Constants.failCount);
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
        int total=bytes[19] & 0xff;
        PSAP.setTotalBand(total);
        Constants.drawSpectrumBands=total;
        PSAP.setNumN((bytes[20] & 0xff));//扫频总段数的序号
        PSAP.setPSbandNum((bytes[21] & 0xff));
        byte[] b2 = new byte[1536];
        System.arraycopy(bytes, 22, b2, 0, 1536);
        PSAP.setPSpower(b2);


        //异常频点
        PSAP.setAPbandNum((bytes[1579] & 0xff));
        PSAP.setAPnum((bytes[1580] & 0xff));
        byte[] b4 = new byte[30];
        System.arraycopy(bytes, 1581, b4, 0, 30);
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
}

