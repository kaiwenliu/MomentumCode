public abstract class Container {
	private String type;

	public Container(String name) {
		type = name;
	}

	public String getName() {
		return type;
	}

	public abstract String getDataType();
}
