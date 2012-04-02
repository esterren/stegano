package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ch.zhaw.swp2.stegano.gui.SteganoGUI;
import ch.zhaw.swp2.stegano.model.InPictureStrategy;
import ch.zhaw.swp2.stegano.model.SteganoStrategy;

public class Controller {

	private SteganoStrategy _steganoStrategy;
	private ActionListener _listenerRunStegano = new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			onRunStegano();
		}
	};;;
	private IfcUserInterface _userInterface;

	public static void main(String[] args) {
		new Controller();

	}

	public Controller() {
		_userInterface = getNewSteganoGUI();
		_userInterface.setListeners(_listenerRunStegano);

	}

	protected IfcUserInterface getNewSteganoGUI() {
		return new SteganoGUI();
	}

	private void onRunStegano() {

		_steganoStrategy = new InPictureStrategy();

		BufferedImage modBaseFile;

		try {
			modBaseFile = _steganoStrategy.runHide(_userInterface.getBaseFile(), _userInterface.getHiddenFile(),
					(byte) 1);
			ImageIO.write(modBaseFile, "bmp", new File("test.bmp"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
