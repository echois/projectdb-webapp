package nz.org.nesi.researchHub.exceptions;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 6/12/13
 * Time: 9:31 AM
 */
public class NoSuchEntityException extends Exception {

    private final Class entityClass;
    private final Integer id;

    public NoSuchEntityException(String msg, Class entityClass, Integer id) {
        super(msg);
        this.entityClass = entityClass;
        this.id = id;
    }

    public Class getEntityClass() {
        return entityClass;
    }

    public Integer getId() {
        return id;
    }
}
