import java.awt.Color;
import java.awt.image.BufferedImage;

public class JuliaPanel extends FractalPanel {

	protected final double xScale;
	protected final double yScale;
	protected ComplexNumber c;

	public JuliaPanel(int width, int height) {
		super(width, height);
		xScale = (maxRealValue - minRealValue) / width;
		yScale = (maxImaginaryValue - minImaginaryValue) / height;
	}

	// renders exactly the same way as the mandelbrot set is rendered except for
	// a different initial value
	public void updateImage() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// loop through all x pixel values
		for (int x = 0; x < width; x++) {
			// work out the double value of the current selection of screen for
			// the current pixel value
			double xValue = (minRealValue + x * xScale);
			// loop through the y values
			for (int y = 0; y < height; y++) {
				ComplexNumber d = new ComplexNumber(xValue,(minImaginaryValue + y * yScale));
				// initial value of d rather than c
				ComplexNumber z = d;
				int count = 0;
				int maximumIterations = iterations;
				// if the maximum iterations have been reached or the value of z
				// diverges stop iterating
				while (count < maximumIterations && z.modulusSquared() < 4) {
					ComplexNumber ztemp = c.addComplex(z.square());
					// check if the current value of z is the same as the last,
					// if so break the loop
					if (ztemp.equals(z)) {
						count = maximumIterations;
						break;
					}
					z = ztemp;
					count++;
				}
				if (count == maximumIterations) {
					image.setRGB(x, y, 0);
				} else {
					// colour the pixel dependent on the value of the amount of
					// iterations it got through
					image.setRGB(x, y, Color.HSBtoRGB((float) count/ maximumIterations, (float) 1, (float) 1));
				}
			}
		}
	}

	// set the value of c to be the user selected point and updates the image
	public void setC(ComplexNumber userSelectedPoint) {
		c = userSelectedPoint;
		updateImage();
	}
}
