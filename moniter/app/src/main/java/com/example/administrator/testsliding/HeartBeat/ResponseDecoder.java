package com.example.administrator.testsliding.HeartBeat;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

public class ResponseDecoder implements MessageDecoder {

	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		if (in.remaining() < 2)
			return MessageDecoderResult.NEED_DATA;
		else {
			byte framehead = in.get();
			if (framehead == (byte) 0x77) {
				byte functioncode = in.get();
				if (functioncode == (byte) 0x88) {
//					System.out.println("�����ظ�");
					return MessageDecoderResult.OK;
				} else
					return MessageDecoderResult.NOT_OK;
			} else
				return MessageDecoderResult.NOT_OK;
		}
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		IoBuffer buffer = IoBuffer.allocate(2).setAutoExpand(true);
		int receivecount = 0;
		HEARTBEATRESPONSE heartbeatresponse = new HEARTBEATRESPONSE();
		while (in.hasRemaining()) {
			byte b = in.get();
			buffer.put(b);
			receivecount++;
			if (receivecount == 2) {
				buffer.flip();
				byte[] accept = new byte[2];
				buffer.get(accept);
//				System.out.println(java.util.Arrays.toString(accept));
				heartbeatresponse.setContent(accept);

				out.write(heartbeatresponse);
				return MessageDecoderResult.OK;
			}
		}
		return MessageDecoderResult.NOT_OK;
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1) throws Exception {
		// TODO Auto-generated method stub

	}

}
