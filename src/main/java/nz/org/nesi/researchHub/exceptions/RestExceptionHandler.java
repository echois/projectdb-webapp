package nz.org.nesi.researchHub.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 9/12/13 Time: 4:00 PM
 */
@ControllerAdvice
public class RestExceptionHandler {

    protected Log log = LogFactory.getLog(this.getClass().getName());

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseException.class)
    @ResponseBody
    public ErrorInfo handleDatabaseConflict(final HttpServletRequest req,
            final DatabaseException ex) {

        log.debug("DatabaseException: " + ex.getLocalizedMessage());

        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidEntityException.class)
    @ResponseBody
    public ErrorInfo handleInvalidEntityConflict(final HttpServletRequest req,
            final InvalidEntityException ex) {

        log.debug("InvalidEntityException: " + ex.getLocalizedMessage());

        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseBody
    public ErrorInfo handleNoEntityConflict(final HttpServletRequest req,
            final NoSuchEntityException ex) {

        log.debug("NoSuchEntityException: " + ex.getLocalizedMessage());

        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OutOfDateException.class)
    @ResponseBody
    public ErrorInfo handleOutOfDateConflict(final HttpServletRequest req,
            final OutOfDateException ex) {

        log.debug("OutOfDateException: " + ex.getLocalizedMessage());

        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ErrorInfo handleGenericException(final HttpServletRequest req,
            final Exception ex) {

        log.debug("Exception: " + ex.getLocalizedMessage());

        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }
}
