package nz.org.nesi.researchHub.exceptions;

import org.springframework.stereotype.Component;

import com.mangofactory.swagger.annotations.ApiError;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 6/12/13 Time: 9:31 AM
 */
@Component
@ApiError(code = 500, reason = "Invalid entity")
public class InvalidEntityException extends Exception {

    private Class entityClass;
    private String invalidProperty;

    public InvalidEntityException() {
        super();
    }

    public InvalidEntityException(final String msg, final Class entityClass,
            final String invalidProperty) {
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

    public void setEntityClass(final Class entityClass) {
        this.entityClass = entityClass;
    }

    public void setInvalidProperty(final String invalidProperty) {
        this.invalidProperty = invalidProperty;
    }
}
