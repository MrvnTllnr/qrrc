package qrrc.model;

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

public class InputProcessor {
	
	private boolean debug;
	private BitSet fileBits;

	public InputProcessor(Path filePath, boolean debug) {
		this.debug = debug;
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
		System.out.println("Bytes: " + fileBytes.length);
		inputAnalysis(fileBytes);
	}
	
	public void inputAnalysis(byte[] fileBytes) {
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
		fileBits = toBitSet(fileBytes);
		if (debug) System.out.println("Bits: " + fileBits.size());
		int lByte = fileBytes.length;
		int lBit = lByte*8;
		if (debug) System.out.println("Space needed: " + lByte + " Byte " + lBit + " Bit");
		int squareSize = (int) Math.ceil(lBit / 2);
		if (debug) System.out.println("Square size: " + squareSize);
	}
	
	private BitSet toBitSet(byte[] fileBytes) {
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
	
	public BitSet getFileData() {
		return fileBits;
	}

	private String decompress(byte[] s) {
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
	
	private  byte[] compress(File f) {
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
}
