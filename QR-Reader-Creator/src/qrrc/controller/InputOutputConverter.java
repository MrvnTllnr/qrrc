package qrrc.controller;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

public class InputOutputConverter {
	
	public static void byteAToFile(File file, boolean debug) {
		System.out.println("not implemented");
	}
	
	public static byte[] pathToByteA(Path filePath, boolean debug) {
		byte[] fileBytes = null;
		boolean useOld = false;
		if (useOld) {
			try {
				fileBytes = Files.readAllBytes(filePath);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fileBytes = compress(filePath.toFile());
		}
		if (debug) System.out.println("Bytes: " + fileBytes.length);
		return fileBytes;
	}
	
	public static BitSet pathToBitSet(Path filePath, boolean debug) {
		return toBitSet(
				pathToByteA(filePath, debug)
				, debug);
	}
	
	private static BitSet toBitSet(byte[] fileBytes, boolean debug) {
		BitSet result = new BitSet(fileBytes.length*8);
		if (debug) System.out.println("result size:" + result.size());
		int pos = 0;
		for (int i = 0; i < fileBytes.length; i++) {
			byte b = fileBytes[i];
			for (int j = 0; j < 8; j++) {
				if ((b & 1) == 1) {
					result.set(j+pos);
				}
				b >>= 1;
			}
			pos += 8;
		}
		return result;
	}
	
	private static  byte[] compress(File f) {
		try {
			FileReader fr = new FileReader(f);
			BufferedReader br = new BufferedReader(fr);
			
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    OutputStream out = new GZIPOutputStream(baos);
			String line = null;
			while((line = br.readLine()) != null) {
				line += "\n";
				out.write(line.getBytes("UTF-8"));
			}
			br.close();
			out.close();
		    return baos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String decompress(byte[] s) {
		try {
			InputStream inStream = new GZIPInputStream(
			        new ByteArrayInputStream(s));
		    ByteArrayOutputStream baoStream2 = new ByteArrayOutputStream();
		    byte[] buffer = new byte[8192];
		    int len;
		    while ((len = inStream.read(buffer)) > 0) {
		        baoStream2.write(buffer, 0, len);
		    }
		    return baoStream2.toString("UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static BufferedImage resize(BufferedImage image, int width, int height) {
	    BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
	    Graphics2D g2d = (Graphics2D) bi.createGraphics();
	    g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
	    g2d.drawImage(image, 0, 0, width, height, null);
	    g2d.dispose();
	    return bi;
	}
	
	public static BufferedImage matToBufferedImage(Mat mat) throws IOException {
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".jpg", mat, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		return ImageIO.read(in);
	}
	
	public static void inputByteAnalysis(byte[] fileBytes, boolean debug) {
		String fileStr = new String(fileBytes,Charset.defaultCharset());
		if (debug) {
			System.out.println("String: " + fileStr);
			System.out.println("Byte: " + fileBytes);
			System.out.print("Values: ");
			for (int i = 0; i < fileBytes.length; i++) {
				System.out.print(fileBytes[i]);
				if (i != fileBytes.length-1) {
					System.out.print(",");
				} else {
					System.out.print("\n");
				}
			}
		}
		if (debug) System.out.println("Space needed: " + fileBytes.length + " Byte");
	}
	
	public static void bitSetAnalysis(BitSet fileBits, boolean debug) {
		if (debug) System.out.println("Bits: " + fileBits.size());
		int lByte = fileBits.size()*8;
		int lBit = lByte/8;
		if (debug) System.out.println("Space needed: " + lBit + " Bit");
		int squareSize = (int) Math.ceil(lBit / 2);
		if (debug) System.out.println("Square size: " + squareSize);
	}
}
