package com.hust.radiofeeler.mina2server.Decoder;


import com.hust.radiofeeler.bean2server.StationCurrentReply;
import com.hust.radiofeeler.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**
 * Created by Administrator on 2015/11/16.
 */
public class StationCurrentDecode implements MessageDecoder  {
    private ComputePara compute=new ComputePara();//计算器
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
            if((b2==(byte)0xB6)&&(b1==0x55)){
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
            length=in.getLong();//回应长度
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
                StationCurrentReply reply=new StationCurrentReply();
                reply=Byte2Object(b);
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

    /**
     * 将解析数据填入对象
     */
    private StationCurrentReply Byte2Object(byte[] b){
        StationCurrentReply reply=new StationCurrentReply();
        String str=null;
        String longstyle=null;
        String latstyle=null;
        int height=0;
        //填入归属单位
        byte[] bytes=new byte[8];
        System.arraycopy(b,4,bytes,0,8);
        str=new String(bytes);
        reply.setASICIIId(str);
        reply.setIDcard(((b[12]&0xff)<<16)+((b[13]&0xff)<<8)+(b[14]&0xff));

        //位置
        //判断位置
        if(b[15]==0x00)
            longstyle="E";
        else if(b[15]==0x01){
            longstyle="W";
        }
        float longtitude=(b[16]&0xff)+compute.BitDecimal2float(b[17],b[18]);

        if((b[19]>>7)==0x00)
            latstyle="N";
        else if((b[19]>>7)==0x01){
            latstyle="S";
        }
        float latitude=(b[19]&0x7f)+compute.BitDecimal2float(b[20],b[21]);

        if((b[22]>>7)==0x00){
            height=((b[22]&0x7f)<<8)+(b[23]&0xff);
        }else{
            height=-(((b[22]&0x7f)<<8)+(b[23]&0xff));
        }
        reply.setLongtitudeStyle(longstyle);
        reply.setLongitude(longtitude);
        reply.setLatitudeStyle(latstyle);
        reply.setLatitude(latitude);
        reply.setHeight(height);
       //频率
        double freq=((b[24] & 0xff) << 6) + (b[25] & 0x3f)+
                compute.TenbitDecimal2float(b[25],b[26]);
        reply.setCentralFreq(freq);
        //功率
        //发射功率

        float f1= (float) (((b[27]&0x7f)<<5)+((b[28]>>3)&0xff)+
                ((b[28]>>2)&0x01)*0.5+((b[28]>>1)&0x01)*0.25+(b[28]&0x01)*0.125);
        if((b[27]>>7)==0x00)
            reply.setEqualPower(f1);
        else
            reply.setEqualPower(-f1);
        //损耗指数
        float r= (float) (((b[29]>>4)&0xff)+((b[29]>>3)&0x01)*0.5+((b[29]>>2)&0x01)*0.25+((b[29]>>1)&0x01)*0.125+(b[29]&0x01)*0.0625);
        reply.setrPara(r);
        //带宽
        float band=(b[30]&0x1f)+compute.TenbitDecimal2float(b[30],b[31]);
        reply.setaBand(band);
        //调制方式
        reply.setModem(compute.QueryModem(b[32]));

        float f2=0;
        for(int j=1;j<9;j++){
            f2+=((b[34]>>(8-j))&0x01)*Math.pow(2,-j);
        }
        float modemPara=(b[33]&0xff)+f2;
        reply.setModemPara(modemPara);
        //业务属性
       reply.setWork(compute.ChartPlan((b[35] & 0xff)));

        float radio= (float) ((b[36]&0xff)/100.0);
        reply.setLiveness(radio);
       int rr=b[37]&0xff;
        if (rr == 0)
            reply.setRule("合法");
        else
            reply.setRule("非法");
        return reply;
    }


}
