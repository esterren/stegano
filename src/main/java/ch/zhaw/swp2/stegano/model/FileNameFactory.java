package ch.zhaw.swp2.stegano.model;

import java.io.File;

/**
 * The FileNameFactory is used for different Filename operations.
 * 
 * @author rest
 * 
 */
public class FileNameFactory {

	/**
	 * This method returns the extension of the File inFile.
	 * 
	 * @param inFile
	 *            File with the extension in the Filename (eg: text.txt). The
	 *            extension of extension-less Files won't be determined.
	 * @return String with the File-Extension in LowerCase. If the Filename has
	 *         no extension, an empty String is returned.
	 * @throws IllegalArgumentException
	 *             When the file is NULL.
	 */
	public static String getExtension(File inFile) throws IllegalArgumentException {
		if (inFile == null) {
			throw new IllegalArgumentException();
		}
		String extension = null;
		String fileName = inFile.getName();
		int index = fileName.lastIndexOf('.');

		if (index > 0 && index < fileName.length() - 1) {
			extension = fileName.substring(index + 1).toLowerCase();
		}
		if (extension == null) {
			return "";
		}
		return extension;
	}
}
