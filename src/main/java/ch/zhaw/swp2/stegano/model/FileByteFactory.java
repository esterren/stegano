/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * The FileByteFactory provides different byte operations with the Files, which
 * are used in the Stegano Algorithms
 * 
 * @author Renato Estermann
 */
public class FileByteFactory {

	/**
	 * This method returns a Byte array from the given file.
	 * 
	 * @param inFile
	 *            the File which should be read and represented in the returned
	 *            array.
	 * @return a Byte array representation of the file.
	 * @throws IOException
	 *             when unable to read the file.
	 */
	public static byte[] getByteArrayFromFile(File inFile) throws IOException {
		FileInputStream fis = new FileInputStream(inFile);

		byte[] byteArrayHiddenFile = new byte[getFileLength(inFile)];
		fis.read(byteArrayHiddenFile);
		fis.close();
		return byteArrayHiddenFile;
	}

	/**
	 * This method can be used to get the length of a file, in bytes. Usefull
	 * for the Stegano algorithms to finde out the length of the HiddenFile.
	 * 
	 * @param inFile
	 *            the file
	 * @return The length, in bytes, of the file.
	 * @throws IllegalArgumentException
	 *             when the file length is greater than Integer.MIN_VALUE or
	 *             less than Integer.MIN_VALUE
	 */
	public static int getFileLength(File inFile) throws IllegalArgumentException {
		long length = inFile.length();
		if (length < Integer.MIN_VALUE || length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Hidden File ist to big");
		}
		return (int) length;
	}
}
