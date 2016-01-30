package com.example.administrator.testsliding.mina2FPGA.Decode;

import com.example.administrator.testsliding.Bean.AbnormalPoint;
import com.example.administrator.testsliding.Bean.PowerSpectrum;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by jinaghao on 15/11/30.
 */
public class PowerSpectrumDecoder implements MessageDecoder {
    private int i;
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");

    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            i = 0;
            while (in.get() != (byte) 0x55) {
                i++;
            }
            byte functionCode = in.get();
            if (functionCode == 0x0d || functionCode == 0x1d) {
                return MessageDecoderResult.OK;


            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {

        Context ctx = getContext(session);//获取session  的context
        long matchCount = ctx.getMatchLength();//目前已获取的数据
        long length = ctx.getLength();//数据总长度
        IoBuffer buffer = ctx.getBuffer();//数据存入buffer

        in.position(i);
        matchCount += in.remaining();
        ctx.setMatchLength(matchCount);

        if (in.hasRemaining()) {// 如果in中还有数据
            buffer.put(in);// 添加到保存数据的buffer中
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[1613];
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                AbnormalPoint abnormalPoint = Abnormalbyte2Object(b);
                if (abnormalPoint != null) {
                    out.write(abnormalPoint);
                    System.out.println("异常频点解码完成.......");
                }
                PowerSpectrum powerSpectrum = byte2Object(b);
                if (powerSpectrum != null) {
                    out.write(powerSpectrum);
                    System.out.println("功率谱解码完成.......");
                }

                //粘包的处理
                if (buffer.remaining() > 0) {
                    IoBuffer temp = IoBuffer.allocate(1024).setAutoExpand(true);
                    temp.put(buffer);
                    temp.flip();
                    in.sweep();
                    in.put(temp);
                }
                ctx.reset();
                i=0;
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


    private AbnormalPoint Abnormalbyte2Object(byte[] bytes) {
        AbnormalPoint abnormalPoint = new AbnormalPoint();
        if (bytes.length >= 1613) {
            abnormalPoint.setBandNum((bytes[1559 + 18] & 0xff));
            abnormalPoint.setNum((bytes[1559 + 19] & 0xff));
            byte[] b2 = new byte[30];
            System.arraycopy(bytes, 1559 + 20, b2, 0, 30);
            abnormalPoint.setPower(b2);
        }
        return abnormalPoint;
    }


    private PowerSpectrum byte2Object(byte[] bytes) {
        PowerSpectrum powerSpectrum = new PowerSpectrum();
        if (bytes.length >= 1560) {
            powerSpectrum.setFunctionID(bytes[1]);
            //位置信息和时间
            byte[] b1 = new byte[14];
            System.arraycopy(bytes, 4, b1, 0, 14);
            powerSpectrum.setLocationandTime(b1);
            powerSpectrum.setSweepModel(((bytes[18]&0xff)>>6));
            powerSpectrum.setFileSendmodel((((bytes[18]&0xff)>> 4) & 0x03));
            powerSpectrum.setIsChange((bytes[18] & 0x0f));
            powerSpectrum.setTotalBand((bytes[19] & 0xff));
            powerSpectrum.setBandNum((bytes[20] & 0xff));
            byte[] b3 = new byte[3];
            System.arraycopy(bytes, 18, b3, 0, 3);
            powerSpectrum.setSweepModel2bandNum(b3);
            byte[] b2 = new byte[1536];
            System.arraycopy(bytes, 21, b2, 0, 1536);
            powerSpectrum.setPower(b2);
        }
        return powerSpectrum;
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
     */
    private class Context {
        public IoBuffer buffer;
        public long length = 1560;
        public long matchLength = 0;

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

        public void reset() {
            this.buffer.clear();
            this.length = 1560;
            this.matchLength = 0;
        }
    }

}
