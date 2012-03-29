package ch.zhaw.swp2.stegano.model;

import java.io.File;

public interface SteganoStrategy {

	/**
	 * This Method is called by the Controller to run the Staganographie
	 * Algorithm of the specific Strategy and hide the inHiddenFile in the
	 * inBaseFile
	 * 
	 * @param inBaseFile
	 *            The BaseFile on which the Algorithm will process the hiding.
	 * @param inHiddenFile
	 *            The File to hide.
	 * @param pollution
	 * 
	 * @return is the modified BaseFile with the hidden Information
	 */
	public File runHide(File inBaseFile, File inHiddenFile, int pollution);

	/**
	 * This Method is called by the Controller to run the Staganographie
	 * Algorithm of the specific Strategy which will seek the hidden Information
	 * in the modified BaseFile.
	 * 
	 * @param inBaseFile
	 *            The BaseFile on which the Algorithm will process and seek for
	 *            hidden Information/File.
	 * @return is the a File, the Steganographie-Algorithm found in the modified
	 *         BaseFile.
	 */
	public File runUnHide(File inModBaseFile);

}
