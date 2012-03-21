package ch.zhaw.swp2.stegano.controller;

import ch.zhaw.swp2.stegano.gui.SteganoGUI;

public class Controller {

	private IfcUserInterface _userInterface;

	public static void main(String[] args) {
		new Controller();

	}

	public Controller() {
		_userInterface = getNewSteganoGUI(this);
	}

	protected IfcUserInterface getNewSteganoGUI(Controller inController) {
		return new SteganoGUI(inController);
	}
}
