package ch.zhaw.swp2.stegano.model;

import java.io.File;

public class InPictureStrategy implements SteganoStrategy {

	private File _baseFile;
	private File _hiddenFile;

	public InPictureStrategy() {
		// super(inBaseFile, inHiddenFile);
	}

	@Override
	public File runHide(File inBaseFile, File inHiddenFile, int inPollution) {
		if (inBaseFile == null || inHiddenFile == null) {
			throw new IllegalArgumentException();
		}
		_baseFile = inBaseFile;
		_hiddenFile = inHiddenFile;

		return null;
	}

	@Override
	public File runUnHide(File inModBaseFile) {
		// TODO

		return null;
	}

}
