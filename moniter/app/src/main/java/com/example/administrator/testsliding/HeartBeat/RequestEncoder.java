package com.example.administrator.testsliding.HeartBeat;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

public class RequestEncoder implements MessageEncoder<HEARTBEATREQUEST> {

	@Override
	public void encode(IoSession session, HEARTBEATREQUEST heartbeatrequest, ProtocolEncoderOutput out) throws Exception {
		IoBuffer buffer=IoBuffer.allocate(2).setAutoExpand(true);
		byte[] send=heartbeatrequest.getContent();
		
//		System.out.print(java.util.Arrays.toString(send));
		buffer.put(send);
		buffer.flip();
		out.write(buffer);
		
	}

}