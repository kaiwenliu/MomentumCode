import java.util.ArrayList;

public class ConditionParser {
	private ArrayList<Number> numbers;

	public ConditionParser(ArrayList<Number> numbers) {
		this.numbers = numbers;
	}

	public boolean getCondition(String cond) {
		double d1 = 0, d2 = 0;
		String op = "";
		int opNum = 0;
		if (cond.contains(">")) {
			op = ">";
			opNum = 1;
		} else if (cond.contains("<")) {
			op = "<";
			opNum = 2;
		} else if (cond.contains(">=")) {
			op = ">=";
			opNum = 3;
		} else if (cond.contains("<=")) {
			op = "<=";
			opNum = 4;
		} else if (cond.contains("==")) {
			op = "==";
			opNum = 5;
		} else if (cond.contains("!=")) {
			op = "!=";
			opNum = 6;
		}

		String s1 = cond.substring(0, cond.indexOf(op)), s2 = cond.substring(cond.indexOf(op) + op.length());
		boolean hardCoded = true;
		for (int i = 0; i < cond.length(); i++) {
			if (!Character.isDigit(cond.charAt(i)) && cond.charAt(i) != '.')
				hardCoded = false;
		}

		if (hardCoded) {
			d1 = Double.parseDouble(s1);
			d2 = Double.parseDouble(s2);
		} else {
			for (int i = 0; i < numbers.size(); i++) {
				if (s1.contains(numbers.get(i).getName()))
					d1 = numbers.get(i).getValue();
				if (s2.contains(numbers.get(i).getName())) {
					d2 = numbers.get(i).getValue();
				}
			}
		}

		switch (opNum) {
		case 1:
			return d1 > d2;
		case 2:
			return d1 < d2;
		case 3:
			return d1 >= d2;
		case 4:
			return d1 <= d2;
		case 5:
			return d1 == d2;
		case 6:
			return d1 != d2;
		default:
			throw new IllegalArgumentException("Invalid operation");
		}
	}

}
