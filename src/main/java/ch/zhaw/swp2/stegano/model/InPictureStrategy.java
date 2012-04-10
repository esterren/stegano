package ch.zhaw.swp2.stegano.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class InPictureStrategy implements SteganoStrategy {

	public InPictureStrategy() {
		// super(inBaseFile, inHiddenFile);
	}

	@Override
	public BufferedImage runHide(File inBaseFile, File inHiddenFile, byte inPollution) throws IOException {
		if (inBaseFile == null || inHiddenFile == null) {
			throw new IllegalArgumentException();
		}

		byte[] bHeader = BaseFileProtocolFactory.generateHeader(FileNameFactory.getExtension(inHiddenFile),
				getLengthHF(inHiddenFile), inPollution);

		BufferedImage baseFileImg = ImageIO.read(inBaseFile);
		byte[] bHiddenFile = getByteArrayFromHiddenFile(inHiddenFile);
		byte[] msg = concatHeaderAndHF(bHeader, bHiddenFile);

		return hideMessage(baseFileImg, msg);
	}

	private BufferedImage hideMessage(BufferedImage img, byte[] message) {

		// Text in Bytes umwandeln
		byte[] b = message;
		// Farbkanal
		Color channel = Color.RED;
		// Alle Bytes durchlaufen
		for (int i = 0, x = 0, y = 0; i < b.length; i++) {
			// Alle Bits durchlaufen
			for (int j = 7; j > -1; j--) {

				// Wert des Bits auslesen
				int bit = ((b[i] & 0xFF) >> j) & 1;
				// Farbe an der aktuellen Position auslesen
				int rgb = img.getRGB(x, y);
				// Den aktuellen Farbkanal auslesen
				int color = (rgb >> channel.getShift()) & 0xFF;

				// Farbkanal manipulieren
				if ((color & 1) != bit) {
					// Den ausgelesenen Farbkanal der Farbe auf 0 setzen
					rgb &= channel.getRGBManipulator();
					switch (bit) {
					case 1:
						color = color + 1;
						break;
					default:
						color = color - 1;
					}
					// Farbkanal zurückschreiben
					rgb |= color << channel.getShift();
					img.setRGB(x, y, rgb);
				}

				// nächsten Farbkanal setzen
				channel = channel.getNext();
				// Falls Farbkanal = RED => X-Position verändern
				if (channel.equals(Color.RED)) {
					x++;
					// Falls x größer als Breite des Bildes => Y-Positon
					// verändern
					if (x >= img.getWidth()) {
						x = 0;
						y++;
						// Falls y größer als Höhe des Bildes => Fehler
						if (y >= img.getHeight()) {
							return null;
						}
					}
				}
			}
		}
		return img;
	}

	@Override
	public File runSeek(File inModBaseFile) throws IOException {
		BufferedImage modBaseFileImg = ImageIO.read(inModBaseFile);

		byte[] header = seekMessage(modBaseFileImg, 0, 8);
		// byte[] bExtension = seekMessage(modBaseFileImg);

		int hiddenFileByteLength = BaseFileProtocolFactory.getLengthFromHeader(header);
		byte baseFilePollution = BaseFileProtocolFactory.getPollutionFromHeader(header);
		String hiddenFileExtension = BaseFileProtocolFactory.getExtensionFromHeader(header);

		byte[] hiddenFile = seekMessage(modBaseFileImg, 8, hiddenFileByteLength);
		// ByteBuffer buffHeader =
		// ByteBuffer.allocate(BaseFileProtocolFactory.HEADER_BYTE_LENGTH);
		// return bytesToString(bytes.toArray(new Byte[0]));

		String filename = "HiddenFile_" + System.currentTimeMillis() + "." + hiddenFileExtension;
		writeFileFromByteArray(filename, hiddenFile);
		return null;
	}

	private byte[] seekMessage(BufferedImage inModBaseFileImg, int startByte, int countByte)
			throws IllegalArgumentException {
		if (startByte < 0 || countByte < 1) {
			throw new IllegalArgumentException();
		}

		// ArrayList für gelesene Bytes
		ArrayList<Byte> bytes = new ArrayList<Byte>(0);
		// Alle horizontalen Pixel durchlaufen
		int pixelCounter = ((startByte * 8) / 3);
		int colorOffest = (startByte * 8) % 3;
		// System.out.println(pixelCounter + " " + colorOffest);
		for (int y = (pixelCounter / inModBaseFileImg.getWidth()), count = 7, value = 0; y < inModBaseFileImg
				.getHeight(); y++) {
			// int count = 7;
			// int value = 0;
			// Alle vertikalen Pixel durchlaufen
			for (int x = (pixelCounter % inModBaseFileImg.getWidth()); x < inModBaseFileImg.getWidth(); x++) {
				// Aktuelle Farbe auslesen
				int rgb = inModBaseFileImg.getRGB(x, y);
				if (colorOffest != 0) {
					switch (colorOffest) {
					case 2:
						value |= (((rgb >> Color.BLUE.getShift()) & 0xFF) & 1) << count--;
						colorOffest = 0;
						break;
					case 1:
						value |= (((rgb >> Color.GREEN.getShift()) & 0xFF) & 1) << count--;
						value |= (((rgb >> Color.BLUE.getShift()) & 0xFF) & 1) << count--;
						colorOffest = 0;
					default:
						break;
					}
				} else {
					// Alle Farbkanäle durchlaufen
					for (Color c : Color.values()) {
						// Aktuelles Byte befüllen
						value |= (((rgb >> c.getShift()) & 0xFF) & 1) << count--;
						// Aktuelles Byte ist voll
						if (count == -1) {
							// Byte == 0 (Nachrichtende)
							// if ((byte) value == 0) {
							// Nachricht in String umwandeln und zurückgeben
							// return bytesToString(bytes.toArray(new Byte[0]));
							// }
							// Byte-ArrayList das aktuelle Byte hinzufügen

							// TODO Hier werden die Bytes (der Versteckten
							// Datei)
							// aus der modifizierten Trägerdatei ausgelesen.
							// Die ersten drei Bytes müssen als
							// HiddenFile-Extension
							// zurückgegeben werden.
							// Anschliessend 4 Bytes mit der Länge (Anzahl
							// Bytes)
							// der Hidden-Datei (=> Abbruchkriterium),
							// dann folgt noch ein Byte mit der Verunreinigung.
							bytes.add((byte) value);
							if (bytes.size() == countByte) {
								byte[] bOut = new byte[bytes.size()];
								for (int i = 0; i < bytes.size(); i++) {
									bOut[i] = bytes.get(i);
								}
								return bOut;

							}
							// Zählvariablen zurücksetzen
							value = 0;
							count = 7;
						}
					}
				}
			}
		}
		// return bytesToString(bytes.toArray(new Byte[0]));

		return null;
	}

	// ByteArray aus dem Basisfile auslesen
	// private byte[] getByteArrayFromImage(BufferedImage inBaseFileImg) throws
	// IOException {
	//
	// byte[] imageInByte;
	//
	// // convert BufferedImage to byte array
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ImageIO.write(inBaseFileImg, "bmp", baos);
	// baos.flush();
	// imageInByte = baos.toByteArray();
	// baos.close();
	//
	// return imageInByte;
	//
	// // WritableRaster raster = inBaseFileImg.getRaster();
	// // DataBufferByte buffer = (DataBufferByte) raster.getDataBuffer();
	// //
	// // return buffer.getData();
	// }

	private byte[] getByteArrayFromHiddenFile(File inHiddenFile) throws IOException {
		FileInputStream fis = new FileInputStream(inHiddenFile);

		byte[] byteArrayHiddenFile = new byte[getLengthHF(inHiddenFile)];
		fis.read(byteArrayHiddenFile);
		fis.close();
		return byteArrayHiddenFile;
	}

	private void writeFileFromByteArray(String inFilePath, byte[] inFileContent) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(inFilePath));
		fos.write(inFileContent);
		fos.close();

	}

	// Verstecken BaseFile
	// private byte[] getModBaseFileImg() throws IOException {
	// FileInputStream fis = new FileInputStream(_hiddenFile);
	//
	// _byteHiddenFile = new byte[getLengthHF(_hiddenFile)];
	// fis.read(_byteHiddenFile);
	// fis.close();
	//
	// byte[] byteMBI = encode_text(getByteArrayFromImage(_baseFileImg),
	// concatHeaderAndMsg(_header, _byteHiddenFile),
	// 0);
	// // InputStream in = new ByteArrayInputStream(byteMBI);
	// // return ImageIO.read(in);
	// return byteMBI;
	// }

	private byte[] concatHeaderAndHF(byte[] inHeader, byte[] inHiddenFile) {
		int lenH = inHeader.length;
		int lenM = inHiddenFile.length;

		ByteBuffer buffer = ByteBuffer.allocate(lenH + lenM);
		buffer.put(inHeader);
		buffer.put(inHiddenFile);
		return buffer.array();

	}

	// private byte[] encode_text(byte[] image, byte[] addition, int offset) {
	// // check that the data + offset will fit in the image
	// if (addition.length + offset > image.length) {
	// throw new IllegalArgumentException("File not long enough!");
	// }
	// // loop through each addition byte
	// for (int i = 0; i < addition.length; ++i) {
	// // loop through the 8 bits of each byte
	// int add = addition[i];
	// for (int bit = 7; bit >= 0; --bit, ++offset) // ensure the new
	// // offset value
	// // carries on
	// // through both
	// // loops
	// {
	// // assign an integer to b, shifted by bit spaces AND 1
	// // a single bit of the current byte
	// int b = (add >>> bit) & 1;
	// // assign the bit by taking: [(previous byte value) AND 0xfe] OR
	// // bit to add
	// // changes the last bit of the byte in the image to be the bit
	// // of addition
	// image[offset] = (byte) ((image[offset] & 0xFE) | b);
	// }
	// }
	// return image;
	// }

	// Länge des HiddenFile auslesen
	private int getLengthHF(File inHiddenFile) throws IllegalArgumentException {
		long length = inHiddenFile.length();
		if (length < Integer.MIN_VALUE || length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Hidden File ist to big");
		}
		return (int) length;
	}
}
