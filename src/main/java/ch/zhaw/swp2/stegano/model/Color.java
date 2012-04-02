package ch.zhaw.swp2.stegano.model;

public enum Color {
	RED(16, (255 << 24) | (255 << 8) | (255 << 0)), GREEN(8, (255 << 24) | (255 << 16) | (255 << 0)), BLUE(0,
			(255 << 24) | (255 << 16) | (255 << 8));

	private int shift = 0;
	private int rgbManipulator;

	private Color(int shift, int rgbManipulator) {
		this.shift = shift;
		this.rgbManipulator = rgbManipulator;
	}

	public Color getNext() {

		if (this == RED) {
			return GREEN;
		} else if (this == GREEN) {
			return BLUE;
		} else {
			return RED;
		}
	}

	public int getShift() {
		return this.shift;
	}

	public int getRGBManipulator() {
		return this.rgbManipulator;
	}
}
