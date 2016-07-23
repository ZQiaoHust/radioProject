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
public class PowerSpectrumAndAbnormalPonitCoarseDecoder implements MessageDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private int k;
    private ReceiveRight mReceiveRight = new ReceiveRight();
    private ReceiveWrong mReceiveWrong = new ReceiveWrong();
    private String TAG="PowerSpectrumAndAbnormalPonitCoarseDecoder";


    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

        Log.d("abcd", "尝试PowerSpectrumAndAbnormalPonitCoarseDecoder谱解码器");

        if(Constants.flag ) {
            Constants.buffer.limit(Constants.positionValue);
            Constants.buffer.flip();
            byte headtail = Constants.buffer.get();
//            while (headtail != (byte) 0x55) {
//                i++;
//                headtail = in.get();
//                if (i >= in.remaining()) {
//                    break;
//                }
//            }
            byte functionCode = Constants.buffer.get();
            if (functionCode == 0x56 | functionCode == 0x58) {
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
                byte bytes=in.get();
//                while (in.get() != (byte) 0x55) {
//                    i++;
//                    if (i >= in.remaining()) {
//                        break;
//                    }
//                }
                byte functionCode = in.get();
                Log.d(TAG, "functionCode: "+functionCode);
                if (functionCode == 0x56||functionCode == 0x58) {
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
        ///////////////////////////////////////////
///////////////////////////////////////////////////
        if(length==0){
            length=1614;
            Constants.ctx.setLength(length);
            matchCount = in.remaining();
        }else {
            matchCount += in.remaining();
        }
        Log.d("abcd", "共收到字节：" + String.valueOf(matchCount));
        Constants.ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            if(matchCount< length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[1614];
                byte[] temp = new byte[(int)length];
                in.get(temp,0, (int) (length-buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                buffer.flip();
                buffer.get(b);
                if (b[0] == (byte) 0x55 && b[1613] == (byte) 0xaa) {

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
                        Constants.NotFill = false;//收成功，NotFill表示没满的变量
                        k++;
                        Log.d("sucess", "成功次数：" + String.valueOf(k));
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
        PSAP.setStyle(1);//1表示coarse
        byte[] b1 = new byte[14];
        System.arraycopy(bytes, 4, b1, 0, 14);
        PSAP.setLocationandTime(b1);
        PSAP.setSweepModel(((bytes[18] >> 6) & 0x03));
        PSAP.setFileSendmodel(((bytes[18] >> 4) & 0x03));
        PSAP.setIsChange((bytes[18] & 0x0f));
        PSAP.setTotalBand((bytes[19] & 0xff));
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
