package nz.org.nesi.researchHub.exceptions;

import com.google.common.base.Throwables;

/**
 * Project: hub
 * <p/>
 * Written by: Markus Binsteiner Date: 4/02/14 Time: 10:01 AM
 */
public class ErrorInfo {

    public final String message;
    public final String[] stackTrace;
    public final String url;

    public ErrorInfo(final String url, final Throwable ex) {
        this.url = url;
        message = ex.getLocalizedMessage();
        stackTrace = Throwables.getStackTraceAsString(ex).split("\n");
    }
}
