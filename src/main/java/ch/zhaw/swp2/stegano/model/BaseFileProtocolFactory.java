package ch.zhaw.swp2.stegano.model;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The BaseFileProtocolFactory calculates the Header, based on the Base-File and
 * the Hidden-File, and has to be placed at the beginning of every modified
 * Base-File
 * 
 * @author rest
 * 
 */
public class BaseFileProtocolFactory {

	private static final int MAX_POLLUTION = 8;
	public static final int HEADER_BYTE_LENGTH = 8;

	public BaseFileProtocolFactory() {
	}

	/**
	 * This Method generates the Header and returns it in a Byte-Array.
	 * 
	 * @param inBaseFile
	 * @param inHiddenFile
	 * @return a Byte-Array representing the Header
	 * @throws IOException
	 */
	public static byte[] generateHeader(String inHiddenFileExtension, int inHiddenFileLength, byte inPollution)
			throws IllegalArgumentException, IOException {

		if (inPollution < 1 || inPollution > MAX_POLLUTION) {
			throw new IllegalArgumentException();
		}
		if (inHiddenFileExtension.length() != 3) {
			throw new IllegalArgumentException();
		}

		byte[] byteHFExtension = inHiddenFileExtension.getBytes("ASCII");
		byte[] byteHFLength = convertIntToByteArray(inHiddenFileLength);
		byte pollution = inPollution;

		int headerByteLength = byteHFExtension.length + byteHFLength.length + 1;

		if (headerByteLength != HEADER_BYTE_LENGTH) {
			throw new IllegalArgumentException();
		}
		ByteBuffer bheader = ByteBuffer.allocate(headerByteLength);

		bheader.put(byteHFExtension);
		bheader.put(byteHFLength);
		bheader.put(pollution);
		return bheader.array();
	}

	private static byte[] convertIntToByteArray(int inLength) {

		return ByteBuffer.allocate(4).putInt(inLength).array();
	}
}
