package qrrc.controller;

import java.io.File;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFileChooser;

import qrrc.view.Window;

public class QRController implements Observer  {
	
	private Window win;

	public QRController() {
		BtnListener alb = new BtnListener();
		alb.addObserver(this);
		win = new Window(alb);
	}

	@Override
	public void update(Observable ob, Object cmd) {
		if (cmd instanceof String && ob instanceof BtnListener) {
			switch ((String)cmd) {
			case Window.STR_LOAD_FILE: loadFile(); break;
			case Window.STR_LOAD_IMAGE: loadImage(); break;
			case Window.STR_SAVE_IMAGE: saveImage(); break;
			default: break;
			}
		}
	}
	
	private void saveImage() {
		JFileChooser fc = new JFileChooser(new File("."));
		fc.showSaveDialog(win.getFrame());
	}

	private void loadImage() {
		JFileChooser fc = new JFileChooser(new File("."));
		if (fc.showOpenDialog(win.getFrame()) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
		}
	}

	private void loadFile() {
		JFileChooser fc = new JFileChooser(new File("."));
		if (fc.showOpenDialog(win.getFrame()) == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
		}
	}

}
