package pm.util;

public class CustomException extends Exception {

	private String customMsg;
	
	public CustomException(String s) {
		this.customMsg = s;

	}
	
	public String getCustomMsg() {
		return this.customMsg;
	}
}
