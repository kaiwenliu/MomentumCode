public class Letter extends Container {
	char character;

	public Letter(String name, char character) {
		super(name);
		this.character = character;
	}

	public char getLetter() {
		return character;
	}

	public void setLetter(char letter) {
		character = letter;
	}

	public String getDataType() {
		return "letter";
	}
}
