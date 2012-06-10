package ch.zhaw.swp2.stegano.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * The BaseFileProtocolFactory calculates the Header, based on the Base-File and
 * the Hidden-File, and has to be placed at the beginning of every modified
 * Base-File
 * 
 * @author Renato Estermann
 * 
 */
public class BaseFileProtocolFactory {

	private static final int MAX_POLLUTION = 8;
	public static final int HEADER_LENGTH = 8;
	private static final int LENGTH_STARTBYTE_POS = 3;
	private static final int POLLUTION_STARTBYTE_POS = 7;
	private static final String EXTENSION_ENCODING = "US-ASCII";

	public BaseFileProtocolFactory() {
	}

	/**
	 * This Method generates the Header and returns it in a Byte-Array.
	 * 
	 * @param inHiddenFileExtension
	 *            the File-Extension of the Hidden file. This String must have a
	 *            length of 3. (e.t.: "txt" or "jpg")
	 * @param inHiddenFileLength
	 *            the Length in Number of bytes of the HiddenFile
	 * @param inPollution
	 *            one byte with the number of bit polluted.
	 * @return a Byte-Array representing the Header
	 * @throws IllegalArgumentException
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

	/**
	 * Converts an int value into a byte array
	 * 
	 * @param inLength
	 *            int value to convert
	 * @return the byte array with the representation of the int value
	 */
	private static byte[] convertIntToByteArray(int inLength) {

		return ByteBuffer.allocate(4).putInt(inLength).array();
	}

	/**
	 * Returns the Length (number of bytes) of the HiddenFile, which is in the
	 * Header
	 * 
	 * @param inHeader
	 *            the byte array with the Header
	 * @return the length (number of bytes) of the HiddenFile. The Algorithm has
	 *         to process this number of bytes to get the whole HiddenFile out
	 *         of the BaseFile.
	 * @throws IllegalArgumentException
	 *             if the Header byte array has not the length of 8.
	 */
	public static int getLengthFromHeader(byte[] inHeader) throws IllegalArgumentException {
		checkHeaderLength(inHeader);
		return ByteBuffer.wrap(inHeader).getInt(LENGTH_STARTBYTE_POS);
	}

	/**
	 * Returns the String with the File-Extension of the HiddenFile from the
	 * Header
	 * 
	 * @param inHeader
	 *            byte array with the Header
	 * @return the String with the Filename-Extension.
	 * @throws IllegalArgumentException
	 *             if the byte array length of the Header is not 8.
	 * @throws UnsupportedEncodingException
	 *             if the Encoding is not supported
	 */
	public static String getExtensionFromHeader(byte[] inHeader) throws IllegalArgumentException,
			UnsupportedEncodingException {
		checkHeaderLength(inHeader);
		byte[] b = { inHeader[0], inHeader[1], inHeader[2] };
		return new String(b, EXTENSION_ENCODING);
	}

	/**
	 * Returns the Pollution from the Header
	 * 
	 * @param inHeader
	 *            byte array with the Header
	 * @return a byte with the number of bits for the pollution
	 */
	public static byte getPollutionFromHeader(byte[] inHeader) {
		checkHeaderLength(inHeader);
		return ByteBuffer.wrap(inHeader).get(POLLUTION_STARTBYTE_POS);
	}

	/**
	 * Checks the length of the Header
	 * 
	 * @param inHeader
	 *            the byte array with the Header
	 * @throws IllegalArgumentException
	 *             if the Header length is not 8.
	 */
	private static void checkHeaderLength(byte[] inHeader) throws IllegalArgumentException {
		if (inHeader.length != HEADER_LENGTH) {
			throw new IllegalArgumentException();
		}
	}
}
