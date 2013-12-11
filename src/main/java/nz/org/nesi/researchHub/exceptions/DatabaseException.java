package nz.org.nesi.researchHub.exceptions;

import org.springframework.stereotype.Component;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 5/12/13
 * Time: 11:38 AM
 */
@Component
public class DatabaseException extends RuntimeException {

    private DatabaseException(){
        super();
    }

    public DatabaseException(String s, Exception e) {
        super(s,e);
    }
}
