public class Number extends Container {
	double value;

	public Number(String name, double value) {
		super(name);
		this.value = value;
	}

	public void setValue(double x) {
		value = x;
	}

	public double getValue() {
		return value;
	}

	public void add(double x) {
		value += x;
	}

	public void subtract(double x) {
		value -= x;
	}

	public void multiplyBy(double x) {
		value *= x;
	}

	public void divideBy(double x) {
		value /= x;
	}

	public void mod(double x) {
		value %= x;
	}

	public String getDataType() {
		return "number";
	}
}
