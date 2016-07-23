package com.hust.radiofeeler.mina2FPGA.Decode;

import android.util.Log;

import com.hust.radiofeeler.Bean.POAdata;
import com.hust.radiofeeler.Bean.ReceiveRight;
import com.hust.radiofeeler.Bean.ReceiveWrong;
import com.hust.radiofeeler.GlobalConstants.Constants;
import com.hust.radiofeeler.bean2server.FileToServerReply;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.AttributeKey;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jinaghao on 15/12/24.
 */
public class POAdataDecoder implements MessageDecoder {

    private final AttributeKey CONTEXT = new AttributeKey(getClass(),
            "context");
    private int k;
    private ReceiveRight mReceiveRight = new ReceiveRight();
    private ReceiveWrong mReceiveWrong = new ReceiveWrong();
    private int num=0;

    @Override
    public MessageDecoderResult decodable(IoSession session, IoBuffer in) {

        if (in.remaining() < 2) {
            return MessageDecoderResult.NEED_DATA;
        } else {
            byte head = in.get();
            byte functionCode = in.get();
            if ((head == 0x55 || head == 0x66) && functionCode == 0x41) {
                Constants.Isstop = false;
                return MessageDecoderResult.OK;
            } else {
                Constants.Isstop = true;
                return MessageDecoderResult.NOT_OK;
            }
        }

    }

    @Override
    public MessageDecoderResult decode(IoSession session, IoBuffer in,
                                       final ProtocolDecoderOutput out) throws Exception {

    Context ctx = getContext(session);//获取session  的context

    int matchCount = ctx.getMatchLength();//目前已获取的数据
    int length = ctx.getLength();//数据总长度
    IoBuffer buffer = ctx.getBuffer();//数据存入buffer


//第一次取数据
    if (length == 0) {

        length =78;
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
            byte[] bytes = new byte[78];
            buffer.get(bytes);
            if (bytes[1] == (byte) 0x41 && bytes[77] == (byte) 0xaa) {
                POAdata poa = byte2Object(bytes);
                TimerTask task = new TimerTask() {
                    public void run() {
                        //实现自己的延时执行任务
                        Constants.FPGAsession.write(mReceiveRight);
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 20);
                out.write(poa);
                Log.d("POAdataDecoder", "异常频点的个数：" + num);
                Log.d("POAdataDecoder", "当前帧总共段数：" + poa.getTotalBand());
                Log.d("POAdataDecoder", "当前帧所在序号：" + poa.getNumN());

                Constants.NotFill = false;//收成功，NotFill表示没满的变量
            }else{
                Constants.FPGAsession.write(mReceiveWrong);
            }
            ctx.reset();
            return MessageDecoderResult.OK;

        } else {
            ctx.setBuffer(buffer);
            Constants.NotFill = true;
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
    private POAdata byte2Object(byte[] bytes) {
        POAdata poa = new POAdata();
        //位置
        byte[] b1 = new byte[9];
        System.arraycopy(bytes, 4, b1, 0, 9);
        poa.setLocation(b1);
        //时间
        byte[] b2 = new byte[9];
        System.arraycopy(bytes, 13, b2, 0, 9);
        poa.setTime(b2);

        poa.setTotalBand((bytes[23] & 0xff));
        poa.setNumN((bytes[24] & 0xff));//扫频总段数的序号

        num = bytes[22] & 0xff;//异常频点的个数
        if (num != 0) {
            byte[] ab = new byte[num * 5];//异常频点有效信息
            System.arraycopy(bytes, 25, ab, 0, num * 5);
            poa.setAbnormalInfo(ab);
        }
        return poa;
    }
/**
 * 定义一个内部类，用来封转当前解码器中的一些公共数据，主要是用于大数据解析
 */
private class Context {
    public IoBuffer buffer;
    public int length = 0;
    public int matchLength = 0;

    public Context() {
        buffer = IoBuffer.allocate(78).setAutoExpand(true);
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

