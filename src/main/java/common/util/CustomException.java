package common.util;

public class CustomException extends Exception {

    private final String customMsg;

    public CustomException(final String s) {
        customMsg = s;

    }

    public String getCustomMsg() {
        return customMsg;
    }
}
