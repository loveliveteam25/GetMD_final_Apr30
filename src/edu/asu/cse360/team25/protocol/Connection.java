package edu.asu.cse360.team25.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;


public abstract class Connection {

	protected Socket socket;

	protected InputStream in;
	protected OutputStream out;

	// buffer for receive
	protected byte[] header = new byte[2];
	protected byte[] length = new byte[4];
	protected byte[] body = new byte[10000];

	// buffer for send
	protected byte[] send = new byte[10000];

	
	
	protected Connection() {
		
	}
	
	
	// The format of message is <2 byte magic number> <4 byte msg body length> <variable length msg body as string>
	
	
	protected void sendMsg(String msg) throws IOException {
		
		byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
		
		send[0] = 0x6D;
		send[1] = 0x64;
		
		int len = bytes.length;
		String lstr = String.format("%04d", len);
		byte[] lbytes = lstr.getBytes(StandardCharsets.UTF_8);
		
		// TODO: check the length of lbytes, i.e. it must not be greater than 4
		
		for(int i = 0; i < 4; i++) {
			send[2 + i] = lbytes[i];
		}
		
		for(int i = 0; i < len; i++) {
			send[6 + i] = bytes[i];
		}
		
		out.write(send, 0, len + 6);
		out.flush();
	}
	
	protected boolean receiveBytes(byte[] buff, int n) throws IOException {
		
		int ret = in.read(buff);
		if(ret < 0) {
			// EOF, i.e. closed by the other end
			return false;
		}

		while (ret < n) {
			int k = in.read(buff, ret, n - ret);
			if(k < 0) {
				// EOF, i.e. closed by the other end
				return false;
			}
			ret += k;
		}

		return true;
	}
	
	protected String receiveMsg() throws IOException {

		// read the header
		boolean ret = receiveBytes(header, header.length);
		if(!ret)
			return null;
		
		// check the header
		if(header[0] != 0x6D || header[1] != 0x64)
			return null;
		
		// read the length
		ret = receiveBytes(length, length.length);
		if(!ret)
			return null;
		
		// parse the length
		int len = Integer.parseInt(new String(length, StandardCharsets.UTF_8));
		
		// read the msg body
		ret = receiveBytes(body, len);
		if(!ret)
			return null;

		return new String(body, 0, len, StandardCharsets.UTF_8);
	}

}
