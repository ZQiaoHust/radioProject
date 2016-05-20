package com.hust.radiofeeler.mina2server.Decoder;


import com.hust.radiofeeler.bean2server.LocationAbnormalReply;
import com.hust.radiofeeler.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

/**异常频点定位解码器
 * Created by ZengQiao on 2015/11/16.
 */
public class LocationAbnormalDecode implements MessageDecoder  {
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
            if((b2==(byte)0xB4)&&(b1==0x55)){
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
               LocationAbnormalReply reply=new LocationAbnormalReply();
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
    private LocationAbnormalReply Byte2Object(byte[] b){
        LocationAbnormalReply reply=new LocationAbnormalReply();
        String str=null;
        String longstyle=null;
        String latstyle=null;
        int height=0;

        //频率
        double freq=((b[4] & 0xff) << 6) + (b[5] & 0x3f)+
                compute.TenbitDecimal2float(b[5],b[6]);
        reply.setAbFreq(freq);
        //带宽
        float band=(b[7]&0x3f)+compute.TenbitDecimal2float(b[7],b[8]);
        reply.setaBand(band);
        //调制方式
        reply.setModem(compute.QueryModem(b[9]));
        float f2=0;
        for(int j=1;j<9;j++){
            f2+=((b[11]>>(8-j))&0x01)*Math.pow(2,-j);
        }
        float modemPara=(b[10]&0xff)+f2;
        reply.setModemPara(modemPara);

        //位置
        //判断位置
        if(b[12]==0x00)
            longstyle="E";
        else if(b[12]==0x01){
            longstyle="W";
        }
        float longtitude=(b[13]&0xff)+compute.BitDecimal2float(b[14],b[15]);

        if((b[16]>>7)==0x00)
            latstyle="N";
        else if((b[16]>>7)==0x01){
            latstyle="S";
        }
        float latitude=(b[16]&0x7f)+compute.BitDecimal2float(b[17],b[18]);

        if((b[19]>>7)==0x00){
            height=((b[19]&0x7f)<<8)+(b[20]&0xff);
        }else{
            height=-(((b[19]&0x7f)<<8)+(b[20]&0xff));
        }
        reply.setLongtitudeStyle(longstyle);
        reply.setLongitude(longtitude);
        reply.setLatitudeStyle(latstyle);
        reply.setLatitude(latitude);
        reply.setHeight(height);

        //发射功率
        float f1= (float) (((b[21]&0x7f)<<5)+((b[22]>>3)&0x1f)+
                ((b[22]>>2)&0x01)*0.5+((b[22]>>1)&0x01)*0.25+(b[22]&0x01)*0.125);
        if((b[21]>>7)==0x00)
            reply.setEqualPower(f1);
        else
            reply.setEqualPower(-f1);
        //损耗指数
        float r= (float) (((b[23]>>4)&0x0f)+((b[23]>>3)&0x01)*0.5+((b[23]>>2)&0x01)*0.25+
                ((b[23]>>1)&0x01)*0.125+(b[23] & 0x01) * 0.0625);
        reply.setrPara(r);

        float radio= (float) ((b[24]&0xff)/100.0);
        reply.setLiveness(radio);

        //业务属性
        reply.setWork(compute.ChartPlan((b[25]&0xff)));
        reply.setIsLegal(b[26] & 0xff);

        //填入归属单位
        byte[] bytes=new byte[8];
        System.arraycopy(b,27,bytes, 0, 8);
        //str=QueryAsc(bytes);
        str=new String(bytes);
        reply.setOrganizer(str);

        return reply;
    }

//    //归属单位ASCii
//    private String QueryAsc(byte[] bytes){
//        String str=null;
//        for(int i=0;i<8;i++){
//            str+=compute.QueryOrganiaer(bytes[i]);
//        }
//        return  str;
//
//    }
}
