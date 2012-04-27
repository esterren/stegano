package ch.zhaw.swp2.stegano.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
	public static final int HEADER_LENGTH = 8;
	private static final int EXTENSION_STARTBYTE_POS = 0;
	private static final int LENGTH_STARTBYTE_POS = 3;
	private static final int POLLUTION_STARTBYTE_POS = 7;
	private static final String EXTENSION_ENCODING = "US-ASCII";

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

		byte[] byteHFExtension = inHiddenFileExtension.getBytes(EXTENSION_ENCODING);
		byte[] byteHFLength = convertIntToByteArray(inHiddenFileLength);
		byte pollution = inPollution;

		int headerByteLength = byteHFExtension.length + byteHFLength.length + 1;

		if (headerByteLength != HEADER_LENGTH) {
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

	public static int getLengthFromHeader(byte[] inHeader) throws IllegalArgumentException {
		checkHeaderLength(inHeader);
		// ByteBuffer buffer = ByteBuffer.wrap(inHeader, 3, 4);
		return ByteBuffer.wrap(inHeader).getInt(LENGTH_STARTBYTE_POS);
	}

	public static String getExtensionFromHeader(byte[] inHeader) throws IllegalArgumentException,
			UnsupportedEncodingException {
		checkHeaderLength(inHeader);
		// TODO refactor this ugly Fix.
		// ByteBuffer buffer = ByteBuffer.wrap(inHeader, 0, 3);
		byte[] b = { inHeader[0], inHeader[1], inHeader[2] };
		// String outExtension = new String(b);
		// return new String(ByteBuffer.wrap(inHeader, 0, 3).array());

		// return outExtension;
		// return ByteBuffer.wrap(b).asCharBuffer().toString();
		return new String(b, EXTENSION_ENCODING);
	}

	public static byte getPollutionFromHeader(byte[] inHeader) {
		checkHeaderLength(inHeader);
		return ByteBuffer.wrap(inHeader).get(POLLUTION_STARTBYTE_POS);
	}

	private static void checkHeaderLength(byte[] inHeader) throws IllegalArgumentException {
		if (inHeader.length != 8) {
			throw new IllegalArgumentException();
		}
	}
}
