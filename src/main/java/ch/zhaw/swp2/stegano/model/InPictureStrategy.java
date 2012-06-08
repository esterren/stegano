package ch.zhaw.swp2.stegano.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

/**
 * The InPictureStrategy implements the Algorithem to hide Information in a
 * Picture of the Impage Format BMP or PNG, or seek/filter information out of a
 * modified Picture of the same format.
 * 
 * @author Renato Estermann
 * 
 */
public class InPictureStrategy implements SteganoStrategy {

	private String baseFileHexString = "";
	private String modBaseFileHexString = "";
	private List<String> baseFileHexList = new LinkedList<String>();
	private List<String> modBaseFileHexList = new LinkedList<String>();

	public InPictureStrategy() {
		// super(inBaseFile, inHiddenFile);
	}

	/**
	 * This Method is called by the Controller, generates the Header
	 * Information, transforms the hidden Information in an appropriate format,
	 * concats all together and runs the private methode hideMessage with the
	 * Algorithm implementation, which hides everything in a Copy of the
	 * BaseFile.
	 * 
	 * @param inModBaseFile
	 *            the File, where the results are saved to.
	 * @param inBaseFile
	 *            The BaseFile on which the Algorithm will process the hiding.
	 * @param inHiddenFile
	 *            The File to hide.
	 * @param pollution
	 *            The nummber [1-7] of Bits in a Byte which are manipulated by
	 *            the Algorithm to hide the information. Increasing pollution
	 *            increases the amount of size for the HiddenFile
	 * 
	 */
	@Override
	public void runHide(File inModBaseFile, File inBaseFile, File inHiddenFile, byte inPollution) throws Exception {
		if (inModBaseFile == null || inBaseFile == null || inHiddenFile == null) {
			throw new Exception("Please import a Basefile and a Hiddenfile!");
		}

		baseFileHexString = "";
		modBaseFileHexString = "";
		// Generate Header Information
		byte[] bHeader = BaseFileProtocolFactory.generateHeader(FileNameFactory.getExtension(inHiddenFile),
				FileByteFactory.getFileLength(inHiddenFile), inPollution);

		BufferedImage baseFileImg = ImageIO.read(inBaseFile);
		// Byte-wise read of the Hiddenfile
		byte[] bHiddenFile = FileByteFactory.getByteArrayFromFile(inHiddenFile);
		// Concats Header and Message
		byte[] bMsg = concat2ByteArrays(bHeader, bHiddenFile);
		byte[] crc = CRCFactory.getCRC(bMsg);
		byte[] bMsgCRC = concat2ByteArrays(bMsg, crc);

		BufferedImage modBaseFile = hideMessage(baseFileImg, bMsgCRC);
		ImageIO.write(modBaseFile, FileNameFactory.getExtension(inModBaseFile), inModBaseFile);
	}

