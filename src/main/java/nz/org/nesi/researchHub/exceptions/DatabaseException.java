package nz.org.nesi.researchHub.exceptions;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 5/12/13
 * Time: 11:38 AM
 */
public class DatabaseException extends RuntimeException {
    public DatabaseException(String s, Exception e) {
        super(s,e);
    }
}
