/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Formatter;

/**
 *
 * @author rest
 */
public class FileByteFactory {
    
    
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
            StringBuilder sb = new StringBuilder(bytes.length * 2);  

            Formatter formatter = new Formatter(sb);  
            for (byte b : bytes) {  
                formatter.format("%02x\t", b);  
            }  

            return sb.toString().toUpperCase();  
    }  
        
}
