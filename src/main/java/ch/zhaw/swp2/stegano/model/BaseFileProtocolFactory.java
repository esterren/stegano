package ch.zhaw.swp2.stegano.model;

import java.io.File;

/**
 * The BaseFileProtocolFactory calculates the Header, based on the Base-File and
 * the Hidden-File, and has to be placed at the beginning of every modified
 * Base-File
 * 
 * @author rest
 * 
 */
public final class BaseFileProtocolFactory {

	private BaseFileProtocolFactory() {
	}

	/**
	 * This Method generates the Header and returns it in a Byte-Array.
	 * 
	 * @param inBaseFile
	 * @param inHiddenFile
	 * @return a Byte-Array representing the Header
	 */
	public static final byte[] generateHeader(File inBaseFile, File inHiddenFile) {

		if (inBaseFile == null || inHiddenFile == null) {
			throw new IllegalArgumentException();
		}
		return null;
	}

}
