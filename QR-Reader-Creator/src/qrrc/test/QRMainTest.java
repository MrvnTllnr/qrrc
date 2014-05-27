package qrrc.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import qrrc.model.InputProcessor;
import qrrc.model.QRDetector;
import qrrc.model.QrData;

public class QRMainTest {
	
	static{ System.loadLibrary("opencv_java249"); }
	
	public static void main(String[] args) {
		String p = "input";
		
		String[] inputText = {"10836_newgrounds_dan_pa.mp3"};
		Path pathText = Paths.get(p,inputText);
		InputProcessor ip = new InputProcessor(pathText, false);
		
//		String[] inputPic = {"qrcode3.png"};
//		Path pathPic = Paths.get(p,inputPic);
//		QRDetector qrd = new QRDetector(pathPic, false);
//		qrd.process();
		
		BitSet data = ip.getFileData();
		QrData qrData = new QrData(data);
		Mat qrDataMat = qrData.toQrMat(1);
		Highgui.imwrite("output/data.bmp",qrDataMat);
	}
}
