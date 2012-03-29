package ch.zhaw.swp2.stegano.model;

import java.io.File;

public abstract class AbstractRootStrategy implements SteganoStrategy {

	private File _baseFile;
	private File _hiddenFile;
	private byte _hiddenMsgLength[];
	private byte _baseFileExtension[];

	public AbstractRootStrategy(File inBasefile, File inHiddenFile) {

		// TODO switch statement for diverent Image extensions
		// TODO Error Handling, Exception throw
		// if (FileNameFactory.getExtension(inBasefile).equals("png") || this
		// instanceof InPictureStrategy) {
		// _baseFile = inBasefile;
		//
		// }

		// TODO define Header (extension + length) based on HiddenFile
		// TODO Implement Strategy Pattern for different HiddenFileTypes
		// TODO Error Handling, Exception throw
		// if (FileNameFactory.getExtension(inHiddenFile).equals("txt")) {
		// _hiddenFile = inHiddenFile;
		// }
	}
}
