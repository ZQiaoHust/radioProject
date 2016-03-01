package com.example.administrator.testsliding.mina2FPGA.Decode;

import android.util.Log;

import com.example.administrator.testsliding.Bean.BackgroundPowerSpectrum;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;


/**
 * Created by jinaghao on 15/12/24.
 */
public class BackgroundPowerSpectrumFineDecoder implements MessageDecoder {

    private int i;
    private boolean flag = false;
    private int positionValue = 0;
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private int k;
    private boolean fail = false;//是否收满
    private ComputePara computePara=new ComputePara();

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
            while (in.get() != (byte) 0x55) {
                i++;
                if (i >= in.remaining()) {
                    break;
                }
            }
            byte functionCode = in.get();
            if (functionCode == 0x52) {
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
        Context ctx = getContext(session);//获取session  的context
        long matchCount = ctx.getMatchLength();//目前已获取的数据
        long length = ctx.getLength();//数据总长度
        IoBuffer buffer = ctx.getBuffer();//数据存入buffer

        flag = false;
        /////////////
        if (i > 0) {
            in.position(i);
            i = 0;
        }
///////////////////////////////////////////////////
        matchCount += in.remaining();
        Log.d("abcd", "共收到字节：" + String.valueOf(matchCount));
        ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            buffer.put(in);// 添加到保存数据的buffer中
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                final byte[] b = new byte[1560];
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);
                if (b[0] == (byte) 0x55 && b[1559] == (byte) 0xaa) {
                    BackgroundPowerSpectrum back = byte2Object(b);
                    if (back != null) {
                        out.write(back);
                        System.out.println("背景功率谱解码完成.......");
                    }

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
                ctx.reset();
                i = 0;
                return MessageDecoderResult.OK;
            } else {
                ctx.setBuffer(buffer);
                return MessageDecoderResult.NEED_DATA;
            }
        }
        return MessageDecoderResult.NEED_DATA;
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    private BackgroundPowerSpectrum byte2Object(byte[] bytes) {
        BackgroundPowerSpectrum back = new BackgroundPowerSpectrum();
        back.setTotalBand(((bytes[19] >> 4) & 0x0f));
        back.setNumN((bytes[19] & 0x0f));//扫频总段数的序号
        back.setPSbandNum((bytes[20] & 0xff));
        byte[] b2 = new byte[1536];
        System.arraycopy(bytes, 21, b2, 0, 1536);
         float[] f1=computePara.Bytes2Power(b2);
        back.setPSpower(f1);
        return back;
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
     * //
     */
    private class Context {
        public IoBuffer buffer;
        public long length = 1560;
        public long matchLength = 0;
        public long startTime = 0;

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
            this.length = 1560;
            this.matchLength = 0;
            this.startTime = 0;
        }
    }
}