	/**
	 * 
	 * @param img
	 * @param message
	 * @return
	 * @throws Exception
	 */
	private BufferedImage hideMessage(BufferedImage img, byte[] message) throws Exception {

		// Text in Bytes umwandeln
		// byte[] b = message;
		// Farbkanal
		Color channel = Color.RED;
		// Alle Bytes durchlaufen
		for (int i = 0, x = 0, y = 0; i < message.length; i++) {
			// Alle Bits durchlaufen
			for (int j = 7; j > -1; j--) {

				// Wert des Bits auslesen
				int bit = ((message[i] & 0xFF) >> j) & 1;
				// Farbe an der aktuellen Position auslesen
				int rgb = img.getRGB(x, y);
				// Den aktuellen Farbkanal auslesen
				int color = (rgb >> channel.getShift()) & 0xFF;
				baseFileHexList.add(getFormatedHexString(color));
				// baseFileHexString += getFormatedHexString(color);
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
				modBaseFileHexList.add(getFormatedHexString(color));
				// modBaseFileHexString += getFormatedHexString(color);

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
							throw new Exception(
									"The Basefile is too small for the Hiddenfile!\nPlease import a bigger Basefile or increase Pollution.");
						}
					}
				}
			}
		}
		return img;
	}

	@Override
	public String runSeek(File inModBaseFile, String inHiddenFileSaveDir) throws Exception {
		if (inModBaseFile == null || inHiddenFileSaveDir == null) {
			throw new Exception("Please import a modified Basefile and set the directory for the Hiddenfile!");
		}
		BufferedImage modBaseFileImg = ImageIO.read(inModBaseFile);
		baseFileHexString = "";
		modBaseFileHexString = "";

		byte[] bHeader = seekMessage(modBaseFileImg, 0, BaseFileProtocolFactory.HEADER_LENGTH, (byte) 1);

		if (bHeader == null) {
			throw new Exception(
					"A problem was encountered during the Seek-Algorithm!\nThe modified Basefile might be corrupted!");
		}
		int hiddenFileByteLength = BaseFileProtocolFactory.getLengthFromHeader(bHeader);
		byte baseFilePollution = BaseFileProtocolFactory.getPollutionFromHeader(bHeader);
		String hiddenFileExtension = BaseFileProtocolFactory.getExtensionFromHeader(bHeader);

		byte[] bHiddenFile = seekMessage(modBaseFileImg, BaseFileProtocolFactory.HEADER_LENGTH, hiddenFileByteLength,
				baseFilePollution);

		byte[] bCRCMsg = seekMessage(modBaseFileImg, BaseFileProtocolFactory.HEADER_LENGTH + hiddenFileByteLength, 8,
				baseFilePollution);
		if (bHiddenFile == null || bCRCMsg == null) {
			throw new Exception(
					"A problem was encountered during the Seek-Algorithm!\nThe modified Basefile might be corrupted!");
		}
		byte[] bMsg = concat2ByteArrays(bHeader, bHiddenFile);
		byte[] bCRCCalc = CRCFactory.getCRC(bMsg);
		if (!Arrays.equals(bCRCCalc, bCRCMsg)) {
			throw new Exception("The hidden CRC doesn't match the Content. The modified Basefile was manipulated!");

		}

		String filepath = inHiddenFileSaveDir + File.separatorChar + "HiddenFile_" + System.currentTimeMillis() + "."
				+ hiddenFileExtension;
		writeFileFromByteArray(filepath, bHiddenFile);
		return filepath;
	}

	/**
	 * The seekMessage Methode seeks for the hidden Bytes in a BaseFile.
	 * 
	 * @param inModBaseFileImg
	 *            the underlying Basefile-Image with the hidden Message (bytes)
	 * @param startByte
	 *            is the Start at which the algorithm starts. It starts with 0.
	 * @param countBytes
	 *            is the number of bytes the algorithm is looking for. For e.g.:
	 *            the length of Bytes (placed in the header) can be placed here.
	 * @return a byte array with the found bytes in the BufferedImage starting
	 *         at startByte and has the length of countByte.
	 * @throws IllegalArgumentException
	 */
	private byte[] seekMessage(BufferedImage inModBaseFileImg, int startByte, int countBytes, byte inPollution)
			throws Exception {
		if (startByte < 0 || countBytes < 1) {
			throw new Exception(
					"Unable to seek for the Hiddenfile, due to corrupt data.\nThe modified Basefile was manipulated!");
		}

		boolean hasPixLineOffset = false;
		// ArrayList für gelesene Bytes
		ArrayList<Byte> bytes = new ArrayList<Byte>(0);
		// Alle horizontalen Pixel durchlaufen
		int pixelCounter = ((startByte * 8) / 3);

		int colorOffest = (startByte * 8) % 3;
		if (pixelCounter % inModBaseFileImg.getWidth() != 0) {
			hasPixLineOffset = true;
		}

		for (int y = (pixelCounter / inModBaseFileImg.getWidth()), count = 7, value = 0; y < inModBaseFileImg
				.getHeight(); y++) {
			// Alle vertikalen Pixel durchlaufen
			// TODO Problem liegt hier bei der modulo Operation, da 21 % 10 = 1
			// und es sollte 0 sein
			// (pixelCounter % ..getWith() darf nur in der ersten Pixelreihe mit
			// versetztem Anfang berechntet werden, ansonsten muss x = 0 sein!!
			int x = 0;
			if (hasPixLineOffset) {
				x = (pixelCounter % inModBaseFileImg.getWidth());
				hasPixLineOffset = false;
			}
			while (x < inModBaseFileImg.getWidth()) {

				int rgb = inModBaseFileImg.getRGB(x, y);

				// Aktuelle Farbe auslesen
				if (colorOffest != 0) {
					switch (colorOffest) {
					case 2:
						value |= (((rgb >> Color.BLUE.getShift()) & 0xFF) & 1) << count--;
						colorOffest = 0;
						modBaseFileHexList.add(getFormatedHexString(value));
						// modBaseFileHexString += getFormatedHexString(value);
						break;
					case 1:
						value |= (((rgb >> Color.GREEN.getShift()) & 0xFF) & 1) << count--;
						value |= (((rgb >> Color.BLUE.getShift()) & 0xFF) & 1) << count--;
						colorOffest = 0;
						modBaseFileHexList.add(getFormatedHexString(value));
						// modBaseFileHexString += getFormatedHexString(value);
					default:
						break;
					}
				} else {
					// Alle Farbkanäle durchlaufen
					for (Color c : Color.values()) {
						// Aktuelles Byte befüllen
						value |= (((rgb >> c.getShift()) & 0xFF) & 1) << count--;
						modBaseFileHexList.add(getFormatedHexString(value));
						// modBaseFileHexString += getFormatedHexString(value);
						// Aktuelles Byte ist voll
						if (count == -1) {

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
							if (bytes.size() == countBytes) {
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
				x++;
			}

		}

		return null;
	}

	// TODO is obsolet, moved to ByteArrayFactory
	private byte[] getByteArrayFromHiddenFile(File inHiddenFile) throws IOException {
		FileInputStream fis = new FileInputStream(inHiddenFile);

		byte[] byteArrayHiddenFile = new byte[FileByteFactory.getFileLength(inHiddenFile)];
		fis.read(byteArrayHiddenFile);
		fis.close();
		return byteArrayHiddenFile;
	}

	/**
	 * @param inFilePath
	 * @param inFileContent
	 * @throws IOException
	 */
	private void writeFileFromByteArray(String inFilePath, byte[] inFileContent) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(inFilePath));
		fos.write(inFileContent);
		fos.close();

	}

	/**
	 * @param inFirstBA
	 * @param inSecondBA
	 * @return
	 */
	private byte[] concat2ByteArrays(byte[] inFirstBA, byte[] inSecondBA) {
		int lenH = inFirstBA.length;
		int lenM = inSecondBA.length;

		ByteBuffer buffer = ByteBuffer.allocate(lenH + lenM);
		buffer.put(inFirstBA);
		buffer.put(inSecondBA);
		return buffer.array();

	}

	// TODO is obsolet, moved to ByteArrayFactory
	// Länge des HiddenFile auslesen
	private int getLengthHF(File inHiddenFile) throws IllegalArgumentException {
		long length = inHiddenFile.length();
		if (length < Integer.MIN_VALUE || length > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Hidden File ist to big");
		}
		return (int) length;
	}


	public String getFormatedHexString(int inValue) {
		return String.format(HEX_STRING_FORMAT, inValue);
	}

	public List<String> getFormatedBaseFileHexString() {
		return baseFileHexList;
	}

	public List<String> getFormatedModBaseFileHexString() {
		return modBaseFileHexList;
	}
}
