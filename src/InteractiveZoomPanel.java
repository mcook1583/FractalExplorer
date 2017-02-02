import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class InteractiveZoomPanel extends FractalPanel {

	Rectangle2D rectangle;
	Point startDrag = null;
	Point endDrag = null;
	int fractal = 0;
	boolean orbitTrapped = false;
    double distance = 1e20;

	public InteractiveZoomPanel(int width, int height) {
		super(width, height);
		updateImage();
	}

	// left empty so other methods can include it in this class, intended to be
	// overridden
	public void updateImage() {
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// loop through all x pixel values
		for (int x = 0; x < width; x++) {
			// work out the double value of the current selection of screen for
			// the current pixel value
			double xValue = XValue(x);
			// loop through the y values
			for (int y = 0; y < height; y++) {
				// create the first complex number object to start the iterative
				// process
				ComplexNumber c = new ComplexNumber(xValue, YValue(y));
				ComplexNumber z = c;
				// start the count of how many iterations have already been run
				int count = 0;
				int maxIterations = iterations;
				// if the maximum iterations have been reached or the value of z
				// diverges stop iterating
				while (count < maxIterations && z.modulusSquared() < 4) {
					ComplexNumber ztemp = calculateNextIteration(z,c);
					// check if the current value of z is the same as the last,
					// if so break the loop
					if (ztemp.equals(z)) {
						count = maxIterations;
						break;
					}
					z = ztemp;
					count++;
					if(orbitTrapped){
						// calculate how far from the selected distance the current iteration is
						double location = z.addComplex(new ComplexNumber(-xValue, -YValue(y))).modulusSquared();
						if(location < distance){
							image.setRGB(x,	y,Color.HSBtoRGB((float) Math.sqrt(location), (float) 1, (float) 1));
						}
				    //standard fractal calculations for colour
					}else{
						if (count == maxIterations) {
							image.setRGB(x, y, 0);
						} else {
							// colour the pixel dependent on the value of the amount of
							// iterations it got through
							image.setRGB(x,	y,Color.HSBtoRGB((float) count/ maxIterations, (float) 1, (float) 1));
						}
				}
				
				}
				
			}
		}
	}

	protected ComplexNumber calculateNextIteration(ComplexNumber z, ComplexNumber c) {
		ComplexNumber ztemp = null;
		if(fractal==0){
			// iterative formula is Z(i+1) = (Z(i) * Z(i)) + c
			ztemp = c.addComplex(z.square());
		}
		if(fractal==1){
			// same code as for the Mandelbrot set but changed iterative
			// formula to z(i+1) = ( |Re Z(i)| + i |Im Z(i)| )^2 + c
			ztemp = new ComplexNumber(Math.abs(z.getRealPart()), Math.abs(z.getImaginaryPart()));
			ztemp = c.addComplex(ztemp.square());
		}
		if(fractal==2){
			// iterative formula is Z(i+1) = (Z(i) * Z(i))^2 + c
			ztemp = c.addComplex(z.square().square());
		}
		return ztemp;
	}
	
	// pass in the integer value for the selected fractal so the image can
	// update to said fractal
	public void setFractal(int fractal) {
		this.fractal = fractal;
		updateImage();
	}

	// toggle the boolean value of whether or not what is currently being
	// displayed is an orbit trap or not
	public void toggleOrbitTrap() {
		orbitTrapped = !orbitTrapped;
		updateImage();
	}
	
	// calculate the values on the x (real) axis of the section currently being
	// displayed
	protected double XValue(int x) {
		return (minRealValue + x * (maxRealValue - minRealValue) / width);
	}

	// calculate the values on the y (imaginary) axis of the section currently
	// being displayed
	protected double YValue(int y) {
		return (minImaginaryValue + y * (maxImaginaryValue - minImaginaryValue)/ height);
	}

	public void setDragPoints(Point startDrag, Point endDrag) {
		this.startDrag = startDrag;
		this.endDrag = endDrag;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, null, null);
		if (startDrag != null && endDrag != null) {
			g2d.setColor(Color.GRAY);
			Shape r = createRectangle(startDrag.x, startDrag.y, endDrag.x,endDrag.y);
			g2d.draw(r);
		}
	}

	//create a rectangle on the panel
	protected Rectangle2D.Float createRectangle(int startX, int startY, int endX, int endY) {
		return new Rectangle2D.Float(Math.min(startX, endX), Math.min(startY, endY),Math.abs(startX - endX), Math.abs(startY - endY));
	}
	
	
	
}