package network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Message {
	public byte cmd;
	private ByteArrayOutputStream os;
	private DataOutputStream dos;
	private ByteArrayInputStream is;
	private DataInputStream dis;
	private byte[] cachedData;

	public Message(int cmd) {
            this.cmd = (byte) cmd;
            this.os = new ByteArrayOutputStream(64);
            this.dos = new DataOutputStream(os);
	}

	public Message(byte cmd, byte[] data) {
            this.cmd = cmd;
            this.cachedData = data;
            this.is = new ByteArrayInputStream(data);
            this.dis = new DataInputStream(is);
	}

	public DataOutputStream writer() {
		return dos;
	}

	public DataInputStream reader() {
		return dis;
	}
        
	public void setReader(DataInputStream newReader) {
		this.dis = newReader;
	}

	public synchronized byte[] getData() {
            if (cachedData != null) {
                return cachedData;
            }
            if (os != null) {
                cachedData = os.toByteArray();
                return cachedData;
            }
            return null;
	}

	public synchronized void cleanup() throws IOException {
            if (os != null) {
                if (cachedData == null) {
                    cachedData = os.toByteArray();
                }
                os.close();
            }
            if (is != null) {
                is.close();
            }
            if (dis != null) {
                dis.close();
            }
            if (dos != null) {
                dos.close();
            }
	}
}
