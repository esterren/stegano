package ch.zhaw.swp2.stegano.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

public class InPictureStrategy implements SteganoStrategy {

	private File _baseFile;
	private BufferedImage _baseFileImg;
	private File _hiddenFile;
	private File _modBaseFile;
	private BufferedImage _modBaseFileImg;
	private byte _pollution;
	private byte[] _byteHiddenFile;
	private byte[] _header;

	public InPictureStrategy() {
		// super(inBaseFile, inHiddenFile);
	}

	@Override
	public BufferedImage runHide(File inBaseFile, File inHiddenFile, byte inPollution) throws IOException {
		if (inBaseFile == null || inHiddenFile == null) {
			throw new IllegalArgumentException();
		}
		_pollution = inPollution;
		_baseFile = inBaseFile;
		_hiddenFile = inHiddenFile;

		_header = BaseFileProtocolFactory.generateHeader(FileNameFactory.getExtension(_hiddenFile),
				getLengthHF(_hiddenFile), _pollution);
		_baseFileImg = ImageIO.read(_baseFile);
		_modBaseFileImg = new BufferedImage(_baseFileImg.getWidth(), _baseFileImg.getHeight(),
				BufferedImage.TYPE_3BYTE_BGR);
		_modBaseFileImg = getModBaseFileImg();

		return _modBaseFileImg;
	}

	@Override
	public File runUnHide(File inModBaseFile) {
		// TODO

		return null;
	}

	// ByteArray aus dem Basisfile auslesen
	private byte[] getByteArrayFromImage(BufferedImage inBaseFileImg) {

		WritableRaster raster = inBaseFileImg.getRaster();
		DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();

		return buffer.getData();
	}

	// Verstecken BaseFile
	private BufferedImage getModBaseFileImg() throws IOException {
		FileInputStream fis = new FileInputStream(_hiddenFile);

		_byteHiddenFile = new byte[getLengthHF(_hiddenFile)];
		fis.read(_byteHiddenFile);
		fis.close();

		byte[] byteMBI = encode_text(getByteArrayFromImage(_baseFileImg), concatHeaderAndMsg(_header, _byteHiddenFile),
				0);
		InputStream in = new ByteArrayInputStream(byteMBI);
		return ImageIO.read(in);
	}

	private byte[] concatHeaderAndMsg(byte[] header, byte[] msg) {
		int lenH = header.length;
		int lenM = msg.length;

		ByteBuffer buffer = ByteBuffer.allocate(lenH + lenM);
		buffer.put(_header);
		buffer.put(_byteHiddenFile);
		return buffer.array();

	}

	private byte[] encode_text(byte[] image, byte[] addition, int offset) {
		// check that the data + offset will fit in the image
		if (addition.length + offset > image.length) {
			throw new IllegalArgumentException("File not long enough!");
		}
		// loop through each addition byte
		for (int i = 0; i < addition.length; ++i) {
			// loop through the 8 bits of each byte
			int add = addition[i];
			for (int bit = 7; bit >= 0; --bit, ++offset) // ensure the new
															// offset value
															// carries on
															// through both
															// loops
			{
				// assign an integer to b, shifted by bit spaces AND 1
				// a single bit of the current byte
				int b = (add >>> bit) & 1;
				// assign the bit by taking: [(previous byte value) AND 0xfe] OR
				// bit to add
				// changes the last bit of the byte in the image to be the bit
				// of addition
				image[offset] = (byte) ((image[offset] & 0xFE) | b);
			}
		}
		return image;
	}

	// Länge des HiddenFile auslesen
	private int getLengthHF(File inHiddenFile) throws IllegalArgumentException {
		long length = inHiddenFile.length();
		if (length < Integer.MIN_VALUE || length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Hidden File ist to big");
		}
		return (int) length;
	}
}
