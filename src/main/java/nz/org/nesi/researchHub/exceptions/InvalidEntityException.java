package nz.org.nesi.researchHub.exceptions;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 6/12/13
 * Time: 9:31 AM
 */
public class InvalidEntityException extends Exception {

    private final Class entityClass;
    private final String invalidProperty;

    public InvalidEntityException(String msg, Class entityClass, String invalidProperty) {
        super(msg);
        this.entityClass = entityClass;
        this.invalidProperty = invalidProperty;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public String getInvalidProperty() {
        return invalidProperty;
    }
}
