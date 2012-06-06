package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * The IfcUserInterface is the Interface, which has to be implemented in any UI.
 * This Interface is used by the Controller Class and provides all basic methods
 * for interaction with the UI.
 * 
 * 
 * 
 * @author Renato Estermann
 * 
 */
public interface IfcUserInterface {
	/**
	 * This Method provides displaying an Error message.
	 * 
	 * @param inErrorMsg
	 *            String with the Error message itself.
	 */
	public void displayErrorMsg(String inErrorMsg);

	/**
	 * This Method provides displaying an Warning message.
	 * 
	 * @param inWarnMsg
	 *            String with the Warn message itself.
	 */
	public void displayWarnMsg(String inWarnMsg);

	/**
	 * This Method provides displaying an Info message.
	 * 
	 * @param inInfoMsg
	 *            String with the Info message itself.
	 */
	public void displayInfoMsg(String inInfoMsg);

	/**
	 * This Method provides displaying the BaseFile
	 */
	public void displayBaseFile();

	/**
	 * This Method provides displaying the modified Basefile
	 */
	public void displayModBaseFile();

	/**
	 * This Method provides displaying the seeked Hiddenfile
	 */
	public void displaySeekedHiddenFile(String inHiddenFilePath) throws Exception;

	/**
	 * This Method is called by the Controller class and sets the
	 * ActionListener. On an actionPerformed, the Controller will run the
	 * Stegano-Algorithm.
	 * 
	 * @param runStegano
	 */
	public void setListeners(ActionListener runHideStegano, ActionListener runSeekStegano);

	/**
	 * This Method is called by the Controller to get the BaseFile.
	 * 
	 * @return is the File BaseFile for the Stegano-Algorithm.
	 */
	public File getBaseFile();

	/**
	 * This Method is called by the Controller to get the HiddenFile.
	 * 
	 * @return is the File HiddenFile for the Stegano-Algorithm.
	 */
	public File getHiddenFile();

	/**
	 * This Method is called by the Controller to get the modified BaseFile.
	 * 
	 * @return is the File BaseFile for the Stegano-Algorithm.
	 */
	public File getModifiedBaseFile();

	/**
	 * This Method is called by the Controller to get the HiddenFile
	 * Save-directory.
	 * 
	 * @return is the save-directory for the Stegano-Algorithm. (Where the
	 *         extracted HiddenFile will be saved)
	 */
	public String getHiddenFileSaveDir();

	/**
	 * This Method sets the List with the hexadecimal representation of the
	 * values from the Basefile, read by the Stegano-Algorithm, in the user
	 * interface.
	 * 
	 * @param inHexString
	 *            a List with the hexadecimal representation of the read values
	 * @throws IllegalArgumentException
	 *             if the List is null, an IllegalArgumentException is thrown.
	 */
	public void setBaseFileHexString(List<String> inHexString) throws IllegalArgumentException;

	/**
	 * This Method sets the List with the hexadecimal representation of the
	 * values, which are written to the modified Basefile, in the user
	 * interface.
	 * 
	 * @param inHexString
	 *            a List with the hexadecimal representation of the read values
	 * @throws IllegalArgumentException
	 *             if the List is null, an IllegalArgumentException is thrown.
	 */
	public void setModBaseFileHexString(List<String> inHexString) throws IllegalArgumentException;

}
