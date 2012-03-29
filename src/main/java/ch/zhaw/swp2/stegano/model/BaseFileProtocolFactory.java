package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.io.IOException;

/**
 * The BaseFileProtocolFactory calculates the Header, based on the Base-File and
 * the Hidden-File, and has to be placed at the beginning of every modified
 * Base-File
 * 
 * @author rest
 * 
 */
public class BaseFileProtocolFactory {

	private byte[] _byteExtension;
	private byte _bytePollution;
	private static final int MAX_POLLUTION = 8;
	private String _hiddenFileExtension;

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
	public byte[] generateHeader(File inBaseFile, File inHiddenFile, int pollution) throws IllegalArgumentException,
			IOException {

		if (inBaseFile == null || inHiddenFile == null) {
			throw new IllegalArgumentException();
		}
		if (pollution < 1 || pollution > MAX_POLLUTION) {
			throw new IllegalArgumentException();
		}

		_hiddenFileExtension = FileNameFactory.getExtension(inHiddenFile);
		if (_hiddenFileExtension.length() != 3) {
			throw new IllegalArgumentException();
		}

		_byteExtension = _hiddenFileExtension.getBytes("ASCII");
		// _bytePollution =
		return null;
	}
}
