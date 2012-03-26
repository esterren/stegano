package ch.zhaw.swp2.stegano.model;

import java.io.File;

public class InPictureStrategy extends AbstractRootStrategy implements SteganoStrategy {

	public InPictureStrategy(File inBaseFile, File inHiddenFile) {
		super(inBaseFile, inHiddenFile);
	}

}
