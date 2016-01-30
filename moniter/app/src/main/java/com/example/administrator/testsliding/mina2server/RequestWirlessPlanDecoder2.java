package com.example.administrator.testsliding.mina2server;

import com.example.administrator.testsliding.bean2server.File_ServiceRadio;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;


/**
 * Created by Administrator on 2015/11/20.
 */
public class RequestWirlessPlanDecoder2 extends CumulativeProtocolDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");

    @Override
    protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out)
            throws Exception {
        int startPosition = in.position();
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
            if (matchCount >= length) {// 如果已经发送的数据的长度>=目标数据的长度,则进行解码
                byte[] b = new byte[(int) length];
                // 一定要添加以下这一段，否则不会有任何数据,因为，在执行in.put(buffer)时buffer的起始位置已经移动到最后，所有需要将buffer的起始位置移动到最开始
                buffer.flip();
                buffer.get(b);
                File_ServiceRadio file = new File_ServiceRadio();
                file.setFileContent(b);
                out.write(file);
                System.out.println("解码完成.......");

                if(buffer.remaining() > 0) {//如果读取一个完整包内容后还粘了包，就让父类再调用一次，进行下一次解析
                    IoBuffer temp = IoBuffer.allocate(1024).setAutoExpand(
                            true);
                    temp.put(buffer);
                    temp.flip();
                    in.clear();
                    in.put(temp);
                    return true;
                }
                ctx.reset();//清空


            } else {
                ctx.setBuffer(buffer);
                return false;
            }
        }
        return false;
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
