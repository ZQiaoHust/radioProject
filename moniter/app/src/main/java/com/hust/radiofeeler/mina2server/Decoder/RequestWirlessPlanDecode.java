package com.hust.radiofeeler.mina2server.Decoder;


import com.hust.radiofeeler.bean2server.File_ServiceRadio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;

import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/11/16.
 */
public class RequestWirlessPlanDecode implements MessageDecoder  {
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if(in.remaining()<10){
            return MessageDecoderResult.NEED_DATA;
        }
        else{
            in.position(8);
            byte b1=in.get();
            byte b2=in.get();
            if((b2==(byte)0xB7)&&(b1==0x55)){
                return MessageDecoderResult.OK;
            }
            else {
                return  MessageDecoderResult.NOT_OK;
            }
        }
    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        //=================结合CumulativeProtocolDecoder================//
        Context ctx =getContext(session);//获取session  的context

        long matchCount=ctx.getMatchLength();//目前已获取的数据
        long length=ctx.getLength();//数据总长度
        IoBuffer buffer=ctx.getBuffer();//数据存入buffer

        //第一次取数据
        if(length==0){
            length=in.getLong();
            //保存第一次获取的长度
            ctx.setLength(length);
            matchCount=in.remaining();
            ctx.setMatchLength(matchCount);
        }
        else{
            matchCount+=in.remaining();
            ctx.setMatchLength(matchCount);
        }
        if (in.hasRemaining()) {// 如果buff中还有数据
            if(matchCount< length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[(int) length];
                byte[] temp = new byte[(int) length];
                in.get(temp,0, (int) (length-buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);
                File_ServiceRadio file = new File_ServiceRadio();
                file.setFileContent(b);
                out.write(file);

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
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput)
            throws Exception {

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
     *
     */
    private class Context {
        public IoBuffer buffer;
        public long length = 0;
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

        public  void reset(){
            this.buffer.clear();
            this.length=0;
            this.matchLength=0;
        }
    }

}
