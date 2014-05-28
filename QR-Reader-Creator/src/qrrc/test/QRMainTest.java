package qrrc.test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.BitSet;

import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;

import qrrc.controller.InputOutputConverter;
import qrrc.model.QrData;

public class QRMainTest {
	
	static{ System.loadLibrary("opencv_java249"); }
	
	public static void main(String[] args) {
		String p = "input";
		
		String[] inputText = {"10836_newgrounds_dan_pa.mp3"};
		Path pathText = Paths.get(p,inputText);
		
		BitSet data = InputOutputConverter.pathToBitSet(pathText, false);
		QrData qrData = new QrData(data);
		Mat qrDataMat = qrData.toQrMat(1, true);
		Highgui.imwrite("output/data.png",qrDataMat);
	}
}
