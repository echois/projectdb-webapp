package nz.org.nesi.researchHub.exceptions;

import com.google.common.base.Throwables;

/**
 * Project: hub
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 4/02/14
 * Time: 10:01 AM
 */
public class ErrorInfo {

    public final String url;
    public final String message;
    public final String[] stackTrace;

    public ErrorInfo(String url, Throwable ex) {
        this.url = url;
        this.message = ex.getLocalizedMessage();
        this.stackTrace = Throwables.getStackTraceAsString(ex).split("\n");
    }
}
