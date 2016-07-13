package com.hust.radiofeeler.mina2server.Decoder;

import com.hust.radiofeeler.bean2server.FileToServerReply;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;
import org.apache.mina.handler.demux.ExceptionHandler;

import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Created by Administrator on 2016/4/19.
 */
public class FileToServerReplyDecoder implements MessageDecoder {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");

    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte b1 = in.get();
            byte b2 = in.get();
            if ((b1 == (byte) 0xff) && (b2 == 0x00)) {
                return MessageDecoderResult.OK;
            } else {
                return MessageDecoderResult.NOT_OK;
            }
        }

    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        CharsetDecoder ce = Charset.forName("UTF-8").newDecoder();
        Context ctx = getContext(session);//获取session  的context

        int matchCount = ctx.getMatchLength();//目前已获取的数据
        int length = ctx.getLength();//数据总长度
        IoBuffer buffer = ctx.getBuffer();//数据存入buffer


//第一次取数据
        if (length == 0) {
            byte b1=in.get();
            byte b2=in.get();
            short len = in.getShort();//文件名长度
            length = len;
            //保存第一次获取的长度
            ctx.setLength(length);
            matchCount = in.remaining();
            ctx.setMatchLength(matchCount);
        } else {
            matchCount += in.remaining();
            ctx.setMatchLength(matchCount);
        }

        if (in.hasRemaining()) {// 如果buff中还有数据
            if (matchCount < length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[length];
                in.get(b, 0, length - buffer.position());//最后一次in的数据可能有多的
                buffer.put(b);

                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();

                String name = buffer.getString(length, ce);
                FileToServerReply reply = new FileToServerReply();
                reply.setFileName(name);
                out.write(reply);

                ctx.reset();
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

    /////////////////////////////////////结合CumulativeProtocolDecoder/////////////////////////////////////////////////
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
        public int length = 0;
        public int matchLength = 0;

        public Context() {
            buffer = IoBuffer.allocate(50).setAutoExpand(true);
        }

        public void setBuffer(IoBuffer buffer) {
            this.buffer = buffer;
        }

        public void setLength(int length) {
            this.length = length;
        }

        public void setMatchLength(int matchLength) {
            this.matchLength = matchLength;
        }

        public IoBuffer getBuffer() {

            return buffer;
        }

        public int getLength() {
            return length;
        }

        public int getMatchLength() {
            return matchLength;
        }

        public void reset() {
            this.buffer.sweep();
            this.length = 0;
            this.matchLength = 0;
        }
    }
}
