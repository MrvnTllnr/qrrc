package qrrc.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.BitSet;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import org.opencv.core.Mat;

import qrrc.model.QrData;
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
			try {
				InputStream in = new FileInputStream(f);
				BufferedImage img = ImageIO.read(in);
				if (img.getWidth() > 250
						&& img.getHeight() > 250) {
					img = InputOutputConverter.resize(img,250,250);
				}
				win.setQRImage(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void loadFile() {
		JFileChooser fc = new JFileChooser(new File("."));
		File f = new File("");
		if (fc.showOpenDialog(win.getFrame()) == JFileChooser.APPROVE_OPTION) {
			f = fc.getSelectedFile();
			BitSet bitSet = InputOutputConverter.pathToBitSet(f.toPath(), false);
			QrData qrd = new QrData(bitSet);
			Mat qrMat = qrd.toQrMat(1, true);
			try {
				BufferedImage img = InputOutputConverter.matToBufferedImage(qrMat);
				if (img.getWidth() > 250
						&& img.getHeight() > 250) {
					img = InputOutputConverter.resize(img,250,250);
				}
				win.setQRImage(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
