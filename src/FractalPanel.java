import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class FractalPanel extends JPanel {

	protected BufferedImage image;
	protected int width;
	protected int height;
	protected double minRealValue = -2;
	protected double maxRealValue = 2;
	protected double minImaginaryValue = -1.6;
	protected double maxImaginaryValue = 1.6;
	protected int iterations = 100;

	public FractalPanel(int width, int height) {
		this.width = width;
		this.height = height;
	}

	// left empty so other methods can include it in this class, intended to be
	// overridden
	public void updateImage() {
	}

	//sets the iterations variable and re-renders the image
	public void setIterations(int iterations) {
		this.iterations = iterations;
		updateImage();
	}

	// sets the values of the axis shown in the panel
	public void setValues(double minX, double maxX, double minY, double maxY) {
		minRealValue = minX;
		maxRealValue = maxX;
		minImaginaryValue = minY;
		maxImaginaryValue = maxY;
		updateImage();
	}

	// draws the buffered image to the screen
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, null, null);
	}
}
