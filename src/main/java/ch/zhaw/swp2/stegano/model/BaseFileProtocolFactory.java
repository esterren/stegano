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

	private static byte[] _byteHFExtension;
	private static byte[] _byteHFLength;
	private static byte _pollution;

	private static final int MAX_POLLUTION = 8;

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

		_byteHFExtension = inHiddenFileExtension.getBytes("ASCII");
		_byteHFLength = convertIntToByteArray(inHiddenFileLength);
		_pollution = inPollution;

		ByteBuffer bheader = ByteBuffer.allocate(_byteHFExtension.length + _byteHFLength.length + 1);

		bheader.put(_byteHFExtension);
		bheader.put(_byteHFLength);
		bheader.put(_pollution);
		return bheader.array();
	}

	private static byte[] convertIntToByteArray(int inLength) {

		return ByteBuffer.allocate(4).putInt(inLength).array();
	}
}
