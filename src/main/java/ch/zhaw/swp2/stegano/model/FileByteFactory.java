/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;

/**
 * 
 * @author rest
 */
public class FileByteFactory {

	private static final int MAX_BYTE_CONVERT = 100000;

	public static byte[] getByteArrayFromFile(File inFile) throws IOException {
		FileInputStream fis = new FileInputStream(inFile);

		byte[] byteArrayHiddenFile = new byte[getFileLength(inFile)];
		fis.read(byteArrayHiddenFile);
		fis.close();
		return byteArrayHiddenFile;
	}

	// Länge des HiddenFile auslesen
	public static int getFileLength(File inFile) throws IllegalArgumentException {
		long length = inFile.length();
		if (length < Integer.MIN_VALUE || length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Hidden File ist to big");
		}
		return (int) length;
	}

	public static String bytesToHexString(byte[] bytes) {
		BigInteger bi = new BigInteger(bytes);
		return bi.toString(16);

		// StringBuilder sb = new StringBuilder(bytes.length * 2);

		// Formatter formatter = new Formatter(sb);
		// for (byte b : bytes) {
		// formatter.format("%02x\t", b);
		// }

		// return sb.toString().toUpperCase();
	}

	public static String convertToHex(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		int innerBytesCounter = 0;
		int byteCounter = 0;
		int value = 0;
		StringBuilder sbHex = new StringBuilder();
		// StringBuilder sbText = new StringBuilder();
		StringBuilder sbResult = new StringBuilder();

		while (((value = is.read()) != -1) && (byteCounter < MAX_BYTE_CONVERT)) {
			// convert to hex value with "X" formatter
			sbHex.append(String.format("%02X", value));

			// If the chracater is not convertable, just print a dot symbol "."
			// if (!Character.isISOControl(value)) {
			// sbText.append((char)value);
			// }else {
			// sbText.append(".");
			// }

			// if 16 bytes are read, reset the counter,
			// clear the StringBuilder for formatting purpose only.
			if (innerBytesCounter == 32) {
				sbResult.append(sbHex).append("\n");
				// sbResult.append(sbHex).append("      ").append(sbText).append("\n");
				sbHex.setLength(0);
				// sbText.setLength(0);
				innerBytesCounter = 0;
			} else {
				sbHex.append(" ");
				innerBytesCounter++;
			}
			byteCounter += 4;
		}

		// if still got content
		// if(bytesCounter!=0){
		// //add spaces more formatting purpose only
		// for(; bytesCounter<16; bytesCounter++){
		// 1 character 3 spaces
		// sbHex.append("   ");
		// }
		// sbResult.append(sbHex).append("      ").append(sbText).append("\n");
		// }

		// out.print(sbResult);
		is.close();
		return sbResult.toString();
	}
}
