package com.hust.radiofeeler.mina2server.Decoder;

import com.hust.radiofeeler.bean2server.File_MapInterpolation;
import com.hust.radiofeeler.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/12/22.
 */
public class MapInterpolationDecoder implements MessageDecoder {
    private ComputePara computePara=new ComputePara();
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private boolean flag = false;
    private int positionValue = 0;
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {

        if(in.remaining()<10){
            return MessageDecoderResult.NEED_DATA;
        }else {
            in.position(8);
            byte b1=in.get();
            byte b2=in.get();
            if((b1==0x55)&&(b2==(byte)0xD3)){
                return MessageDecoderResult.OK;
            }else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }
    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {

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
        }
        else{
            matchCount+=in.remaining();
        }
        ctx.setMatchLength(matchCount);
        if (in.hasRemaining()) {// 如果buff中还有数据
            buffer.put(in);// 添加到保存数据的buffer中
            if(matchCount< length) {
                buffer.put(in);// 添加到保存数据的buffer中
            }
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                final byte[] b = new byte[(int) length];
                byte[] temp = new byte[(int) length];
                in.get(temp,0, (int) (length-buffer.position()));//最后一次in的数据可能有多的
                buffer.put(temp);
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);

                File_MapInterpolation file=new File_MapInterpolation();
                byte[] b1=new byte[34];
                System.arraycopy(b,4,b1,0,34);
                byte[] b2=new byte[(int) length-41];
                System.arraycopy(b,38,b2,0,(int)length-41);
                file.setShowContent(b1);
                file.setFileContent(b2);
                out.write(file);


                ctx.reset();//清空
                return MessageDecoderResult.OK;

            } else {
                ctx.setBuffer(buffer);
                return MessageDecoderResult.NEED_DATA;
            }
        }
        return MessageDecoderResult.NEED_DATA;
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
    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }
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
