
public class Cond extends Container {

	private boolean cond;

	public Cond(String name, boolean cond) {
		super(name);
		this.setCond(cond);
	}

	public boolean getCond() {
		return cond;
	}

	public void setCond(boolean cond) {
		this.cond = cond;
	}

	public String getDataType() {
		return "cond";
	}
}
