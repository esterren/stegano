package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.swp2.stegano.gui.SteganoGUI;
import ch.zhaw.swp2.stegano.model.FileNameFactory;
import ch.zhaw.swp2.stegano.model.InAudioStrategy;
import ch.zhaw.swp2.stegano.model.InPictureStrategy;
import ch.zhaw.swp2.stegano.model.SteganoStrategy;

/**
 * The Controller Class instantiates the GUI and selects the appropriate
 * Stegano-Strategy (Algorithm) for Hiding and Seeking.
 * 
 * @author Renato Estermann
 * 
 */
public class Controller {

	private SteganoStrategy _steganoStrategy;
	private ActionListener _listenerHideStegano = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			onRunHideStegano();
		}
	};;;
	private ActionListener _listenerSeekStegano = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			onRunSeekStegano();

		}
	};
	private IfcUserInterface _userInterface;

	/**
	 * Strarts the Stegano Application.
	 * 
	 * @param args
	 *            no Arguments are needed or processed.
	 */
	public static void main(String[] args) {
		new Controller();

	}

	/**
	 * The default constructor instantiates a new User Interface and sets the
	 * two Listeners in it.
	 */
	public Controller() {
		_userInterface = getNewSteganoGUI();
		_userInterface.setListeners(_listenerHideStegano, _listenerSeekStegano);

	}

	/**
	 * Instantiates a new SteganoGUI.
	 * 
	 * @return the SteganoGUI
	 */
	protected IfcUserInterface getNewSteganoGUI() {
		return new SteganoGUI();
	}

	/**
	 * This method is called by the ActionListener and runs the Hide Part of the
	 * Stegano-Algorithm.
	 */
	private void onRunHideStegano() {
		// According to the filename-extension of the BaseFile the Strategy is
		// selected.
		if (FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("bmp")
				|| FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("png")) {
			_steganoStrategy = new InPictureStrategy();

		} else if (FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("wav")) {
			_steganoStrategy = new InAudioStrategy();
		} else {
			_steganoStrategy = null;
			_userInterface.displayInfoMsg("Supported Basefile Types are: BMP, PNG and WAV!");
			return;
		}

		// Runs the Hide algorithm of the selected Strategy and displays the
		// result in the gui
		try {
			_steganoStrategy.runHide(_userInterface.getModifiedBaseFile(), _userInterface.getBaseFile(),
					_userInterface.getHiddenFile(), (byte) 1);
			_userInterface.setBaseFileHexString(_steganoStrategy.getFormatedBaseFileHexString());
			_userInterface.setModBaseFileHexString(_steganoStrategy.getFormatedModBaseFileHexString());
			_userInterface.displayModBaseFile();
		} catch (Exception e) {
			e.printStackTrace();
			_userInterface.displayErrorMsg(e.getMessage());
		}

	}

	/**
	 * This method is called by the ActionListener and runs the Seek Part of the
	 * Stegano-Algorithm.
	 */
	private void onRunSeekStegano() {
		// According to the filename-extension of the BaseFile the Strategy is
		// selected.
		if (FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("bmp")
				|| FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("png")) {
			_steganoStrategy = new InPictureStrategy();

		} else if (FileNameFactory.getExtension(_userInterface.getBaseFile()).toLowerCase().equals("wav")) {
			_steganoStrategy = new InAudioStrategy();
		} else {
			_steganoStrategy = null;
			_userInterface.displayInfoMsg("Supported modified Basefile Types are: BMP, PNG and WAV!");
			return;
		}
		// Runs the Seek algorithm of the selected Strategy and displays the
		// result in the gui.
		try {
			String hiddenFilePath = _steganoStrategy.runSeek(_userInterface.getModifiedBaseFile(),
					_userInterface.getHiddenFileSaveDir());
			_userInterface.displaySeekedHiddenFile(hiddenFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			_userInterface.displayErrorMsg(e.getMessage());
		}
	}
}
