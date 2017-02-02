import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Main {

	public static void main(String[] args) {
		// instantiate the window(frame) that will hold all of the components
		MandelbrotFrame mbf = new MandelbrotFrame();
		mbf.setResizable(false);
		mbf.setVisible(true);
	}

}

class MandelbrotFrame extends JFrame {

	protected JTextField minRealField;
	protected JTextField maxRealField;
	protected JTextField minImaginaryField;
	protected JTextField maxImaginaryField;
	protected JTextField iterationField;
	protected JComboBox<String> fractalComboBox;
	protected JLabel uspLabel;
	protected JButton juliaSelectedButton;
	protected ComplexNumber userSelectedPoint;
	protected JComboBox<String> favouritesComboBox;
	protected HashMap<String, ComplexNumber> favouritesMap;
	protected int favouritesCount = 0;
	protected InteractiveZoomPanel mainPanel;
	protected Point startDrag, endDrag;
	protected JuliaPanel juliaPanel;

	public MandelbrotFrame() {
		super("Mandelbrot Set");
		Container pane = this.getContentPane();
		pane.setLayout(new BorderLayout());

		// create a panel with dynamic text fields to represent the values of
		// the range of pixel values to be rendered
		JPanel axisValuesPanel = new JPanel();
		axisValuesPanel.setLayout(new GridLayout(3, 3));
		minRealField = new JTextField(1);
		maxRealField = new JTextField(1);
		minImaginaryField = new JTextField(1);
		maxImaginaryField = new JTextField(1);
		minRealField.setText(Double.toString(-2));
		maxRealField.setText(Double.toString(2));
		minImaginaryField.setText(Double.toString(-1.6));
		maxImaginaryField.setText(Double.toString(1.6));
		JLabel minLabel = new JLabel("Min:", SwingConstants.CENTER);
		JLabel maxLabel = new JLabel("Max:", SwingConstants.CENTER);
		JLabel realLabel = new JLabel("Real:", SwingConstants.CENTER);
		JLabel imaginaryLabel = new JLabel("Imaginary:", SwingConstants.CENTER);
		axisValuesPanel.add(new JLabel(""));
		axisValuesPanel.add(minLabel);
		axisValuesPanel.add(maxLabel);
		axisValuesPanel.add(realLabel);
		axisValuesPanel.add(minRealField);
		axisValuesPanel.add(maxRealField);
		axisValuesPanel.add(imaginaryLabel);
		axisValuesPanel.add(minImaginaryField);
		axisValuesPanel.add(maxImaginaryField);

		// create a panel with a dynamic text field for changing the amount of
		// iterations and a button to render the panel
		JPanel iterationPanel = new JPanel();
		iterationPanel.setLayout(new FlowLayout());
		JLabel iterationLabel = new JLabel("Iterations:", SwingConstants.CENTER);
		iterationField = new JTextField(10);
		iterationField.setText("100");
		JButton renderButton = new JButton("Render");
		iterationPanel.add(iterationLabel);
		iterationPanel.add(iterationField);
		iterationPanel.add(renderButton);

		// create a panel to display the current user selected point and the
		// julia set it creates
		JPanel juliaView = new JPanel();
		juliaView.setLayout(new BorderLayout());
		uspLabel = new JLabel("User Selected Point:", SwingConstants.CENTER);
		juliaPanel = new JuliaPanel(270, 200);
		juliaSelectedButton = new JButton("Clear User Selected Point");
		juliaView.add(uspLabel, BorderLayout.NORTH);
		juliaView.add(juliaPanel, BorderLayout.CENTER);
		juliaView.add(juliaSelectedButton, BorderLayout.SOUTH);
		
		// button to de-select the current user selected point, making the 
		//julia panel live again
		juliaSelectedButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				userSelectedPoint = null;
				uspLabel.setText("User Selected Point: ");
			}
			
		});

		// create a panel to save the currently selected julia set and display
		// already saved sets
		JPanel favouritesPanel = new JPanel();
		favouritesPanel.setLayout(new FlowLayout());
		favouritesComboBox = new JComboBox<String>();
		favouritesMap = new HashMap<String, ComplexNumber>();
		JButton saveButton = new JButton("Save Julia Set");

		// attach a listener to the save button which stores the julia set in
		// the combobox
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(userSelectedPoint != null){
					favouritesCount++;
					// set the label to show the representation of the current user
					// selected point
					String currentPointString = "Julia Set " + favouritesCount+ " ( c = " + userSelectedPoint.getRoundedString()+ ")";
					// store the easy to read version of the point with the point
					// with all decimal values
					favouritesMap.put(currentPointString, userSelectedPoint);
					favouritesComboBox.addItem(currentPointString);
					favouritesComboBox.setSelectedItem(currentPointString);
				}
			}
		});

		// add a listener that displays the selected julia set from the ones
		// already stored in the combobox
		favouritesComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				userSelectedPoint = favouritesMap.get(favouritesComboBox.getSelectedItem());
				juliaPanel.setC(userSelectedPoint);
				uspLabel.setText("User Selected Point:  "+ userSelectedPoint.getRoundedString());
				juliaPanel.repaint();
			}
		});
		favouritesPanel.add(saveButton);

		// create the panel which renders out the main mandelbrot set image
		mainPanel = new InteractiveZoomPanel(630, 600);

		// attach a listener to the render button to refresh the panel with the
		// current values
		renderButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mainPanel.setIterations(Integer.parseInt(iterationField.getText()));
				mainPanel.setValues(Double.parseDouble(minRealField.getText()),Double.parseDouble(maxRealField.getText()),Double.parseDouble(minImaginaryField.getText()),Double.parseDouble(maxImaginaryField.getText()));
				mainPanel.repaint();
				juliaPanel.setIterations(Integer.parseInt(iterationField.getText()));
				juliaPanel.repaint();
			};
		});

		
		String[] panelArray = {"Mandelbrot Set","Burning Ship","Multibrot Power 4"};
		// ComboBox of the names of fractals that can be selected
		fractalComboBox = new JComboBox<String>(panelArray);
		// Item listener to determine which fractal has been selected and render
		// it
		fractalComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getItem().toString()=="Mandelbrot Set"){
					mainPanel.setFractal(0);
				}
				if(e.getItem().toString()=="Burning Ship"){
					mainPanel.setFractal(1);
				}
				if(e.getItem().toString()=="Multibrot Power 4"){
					mainPanel.setFractal(2);
				}
				repaint();
			}
		});
		
		// button to toggle the orbit trap value to change how the current
		// fractal is rendered
		JButton toggleButton = new JButton("Toggle Orbit Trap");
		toggleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mainPanel.toggleOrbitTrap();
				mainPanel.repaint();
			}
		});
		
		// add a listener for calculating the location of where the rectangle
		// should start and end
		mainPanel.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startDrag = new Point(e.getX(), e.getY());
				endDrag = startDrag;
				mainPanel.setDragPoints(startDrag, endDrag);
				repaint();
			}

			public void mouseReleased(MouseEvent e) {
				// check to see that the mouse has been dragged over an area or
				// just clicked in one place
				if (startDrag.x != e.getX() && startDrag.y != e.getY()) {
					double minX = mainPanel.XValue((int) Math.min(startDrag.x, endDrag.x));
					double minY = mainPanel.YValue((int) Math.min(startDrag.y, endDrag.y));
					double maxX = mainPanel.XValue((int) Math.max(startDrag.x, endDrag.x));
					double maxY = mainPanel.YValue((int) Math.max(startDrag.y, endDrag.y));
					minRealField.setText(Double.toString(minX));
					maxRealField.setText(Double.toString(maxX));
					minImaginaryField.setText(Double.toString(minY));
					maxImaginaryField.setText(Double.toString(maxY));
					// re-adjust the view to the rectangle just drawn
					mainPanel.setValues(minX, maxX, minY, maxY);
				} else {
					// set the user selected point to the value that has just
					// been clicked
					userSelectedPoint = new ComplexNumber(mainPanel.XValue(e.getX()), mainPanel.YValue(e.getY()));
					uspLabel.setText("User Selected Point:  "+ userSelectedPoint.getRoundedString());
					juliaPanel.setC(userSelectedPoint);
				}
				startDrag = null;
				endDrag = null;
				mainPanel.setDragPoints(startDrag, endDrag);
				repaint();
			}
		});

		// listener to draw the rectangle and change the where the end of the
		// dragging will be
		mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				endDrag = new Point(e.getX(), e.getY());
				mainPanel.setDragPoints(startDrag, endDrag);
				repaint();
			}

			// live update the julia set
			public void mouseMoved(MouseEvent e) {
				if (userSelectedPoint == null) {
					juliaPanel.setC(new ComplexNumber(mainPanel.XValue(e.getX()), mainPanel.YValue(e.getY())));
					repaint();
				}
			}
		});

		// add all panels to the main content pane
		JPanel controlPanel = new JPanel();
		controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.PAGE_AXIS));
		controlPanel.add(Box.createRigidArea(new Dimension(270, 30)));
		controlPanel.add(axisValuesPanel);
		controlPanel.add(Box.createRigidArea(new Dimension(270, 20)));
		controlPanel.add(iterationPanel);
		controlPanel.add(Box.createRigidArea(new Dimension(270, 15)));
		controlPanel.add(fractalComboBox);
		controlPanel.add(toggleButton);
		controlPanel.add(Box.createRigidArea(new Dimension(270, 15)));
		controlPanel.add(juliaView);
		controlPanel.add(Box.createRigidArea(new Dimension(270, 30)));
		controlPanel.add(favouritesComboBox);
		controlPanel.add(favouritesPanel);
		controlPanel.add(Box.createRigidArea(new Dimension(270, 30)));
		pane.add(controlPanel, BorderLayout.WEST);
		pane.add(mainPanel, BorderLayout.CENTER);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(900, 600);
		this.setVisible(true);
	}
	
}