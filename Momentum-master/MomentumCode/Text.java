public class Text extends Container {
	String text;

	public Text(String name, String text) {
		super(name);
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void addText(String text) {
		this.text += text;
	}

	public void cut(int firstIndex, int secondIndex) {
		text = text.substring(firstIndex - 1, secondIndex);
	}

	public void remove(String word) {
		text = text.replace(word, "");
	}

	public void replace(String delete, String replace) {
		text = text.replace(delete, replace);

	}

	public String getDataType() {
		return "text";
	}
}
