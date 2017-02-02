public class ComplexNumber {

	double realPart;
	double imaginaryPart;

	ComplexNumber(double realPart, double imaginaryPart) {
		this.realPart = realPart;
		this.imaginaryPart = imaginaryPart;
	}

	public double getRealPart() {
		return realPart;
	}

	public double getImaginaryPart() {
		return imaginaryPart;
	}

	// returns a string of the complex number to five decimal places on each
	// part for readability
	public String getRoundedString() {
		if (Math.signum(imaginaryPart) > 0) {
			return (String) (String.format("%.5g%n", realPart) + "+"+ String.format("%.5g%n", imaginaryPart) + "i");
		} else {
			return (String.format("%.5g%n", realPart)+ String.format("%.5g%n", imaginaryPart) + "i");
		}
	}

	// creates a new complex number which is the square of the current complex
	// number
	public ComplexNumber square() {
		double squareReal = (realPart * realPart)- (imaginaryPart * imaginaryPart);
		double squareImaginary = realPart * imaginaryPart * 2;
		return new ComplexNumber(squareReal, squareImaginary);
	}

	// returns the square of the modulus
	public double modulusSquared() {
		return (realPart * realPart) - (imaginaryPart * imaginaryPart);
	}

	// returns the sum of this complex number and another complex number d which
	// is passed as a parameter
	public ComplexNumber addComplex(ComplexNumber d) {
		double resultReal = this.getRealPart() + d.getRealPart();
		double resultImaginary = this.getImaginaryPart() + d.getImaginaryPart();
		ComplexNumber result = new ComplexNumber(resultReal, resultImaginary);
		return result;
	}
	
}
