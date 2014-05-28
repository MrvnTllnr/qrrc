package qrrc.model;

import java.nio.file.Path;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class QRDetector {

	private boolean debug;
	
	private Mat img;
	private Mat tmp;
	private Mat qrImg;
	
	private Point pSize;

	public QRDetector(Path filePath, boolean debug) {
		this.debug = debug;
		img = Highgui.imread(filePath.toString());
		tmp = Highgui.imread("input/qrtemplate3.png");
	}
	
	public void process() {
		boolean edgeDetection = true;
		
		ArrayList<Rect> threeRects = detectCorners();
		
		qrImg = cutQR(threeRects);
		
		pSize = new Point(threeRects.get(0).width/7,threeRects.get(0).height/7);
		
		ArrayList<ArrayList<Rect> > qrRects = getQrRects();
		
		if (edgeDetection) edgeDetection();
		
		Mat qrMat = createQRMat(qrRects);
		
		QrData qrData = new QrData(qrMat);
		
		Mat qrDataMat = qrData.toQrMat(4, true);
		
	}
	
	private ArrayList<Rect> detectCorners() {
		boolean drawQrCorners = false;
		
		ArrayList<Rect> result = new ArrayList<Rect>();
		
		int halfHeigth = (int) Math.ceil((img.rows()/2));
		int halfWidth = (int) Math.ceil((img.cols()/2));
		
		int[][] multi = {{0,0},{1,0},{0,1}};
		for (int i = 0; i < 3; i++) {
			Rect rect = new Rect(halfWidth*multi[i][0],halfHeigth*multi[i][1],halfWidth,halfHeigth);
			result.add(templateMatching(img.submat(rect),tmp,new Point(halfWidth*multi[i][0],halfHeigth*multi[i][1])));
		}
		
		if (drawQrCorners) {
			Mat dbg = img.clone();
			for (Rect r : result) {
				Core.rectangle(dbg, r.tl(),r.br(), new Scalar(0, 255, 0));
			}
			Highgui.imwrite("qrCorners.bmp", dbg);
			System.out.println("Debug image 'qrCorners.bmp' drawn!");
		}
		return result;
	}
	
	private void edgeDetection() {
	    Mat dst = new Mat();
	    Mat cdst = qrImg.clone();
	    Imgproc.Canny(qrImg, dst, 50, 200);
	    
	    Mat lines = new Mat();
	    int threshold = (int) (pSize.x*11);					
	    int minLineSize = (int) pSize.x*7;
	    int lineGap = (int) pSize.x*7;
//	    Good Setting:
//	    int threshold = 10;					
//	    int minLineSize = (int) pSize.x*7;
//	    int lineGap = (int) pSize.x*7;
	    
	    Imgproc.HoughLinesP(dst, lines, 1, Math.PI/2, threshold, minLineSize, lineGap);
	    
	    for (int x = 0; x < lines.cols(); x++) {
	          double[] vec = lines.get(0, x);
	          double x1 = vec[0], 
	                 y1 = vec[1],
	                 x2 = vec[2],
	                 y2 = vec[3];
	          Point start = new Point(x1, y1);
	          Point end = new Point(x2, y2);

	          Core.line(cdst, start, end, new Scalar(255,0,0), 1);

	    }
	    
		Highgui.imwrite("edge.bmp",cdst);
		System.out.println("Image 'edge.bmp' printed");
	}
	
	private ArrayList<ArrayList<Rect> > getQrRects() {
		boolean drawQrRects = false;
		
		ArrayList<ArrayList<Rect> > result = new ArrayList<ArrayList<Rect> >();
		Mat dbg = null;
		if (drawQrRects) dbg = qrImg.clone();
		for (int row = 0; row < qrImg.rows(); row+=(int)pSize.y) {
			ArrayList<Rect> colRects = new ArrayList<Rect>();
			for (int col = 0; col < qrImg.cols(); col+=(int)pSize.x) {
				colRects.add(new Rect(col,row,(int)pSize.x,(int)pSize.y));
			}
			result.add(colRects);
		}
		
		if (drawQrRects) {
			for(ArrayList<Rect> alr : result) {
				for (Rect r : alr) {
					Core.rectangle(dbg, r.tl(), r.br(), new Scalar(0,255,0));
				}
			}
		}
		
		if (drawQrRects) {
			Highgui.imwrite("qrRects.bmp", dbg);
			System.out.println("Debug image 'qrRects.bmp' drawn!");
		}
		return result;
	}
	
	private Mat createQRMat(ArrayList<ArrayList<Rect> > rects) {
		boolean drawQrMat = false;
		
		int matWidth = (int)Math.ceil(qrImg.rows()/pSize.y);
		int matHeigth = (int)Math.ceil(qrImg.cols()/pSize.x);
		Mat result = new Mat(matWidth,matHeigth,CvType.CV_8UC1);
		int row = 0;
		for(ArrayList<Rect> rows : rects) {
			int col = 0;
			for (Rect cols : rows) {
				byte[] data = new byte[1];
				Mat subMat = qrImg.submat(cols);
				double[] value = subMat.get(subMat.rows()/2, subMat.cols()/2);
				if (value[0] > 0) {
					data[0] = 1;
				} else {
					data[0] = 0;
				}
				result.put(row, col, data);
				col++;
			}
			row++;
		}
		
		if (drawQrMat) { 
			Highgui.imwrite("qrMat.bmp", result);
			System.out.println("Image drawn: qrMat.bmp");
		}
		return result;
	}
	
	private Mat cutQR(ArrayList<Rect> rects) {
		boolean drawQrImg = false;
		
		Rect roi = new Rect(new Point(rects.get(0).x,rects.get(0).y)
						, new Point(rects.get(1).x+rects.get(1).width,rects.get(2).y+rects.get(2).height));
		
		if (drawQrImg) Highgui.imwrite("qrImg.bmp", img.submat(roi));
		return img.submat(roi);
	}
	
	private Rect templateMatching(Mat subImg, Mat tmp, Point offset) {
		int method = Imgproc.TM_CCOEFF_NORMED;
        int result_cols = subImg.cols() - tmp.cols() + 1;
        int result_rows = subImg.rows() - tmp.rows() + 1;
        
        Mat result = new Mat(result_rows, result_cols, CvType.CV_32FC1);
        
		Imgproc.matchTemplate(subImg, tmp, result, method);
		Core.normalize(result, result,0,1,Core.NORM_MINMAX,-1, new Mat());
		
        MinMaxLocResult mmlr = Core.minMaxLoc(result);
        Point matchLoc;
        if (method == Imgproc.TM_SQDIFF || method == Imgproc.TM_SQDIFF_NORMED) {
        	matchLoc = mmlr.minLoc;
        } else {
        	matchLoc = mmlr.maxLoc;
        }
        
        return new Rect(new Point(matchLoc.x + offset.x, matchLoc.y + offset.y)
        				, new Point(matchLoc.x + tmp.cols() + offset.x, matchLoc.y + tmp.rows() + offset.y));
	}
}
