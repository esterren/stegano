package ch.zhaw.swp2.stegano.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ch.zhaw.swp2.stegano.gui.SteganoGUI;

public class Controller {

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

	}

	protected IfcUserInterface getNewSteganoGUI() {
		return new SteganoGUI();
	}

	private void onRunStegano() {
		// TODO GetBasefile and HiddenFile and to the fancy stuff
	}
}
