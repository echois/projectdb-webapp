package nz.org.nesi.researchHub.exceptions;

import com.mangofactory.swagger.annotations.ApiError;

/**
 * Project: project_management
 * <p/>
 * Written by: Nick Young Date: 10/12/13 Time: 15:53pm
 */
@ApiError(code = 500, reason = "Timestamp mismatch")
public class OutOfDateException extends Exception {
    public OutOfDateException(final String s) {
        super(s);
    }
}
