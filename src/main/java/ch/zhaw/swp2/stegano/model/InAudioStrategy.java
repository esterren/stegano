package ch.zhaw.swp2.stegano.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class InAudioStrategy implements SteganoStrategy {

	private List<String> baseFileHexList = new LinkedList<String>();
	private List<String> modBaseFileHexList = new LinkedList<String>();
	
	public InAudioStrategy() {
		// super(inBaseFile, inHiddenFile);
	}

	@Override
	public void runHide(File inModBaseFile, File inBaseFile, File inHiddenFile, byte inPollution) throws Exception {
		if (inModBaseFile == null || inBaseFile == null || inHiddenFile == null) {
			throw new Exception("Please import a Basefile and a Hiddenfile!");
		}

		WavFile wBaseFile = WavFile.openWavFile(inBaseFile);

		byte[] bHeader = BaseFileProtocolFactory.generateHeader(FileNameFactory.getExtension(inHiddenFile),
				FileByteFactory.getFileLength(inHiddenFile), inPollution);

		// byte[] baseFileAudio = getBytesFromFile(inBaseFile);

		byte[] bHiddenFile = getBytesFromFile(inHiddenFile);
		byte[] bMsg = concat2ByteArrays(bHeader, bHiddenFile);
		byte[] crc = CRCFactory.getCRC(bMsg);
		byte[] bMsgCRC = concat2ByteArrays(bMsg, crc);

		long[] audioLongArr = hideMessage(wBaseFile, bMsgCRC);	
		
		WavFile wInModBaseFile = WavFile.newWavFile(inModBaseFile, wBaseFile.getNumChannels(), wBaseFile.getNumFrames(), wBaseFile.getValidBits(), wBaseFile.getSampleRate());
		wInModBaseFile.writeFrames(audioLongArr, wBaseFile.getNumChannels() * (int) wBaseFile.getNumFrames());
		wInModBaseFile.close();
	}

	private long[] hideMessage(WavFile audio, byte[] message) throws Exception {

		int sampleTotal = (int) audio.getNumFrames();
		int channelTotal = (int) audio.getNumChannels();
		long[] buffer = new long[sampleTotal * channelTotal];
		audio.readFrames(buffer, sampleTotal);

		for (int i = 0; i < message.length; i++) {
			byte m = message[i];
			// boolean[] mBit = new boolean[8];

			for (int j = 0; j < 8; j++) {
				baseFileHexList.add(Long.toHexString(buffer[i*8+j]));
				System.out.println(buffer[i*8+j]);
				if ((m % 2) == 1 && buffer[i * 8 + j] % 2 == 0) {
					buffer[i * 8 + j] = buffer[i * 8 + j] - 1;
					m = (byte) ((m - 1) / 2);
				} else if ((m % 2) == 1 && buffer[i * 8 + j] % 2 == 1) {
					m = (byte) ((m - 1) / 2);
				} else if ((m % 2) == 0 && buffer[i * 8 + j] % 2 == 0) {
					m = (byte) ((m) / 2);
				} else {
					buffer[i * 8 + j] = buffer[i * 8 + j] - 1;
				}
				modBaseFileHexList.add(Long.toHexString(buffer[i*8+j]));
				System.out.println(buffer[i*8+j]);
			}

		}

		return buffer;
	}

	@Override
	public String runSeek(File inModBaseFile, String inHiddenFileSaveDir) throws Exception {
		if (inModBaseFile == null || inHiddenFileSaveDir == null) {
			throw new Exception("Please import a modified Basefile and set the directory for the Hiddenfile!");
		}
		WavFile modBaseFileAudio = WavFile.openWavFile(inModBaseFile);

		long[] audioLongArray = null;
		byte[] bModBaseFileAudio = null;
		
		modBaseFileAudio.readFrames(audioLongArray, (int)64);
		
		//modBaseFileAudio.readFrames(audioLongArray, (int)modBaseFileAudio.getNumFrames());
		
		for (int i = 0; i < audioLongArray.length; i++) {
			byte m = 0;
			
			for (int j = 0; j < 8; j++) {
				if ((audioLongArray[i * 8 + j] % 2) == 1) {
					m= (byte) ((m *2) + 1);
				} else if ((m % 2) == 0) {
					m= (byte) ((m *2));
				}
			}
			bModBaseFileAudio[i] = m;
		}
	
				
		byte[] bHeader = seekMessage(bModBaseFileAudio, 0, BaseFileProtocolFactory.HEADER_LENGTH, (byte) 0);

		if (bHeader == null) {
			throw new Exception(
					"A problem was encountered during the Seek-Algorithm!\nThe modified Basefile might be corrupted!");
		}
		
		int hiddenFileByteLength = BaseFileProtocolFactory.getLengthFromHeader(bHeader);
		byte baseFilePollution = BaseFileProtocolFactory.getPollutionFromHeader(bHeader);
		String hiddenFileExtension = BaseFileProtocolFactory.getExtensionFromHeader(bHeader);
		
		//
		modBaseFileAudio.readFrames(audioLongArray, (int)hiddenFileByteLength*8);
		
		for (int i = 0; i < audioLongArray.length; i++) {
			byte m = 0;
			
			for (int j = 0; j < 8; j++) {
				if ((audioLongArray[i * 8 + j] % 2) == 1) {
					m= (byte) ((m *2) + 1);
				} else if ((m % 2) == 0) {
					m= (byte) ((m *2));
				}
			}
			bModBaseFileAudio[i] = m;
		}
		
		
		

		byte[] bHiddenFile = seekMessage(bModBaseFileAudio, BaseFileProtocolFactory.HEADER_LENGTH, hiddenFileByteLength,
				baseFilePollution);

		byte[] bCRCMsg = seekMessage(bModBaseFileAudio, BaseFileProtocolFactory.HEADER_LENGTH + hiddenFileByteLength, 8,
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
	private byte[] seekMessage(byte[] bModBaseFileAudio, int startByte, int countBytes, byte inPollution)
			throws Exception {
		if (startByte < 0 || countBytes < 1) {
			throw new Exception(
					"Unable to seek for the Hiddenfile, due to corrupt data.\nThe modified Basefile was manipulated!");
		}

		byte[] seekData = null;
		
		for (int i = 0; i < countBytes; i++) {
			seekData[i] = bModBaseFileAudio[startByte];
			startByte++;
		}
		
		return seekData;
	}

	private byte[] longToByteArray(long longArr) {
		return new byte[] { (byte) ((longArr >> 56) & 0xff), (byte) ((longArr >> 48) & 0xff),
				(byte) ((longArr >> 40) & 0xff), (byte) ((longArr >> 32) & 0xff), (byte) ((longArr >> 24) & 0xff),
				(byte) ((longArr >> 16) & 0xff), (byte) ((longArr >> 8) & 0xff), (byte) ((longArr >> 0) & 0xff), };
	}

	private long byteArrayToLong(byte[] byteArr) {
		if (byteArr == null || byteArr.length != 8)
			return 0x0;

		return (long) ((long) (0xff & byteArr[0]) << 56 | (long) (0xff & byteArr[1]) << 48
				| (long) (0xff & byteArr[2]) << 40 | (long) (0xff & byteArr[3]) << 32
				| (long) (0xff & byteArr[4]) << 24 | (long) (0xff & byteArr[5]) << 16 | (long) (0xff & byteArr[6]) << 8 | (long) (0xff & byteArr[7]) << 0);
	}

	// TODO is obsolet, moved to ByteArrayFactory
	private byte[] getByteArrayFromHiddenFile(File inHiddenFile) throws IOException {
		FileInputStream fis = new FileInputStream(inHiddenFile);

		byte[] byteArrayHiddenFile = new byte[FileByteFactory.getFileLength(inHiddenFile)];
		fis.read(byteArrayHiddenFile);
		fis.close();
		return byteArrayHiddenFile;
	}

	private void writeFileFromByteArray(String inFilePath, byte[] inFileContent) throws IOException {
		FileOutputStream fos = new FileOutputStream(new File(inFilePath));
		fos.write(inFileContent);
		fos.close();

	}

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

	public byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		// Laenge des Files
		long length = file.length();

		// Pruefen ob File ueberhaupt verarbeitet werden kann
		if (length > Integer.MAX_VALUE) {
			// File is too large
		}

		// Bytearray fuer Rueckgabe
		byte[] bytes = new byte[(int) length];

		// Bytearray fuellen
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// sicherstellen dass alle Bytes gelesen wurden
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}

		// schliessen Inputstream
		is.close();
		return bytes;
	}

	@Override
	public List<String> getFormatedBaseFileHexString() {
		return baseFileHexList;
	}

	@Override
	public List<String> getFormatedModBaseFileHexString() {
		return modBaseFileHexList;
	}
}