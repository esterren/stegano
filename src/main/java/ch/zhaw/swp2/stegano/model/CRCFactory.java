package ch.zhaw.swp2.stegano.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public final class CRCFactory {

	public static byte[] getCRC(byte[] data) throws IOException {

		CRC32 crc = new CRC32();
		crc.update(data);

		// convert crc from long to byte-array
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bos);
		dos.writeLong(crc.getValue());
		dos.flush();

		byte[] crcB = bos.toByteArray();

		return crcB;
	};
}
