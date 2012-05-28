package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.swp2.stegano.gui.SteganoGUI;
import ch.zhaw.swp2.stegano.model.InPictureStrategy;
import ch.zhaw.swp2.stegano.model.SteganoStrategy;

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

	public static void main(String[] args) {
		new Controller();

	}

	public Controller() {
		_userInterface = getNewSteganoGUI();
		_userInterface.setListeners(_listenerHideStegano, _listenerSeekStegano);

	}

	protected IfcUserInterface getNewSteganoGUI() {
		return new SteganoGUI();
	}

	private void onRunHideStegano() {

		_steganoStrategy = new InPictureStrategy();

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

	private void onRunSeekStegano() {
		_steganoStrategy = new InPictureStrategy();

		try {
			// TODO The Save-Path from the GUI has to be passed to the model to
			// save the hidden File and the abolute Filepath should be the
			// return for the GUI.
			String hiddenFilePath = _steganoStrategy.runSeek(_userInterface.getModifiedBaseFile(),
					_userInterface.getHiddenFileSaveDir());
			_userInterface.displaySeekedHiddenFile(hiddenFilePath);
		} catch (Exception e) {
			e.printStackTrace();
			_userInterface.displayErrorMsg(e.getMessage());
		}
	}
}
