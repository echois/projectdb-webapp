package nz.org.nesi.researchHub.exceptions;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 9/12/13
 * Time: 4:00 PM
 */
@ControllerAdvice
public class RestExceptionHandler {

    protected Log log = LogFactory.getLog(this.getClass().getName());

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DatabaseException.class)
    @ResponseBody
    public DatabaseException handleDatabaseConflict(DatabaseException ex) {

        log.debug("DatabaseException: "+ex.getLocalizedMessage());

        return ex;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(InvalidEntityException.class)
    @ResponseBody
    public InvalidEntityException handleInvalidEntityConflict(InvalidEntityException ex) {

        log.debug("InvalidEntityException: "+ex.getLocalizedMessage());

        return ex;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoSuchEntityException.class)
    @ResponseBody
    public NoSuchEntityException handleNoEntityConflict(NoSuchEntityException ex) {

        log.debug("NoSuchEntityException: "+ex.getLocalizedMessage());

        return ex;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(OutOfDateException.class)
    @ResponseBody
    public OutOfDateException handleOutOfDateConflict(OutOfDateException ex) {

        log.debug("OutOfDateException: "+ex.getLocalizedMessage());

        return ex;
    }
}
