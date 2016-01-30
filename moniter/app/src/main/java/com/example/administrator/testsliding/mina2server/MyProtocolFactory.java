package com.example.administrator.testsliding.mina2server;


import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

/**
 * Created by Administrator on 2015/11/16.
 */
public class MyProtocolFactory implements ProtocolCodecFactory {
    private RequestWirlessPlanEncode2 mwirlessEncoder;
    private RequestWirlessPlanDecoder2 mwirlessDocoder;

    public MyProtocolFactory(){
        mwirlessEncoder=new RequestWirlessPlanEncode2();
        mwirlessDocoder=new RequestWirlessPlanDecoder2();

    }
    @Override
    public ProtocolEncoder getEncoder(IoSession ioSession) throws Exception {
        return mwirlessEncoder;
    }

    @Override
    public ProtocolDecoder getDecoder(IoSession ioSession) throws Exception {
        return mwirlessDocoder;
    }
}
