package org.luncert.csdn2.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class IO {

    /**
     * 读输入流
     * @return 内容
     */
    public static byte[] read(InputStream inputStream) throws IOException {
		BufferedInputStream buffer = null;
		DataInputStream dataIn = null;
		ByteArrayOutputStream bos = null;
		DataOutputStream dos = null;
		byte[] bArray = null;
		try {
			buffer = new BufferedInputStream(inputStream);
			dataIn = new DataInputStream(buffer);
			bos = new ByteArrayOutputStream();
			dos = new DataOutputStream(bos);
			byte[] buf = new byte[1024];
			while (true) {
				int len = dataIn.read(buf);
				if (len < 0)
					break;
				dos.write(buf, 0, len);
			}
            bArray = bos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (dataIn != null) dataIn.close();
			if (buffer != null) buffer.close();
			if (bos != null) bos.close();
			if (dos != null) dos.close();
		}
		return bArray;
    }
    
}