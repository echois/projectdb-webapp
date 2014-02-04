package nz.org.nesi.researchHub.exceptions;

import org.springframework.stereotype.Component;

import com.mangofactory.swagger.annotations.ApiError;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 6/12/13
 * Time: 9:31 AM
 */
@Component
@ApiError(code=2,reason="Invalid entity")
public class InvalidEntityException extends Exception {

    private Class entityClass;
    private String invalidProperty;

    public InvalidEntityException() {
        super();
    }

    public void setEntityClass(Class entityClass) {
        this.entityClass = entityClass;
    }

    public void setInvalidProperty(String invalidProperty) {
        this.invalidProperty = invalidProperty;
    }

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
