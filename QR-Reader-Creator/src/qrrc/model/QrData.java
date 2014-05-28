package qrrc.model;

import java.util.BitSet;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.Highgui;

public class QrData {
	
	private BitSet data;
	
	public QrData(BitSet data) {
		this.data = data;
	}
	
	public QrData(Mat qrMat) {
		this.data = new BitSet(qrMat.cols()*qrMat.rows() - (3 * 8 * 8));
		int pos = 0;
		for (int row = 0; row < qrMat.rows(); row++) {
			for (int col = 0; col < qrMat.cols(); col++) {
				double[] value=  qrMat.get(row, col);
				boolean isInData = checkPos(qrMat.rows(), qrMat.cols(), row, col, 1);
				if (isInData) {
					if (value[0] <= 0) {
						this.data.set(pos);
					}
					pos++;
				}
			}
		}
	}
	
	private boolean checkPos(int rows, int cols, int row, int col, int ps) {
		return !(	(row <= 7 * ps && col <= 7 * ps)
					|| (row >= rows-8 * ps && col <= 7 * ps)
					|| (row <= 7 * ps && col >= cols-8 * ps)
					);
	}
	
	public BitSet getData() {
		return data;
	}
	
	public Mat toQrMat(int pixelSize, boolean drawMat) {
		int size =  (int) Math.ceil(Math.sqrt((((8*8*3)+(data.size())/(32*3)))))*pixelSize;
		
		Mat result = new Mat(size,size,CvType.CV_32FC3);

		drawCorners(result, pixelSize);

		drawData(result, pixelSize);
		
		if (drawMat) {
			Highgui.imwrite("debug/toQrMat.png",result);
			System.out.println("Image drawn: toQrMat.bmp");
		}
		return result;
	}
	
	private void drawData(Mat img, int ps) {
		int pos = 0;
		for (int row = 0; row < img.rows(); row+=ps) {
			for (int col = 0; col < img.cols(); col+=ps) {
				boolean isInData = checkPos(img.rows(), img.cols(), row, col, ps);
				if (isInData) {
					Rect rect = new Rect(col,row,ps-1,ps-1);
					int[] color = new int[3];
					int colorPos = 0;
					for (int j = 0; j < 32*3; j++) {
						color[colorPos/32] <<=1;
						if (data.get(pos)) {
							color[colorPos/32]++;
						}
						colorPos++;
						pos++;
					}
					drawRect(img,rect,new Scalar(color[0],color[1],color[2]));
				}
			}
		}
	}

	private void drawCorners(Mat img, int ps) {
		//tl
		drawRect(img,new Rect(0,0,ps*8-1,ps*8-1), new Scalar(255,255,255));
		drawRect(img,new Rect(0,0,ps*7-1,ps*7-1), new Scalar(0,0,0));
		drawRect(img,new Rect(ps,ps,ps*5-1,ps*5-1), new Scalar(255,255,255));
		drawRect(img,new Rect(ps*2,ps*2,ps*3-1,ps*3-1), new Scalar(0,0,0));
		//tr
		drawRect(img,new Rect(img.cols()-ps*8,0,ps*8-1,ps*8-1), new Scalar(255,255,255));
		drawRect(img,new Rect(img.cols()-ps*7,0,ps*7-1,ps*7-1), new Scalar(0,0,0));
		drawRect(img,new Rect(img.cols()-ps*6,ps,ps*5-1,ps*5-1), new Scalar(255,255,255));
		drawRect(img,new Rect(img.cols()-ps*5,ps*2,ps*3-1,ps*3-1), new Scalar(0,0,0));
		//bl
		drawRect(img,new Rect(0,img.rows()-ps*8,ps*8-1,ps*8-1), new Scalar(255,255,255));
		drawRect(img,new Rect(0,img.rows()-ps*7,ps*7-1,ps*7-1), new Scalar(0,0,0));
		drawRect(img,new Rect(ps,img.rows()-ps*6,ps*5-1,ps*5-1), new Scalar(255,255,255));
		drawRect(img,new Rect(ps*2,img.rows()-ps*5,ps*3-1,ps*3-1), new Scalar(0,0,0));
	}
	
	private void drawRect(Mat img, Rect rect, Scalar color) {
		Core.rectangle(img, rect.tl(), rect.br(), color, -1);
	}

}
