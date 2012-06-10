package ch.zhaw.swp2.stegano.model;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.zip.CRC32;

/**
 * The CRCFactory Class is used to generate the Checksum of the whole given
 * Information. It is used to generate it and to check if the hidden Information
 * was manipulated.
 * 
 * @author Renato Estermann
 * 
 */
public final class CRCFactory {

	/**
	 * This method returns a byte array with the generated CRC32, based on the
	 * input.
	 * 
	 * @param data
	 *            a byte array with the whole information, on which the CRC32
	 *            will be generated
	 * @return a byte with the generated CRC.
	 * @throws IOException
	 */
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
