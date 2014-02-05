package nz.org.nesi.researchHub.exceptions;

/**
 * Project: hub
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 4/02/14
 * Time: 10:01 AM
 */
public class ErrorInfo {
    public final String url;
    public final String error;

    public ErrorInfo(String url, Exception ex) {
        this.url = url;
        this.error = ex.getLocalizedMessage();
    }
}
