package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionListener;
import java.io.File;

/**
 * The IfcUserInterface is the Interface, which has to be implemented in any UI.
 * This Interface is used by the Controller Class and provides all basic methods
 * for interaction with the UI.
 * 
 * 
 * 
 * @author rest
 * 
 */
public interface IfcUserInterface {

	/**
	 * This Method provides displaying the BaseFile
	 */
	public void displayBaseFile();

	/**
	 * This Method provides displaying the modified Basefile
	 */
	public void displayModBaseFile();

	/**
	 * This Method is called by the Controller class and sets the
	 * ActionListener. On an actionPerformed, the Controller will run the
	 * Stegano-Algorithm.
	 * 
	 * @param runStegano
	 */
	public void setListeners(ActionListener runStegano);

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

}
