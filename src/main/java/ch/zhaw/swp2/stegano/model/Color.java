package ch.zhaw.swp2.stegano.model;

/**
 * The Color enumeration is a helper Class for the InAudioStrategy.
 * 
 * @author Renato Estermann
 */
public enum Color {
	RED(16, (255 << 24) | (255 << 8) | (255 << 0)), GREEN(8, (255 << 24) | (255 << 16) | (255 << 0)), BLUE(0,
			(255 << 24) | (255 << 16) | (255 << 8));

	private int shift = 0;
	private int rgbManipulator;

	/**
	 * a Color is built up on a shift and a RGBManipulator
	 * 
	 * @param shift
	 *            the shift of the int value for each color
	 * @param rgbManipulator
	 *            the RGBManipulator of the each color
	 */
	private Color(int shift, int rgbManipulator) {
		this.shift = shift;
		this.rgbManipulator = rgbManipulator;
	}

	/**
	 * @return the next Color
	 */
	public Color getNext() {

		if (this == RED) {
			return GREEN;
		} else if (this == GREEN) {
			return BLUE;
		} else {
			return RED;
		}
	}

	/**
	 * @return the shift of the Color
	 */
	public int getShift() {
		return this.shift;
	}

	/**
	 * @return the RGBManipulator
	 */
	public int getRGBManipulator() {
		return this.rgbManipulator;
	}
}
