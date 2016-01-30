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
public class PowerSpectrumAndAbnormalPonitDecoder implements MessageDecoder {


    private int i;
    private boolean flag = false;
    private int positionValue = 0;
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private int k;
    private ReceiveRight mReceiveRight=new ReceiveRight();
    private ReceiveWrong mReceiveWrong=new ReceiveWrong();
    private boolean fail=false;//是否收满


    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
        Log.d("abcd", "尝试功率谱解码器");

        if (flag == true) {
            in.limit(positionValue);
            in.flip();
        }
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            i = 0;
            byte hd1=in.get();
            while (!((hd1 == (byte) 0x55)||(hd1 == (byte) 0x66))) {
                i++;
                hd1=in.get();
                if (i >= in.remaining()) {
                    break;
                }
            }
            byte functionCode = in.get();
            if (functionCode == 0x0e || functionCode == 0x1d||functionCode == 0x0d) {
                return MessageDecoderResult.OK;


            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       final ProtocolDecoderOutput out) throws Exception {


        if (flag == true) {
            in.limit(positionValue);
            in.flip();
        }

        Constants.ctx = getContext(session);//获取session  的context
        long matchCount =  Constants.ctx .getMatchLength();//目前已获取的数据
        long length =  Constants.ctx .getLength();//数据总长度
        long sTime= Constants.ctx .getStartTime();//开始时间
        IoBuffer buffer =  Constants.ctx .getBuffer();//数据存入buffer

        flag = false;

        /////////////
        if (i > 0) {
            in.position(i);
            i = 0;
        }
        ///////////////////////////////////////////
        if(matchCount==0){//第一次收到数据
            sTime=System.currentTimeMillis();
            Constants.ctx .setStartTime(sTime);//存储时间
            Log.d("abcd", "起始时间："+String.valueOf(sTime));
            Constants.startTime=sTime;
        }
///////////////////////////////////////////////////
        matchCount += in.remaining();
        Log.d("abcd", "共收到字节："+String.valueOf(matchCount));
        Constants.ctx .setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据

            buffer.put(in);// 添加到保存数据的buffer中
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                final byte[] b = new byte[1613];
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);
                if(b[0]==(byte)0x55 && b[1612]==(byte)0xaa ){
                    Log.d("abcd", "结束时间："+String.valueOf(System.currentTimeMillis()));

                    //Constants.FPGAsession .write(mReceiveRight);
//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {

                            long a=  System.currentTimeMillis();
                            Log.d("abcd", "解码开始时间："+String.valueOf(System.currentTimeMillis()));
                            PowerSpectrumAndAbnormalPonit PSAP =byte2Object(b);
                    Log.d("psap", Arrays.toString(PSAP.getPSpower()));
                            if (PSAP!= null) {

                                TimerTask task = new TimerTask(){
                                         public void run(){
                                             //实现自己的延时执行任务
                                             Constants.FPGAsession.write(mReceiveRight);
                                            }
                                     };
                                Timer timer = new Timer();
                                timer.schedule(task,200);

                                out.write(PSAP);
                                Log.d("psap", Arrays.toString(b));

                                Constants.Success=true;
                                Constants.NotFill=false;//收成功，NotFill表示没满的变量
                                k++;
                                Log.d("abcd", "成功次数："+String.valueOf(k));
                                System.out.println("功率谱和异常频点解码完成.......");
                            }
                            long c=System.currentTimeMillis();
                            Log.d("abcd", "解码结束时间："+String.valueOf(c));
                            Log.d("abcd", "解码器所用时间："+String.valueOf(c-a));

//                    }).start();


                }else {
                    Constants.Success=false;
                    Constants.FPGAsession.write(mReceiveWrong);

                }


                //粘包的处理
                if (buffer.remaining() > 0) {
                    IoBuffer temp = IoBuffer.allocate(1024).setAutoExpand(true);
                    temp.put(buffer);
                    temp.flip();
                    in.sweep();
                    in.put(temp);
                    positionValue = in.position();
                    flag = true;
                }
                Constants.ctx .reset();
                i = 0;
                return MessageDecoderResult.OK;
            } else {
                Constants.ctx .setBuffer(buffer);
                Constants.NotFill=true;
                Constants.Success=false;
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
        PSAP.setFunctionID(bytes[1]);
        byte[] b1 = new byte[14];
        System.arraycopy(bytes, 4, b1, 0, 14);
        PSAP.setLocationandTime(b1);
        PSAP.setSweepModel(((bytes[18]>>6)&0x03));
        PSAP.setFileSendmodel(((bytes[18]>>4)&0x03));
        PSAP.setIsChange((bytes[18] & 0x0f));
        PSAP.setTotalBand((bytes[19] & 0xff));
        PSAP.setPSbandNum((bytes[20] & 0xff));
        byte[] b2 = new byte[1536];
        System.arraycopy(bytes, 21, b2, 0, 1536);
        PSAP.setPSpower(b2);


        //异常频点
        PSAP.setAPbandNum((bytes[1559 + 19] & 0xff));
        PSAP.setAPnum((bytes[1559 + 20] & 0xff));
        byte[] b4 = new byte[30];
        System.arraycopy(bytes, 1559 + 21, b4, 0, 30);
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
