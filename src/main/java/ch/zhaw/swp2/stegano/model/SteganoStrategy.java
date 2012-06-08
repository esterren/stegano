package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.util.List;

public interface SteganoStrategy {
	
	

	/**
	 * This Method is called by the Controller to run the Staganographie
	 * Algorithm of the specific Strategy and hide the inHiddenFile in the
	 * inBaseFile
	 * 
	 * @param inModBaseFile
	 *            the File, where the results are saved to.
	 * @param inBaseFile
	 *            The BaseFile on which the Algorithm will process the hiding.
	 * @param inHiddenFile
	 *            The File to hide.
	 * @param pollution
	 *            The nummber [1-7] of Bits in a Byte which are manipulated by
	 *            the Algorithm to hide the information. Increasing pollution
	 *            increases the amount of size for the HiddenFile
	 * 
	 */
	public void runHide(File inModBaseFile, File inBaseFile, File inHiddenFile, byte pollution) throws Exception;

	/**
	 * This Method is called by the Controller to run the Staganographie
	 * Algorithm of the specific Strategy which will seek the hidden Information
	 * in the modified BaseFile.
	 * 
	 * @param inBaseFile
	 *            The BaseFile on which the Algorithm will process and seek for
	 *            hidden Information/File.
	 * @param inHiddenFileSaveDir
	 *            The directory where the Hiddenfile will be saved.
	 * @return is the absolute Filepath to the Hiddenfile, the
	 *         Steganographie-Algorithm found in the modified BaseFile.
	 */
	public String runSeek(File inModBaseFile, String inHiddenFileSaveDir) throws Exception;

	/**
	 * This method returns a List with the hexadecimal String representation of
	 * the Elements (this could be: Integer, Long or Byte), which are read from
	 * the Basefile in the specific Stegano Algorithm.
	 * 
	 * @return a List with a hexadecimal String representation of each processed
	 *         Element.
	 */
	public List<String> getFormatedBaseFileHexString();

	/**
	 * This method returns a List with the hexadecimal String representation of
	 * the Elements (this could be: Integer, Long or Byte), which are
	 * modiefied/processed by the specific Stegano Algorithm. Each Element in
	 * the returned List corresponds to the Element with the same Index in
	 * getFormatedBaseFileHexString(). Moreover the difference between the two
	 * corresponding elements, represents the modifications made by the Stegano
	 * Algorithm.
	 * 
	 * @return a List with a hexadecimal String representation of each processed
	 *         Element.
	 */
	public List<String> getFormatedModBaseFileHexString();

}
