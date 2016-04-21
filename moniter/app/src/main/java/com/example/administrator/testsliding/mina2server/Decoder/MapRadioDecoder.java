package com.example.administrator.testsliding.mina2server.Decoder;

import android.content.Context;
import android.util.Log;

import com.example.administrator.testsliding.bean2server.MapRadioPointInfo;
import com.example.administrator.testsliding.bean2server.MapRadioResult;
import com.example.administrator.testsliding.bean2server.MapRouteResult;
import com.example.administrator.testsliding.compute.ComputePara;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by Administrator on 2015/12/22.
 */
public class MapRadioDecoder implements MessageDecoder {
    private ComputePara computePara=new ComputePara();
    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private boolean flag = false;
    private int positionValue = 0;
    @Override
    public MessageDecoderResult decodable(IoSession ioSession, IoBuffer in) {
        if (flag == true) {
            in.limit(positionValue);
            in.flip();
        }
        if(in.remaining()<10){
            return MessageDecoderResult.NEED_DATA;
        }else {
            in.position(8);
            byte b1=in.get();
            byte b2=in.get();
            if((b1==0x55)&&(b2==(byte)0xB2)){
                return MessageDecoderResult.OK;
            }else {
                return MessageDecoderResult.NOT_OK;
            }
        }
    }
    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       ProtocolDecoderOutput out) throws Exception {
        if (flag == true) {
            in.limit(positionValue);
            in.flip();
        }

        Context ctx =getContext(session);//获取session  的context

        long matchCount=ctx.getMatchLength();//目前已获取的数据
        long length=ctx.getLength();//数据总长度
        IoBuffer buffer=ctx.getBuffer();//数据存入buffer

        Log.d("map","电磁态势相应时间: "+System.currentTimeMillis() );
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
                MapRadioResult map=Byte2Object(b);
                out.write(map);
                System.out.println("解码完成.......");

                if (buffer.remaining() > 0) {
                    IoBuffer temp = IoBuffer.allocate(1024).setAutoExpand(true);
                    temp.put(buffer);
                    temp.flip();
                    in.sweep();
                    in.put(temp);
                    positionValue = in.position();
                    flag = true;
                }
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
    private MapRadioResult Byte2Object(byte[] b){
        MapRadioResult map=new MapRadioResult();
        int length=b.length;
        ArrayList<MapRadioPointInfo> list=new ArrayList<>();

        map.setCentralFreq(((b[4] & 0xff) << 8) + (b[5] & 0xff));
        map.setBand(b[6] & 0xff);
        map.setFreshNum(b[7] & 0xff);
        map.setFreshTime(b[8] & 0xff);
        map.setNx(b[9] & 0xff);
        map.setNy(b[10] & 0xff);
        float f1= (float) (((b[11]>>3)&0x1f)+((b[11]>>2)&0x01)*0.5+((b[11]>>1)&0x01)*0.25+(b[11]&0x01)*0.125);
        map.setDieta(f1);
        for(int i=12;i<length-3;i=i+16){
            MapRadioPointInfo point=new MapRadioPointInfo();
            point.setYear(((b[i]&0xff)<<4)+((b[i+1]>>4)&0x0f));
            point.setMonth(b[i + 1] & 0x0f);
            point.setDate((b[i + 2] >> 3) & 0x1f);
            point.setHour(((b[i + 2] & 0x07) << 2) + (b[i + 3] & 0x03));
            point.setMin((b[i + 3] >> 2) & 0x3f);

            //判断位置
            if(b[i+4]==0x00)
                point.setLongtitudeStyle("E");
            else if(b[i+4]==0x01){
                point.setLongtitudeStyle("W");
            }
            float longtitude=(b[i+5]&0xff)+computePara.BitDecimal2float(b[i+6],b[i+7]);
            point.setLongitude(longtitude);
            if((b[i+8]>>7)==0x00)
                point.setLatitudeStyle("N");
            else if((b[i+8]>>7)==0x01){
                point.setLatitudeStyle("S");
            }
            float latitude=(b[i+8]&0x7f)+computePara.BitDecimal2float(b[i+9],b[i+10]);
            point.setLatitude(latitude);
            int height=((b[i+11]&0x7f)<<8)+(b[i+12]&0xff);
            if((b[i+11]>>7)==0x00){
                point.setHeight(height);
            }else{
                point.setHeight(-height);
            }
            //发射功率
            float ff= (float) (((b[i+13]&0x7f)<<5)+((b[i+14]>>3)&0x1f)+
                    ((b[i+14]>>2)&0x01)*0.5+((b[i+14]>>1)&0x01)*0.25+(b[i+14]&0x01)*0.125);
            if((b[i+13]>>7)==0x00)
                point.setEqualPower(ff);
            else
                point.setEqualPower(-ff);
            //损耗指数
            float f2= (float) (((b[i+15]>>4)&0x0f)+((b[i+15]>>3)&0x01)*0.5+((b[i+15]>>2)&0x01)*0.25+
                    ((b[i+15]>>1)&0x01)*0.125+(b[i+15]&0x01)*0.0625);
            point.setrPara(f2);

            list.add(point);
        }
        map.setMapRadioPointInfoList(list);
        return map;
    }
}
