package com.example.administrator.testsliding.Bean;

/**
 * Created by jinaghao on 16/1/13.
 */
public class ToServerPowerSpectrumAndAbnormalPoint {
    private String fileName;
    private short fileNameLength;
    private byte[] content;
    private long contentLength;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileNameLength(short fileNameLength) {
        this.fileNameLength = fileNameLength;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public String getFileName() {

        return fileName;
    }

    public short getFileNameLength() {
        return fileNameLength;
    }

    public byte[] getContent() {
        return content;
    }

    public double getContentLength() {
        return contentLength;
    }
}
