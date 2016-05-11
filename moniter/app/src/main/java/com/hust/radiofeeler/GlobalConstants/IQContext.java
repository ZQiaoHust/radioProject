package com.hust.radiofeeler.GlobalConstants;

import org.apache.mina.core.buffer.IoBuffer;

/**
 * Created by Administrator on 2016/3/25.
 */
public class IQContext {
    public IoBuffer buffer;
    public long length = 6027 ;
    public long matchLength = 0;

    public IQContext() {
        buffer = IoBuffer.allocate(5120).setAutoExpand(true);
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
        this.length = 6027;
        this.matchLength = 0;
    }
}

