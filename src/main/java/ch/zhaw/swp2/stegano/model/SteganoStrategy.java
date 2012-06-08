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
	 * 
	 * @return is the modified BaseFile with the hidden Information
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

	public List<String> getFormatedBaseFileHexString();

	public List<String> getFormatedModBaseFileHexString();

}
