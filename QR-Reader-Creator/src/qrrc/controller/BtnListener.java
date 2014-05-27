package qrrc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;

import qrrc.view.Window;

public class BtnListener extends Observable implements ActionListener  {
	

	@Override
	public void actionPerformed(ActionEvent event) {
		switch (event.getActionCommand()) {
		case Window.STR_LOAD_FILE: loadFile(); break;
		case Window.STR_LOAD_IMAGE: loadImage(); break;
		case Window.STR_SAVE_IMAGE: saveImage(); break;
		default: break;
		}
	}

	private void saveImage() {
		setChanged();
		notifyObservers(Window.STR_SAVE_IMAGE);
	}

	private void loadImage() {
		setChanged();
		notifyObservers(Window.STR_LOAD_IMAGE);
	}

	private void loadFile() {
		setChanged();
		notifyObservers(Window.STR_LOAD_FILE);
	}
}
