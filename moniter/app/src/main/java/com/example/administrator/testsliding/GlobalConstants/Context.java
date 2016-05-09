package com.example.administrator.testsliding.GlobalConstants;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * 实时频谱数据解码器  用来封转当前解码器中的一些公共数据，主要是用于大数据解析
 * Created by jinaghao on 16/1/9.
 */
public class Context {
    public IoBuffer buffer;
    public long length = 1614 ;
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
        this.length = 1614;
        this.matchLength = 0;

    }

}
