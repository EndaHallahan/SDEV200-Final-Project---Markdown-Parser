package application;

public class TextToken extends Token {
	private String value = "";
	
	public String getValue() {
		return this.value;
	}
	
	public void setValue(String val) {
		this.value = val;
	}
	
	@Override
	public String toString() {
		return "[" + this.value + "]";
	}
}
