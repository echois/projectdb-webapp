package nz.org.nesi.researchHub.model;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 10:50 AM
 */
public class AuthorizationIdentity {

    private AuthorizationPoint authzPoint;
    private String username;

    public AuthorizationIdentity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthorizationPoint getAuthzPoint() {
        return authzPoint;
    }

    public void setAuthzPoint(AuthorizationPoint authzPoint) {
        this.authzPoint = authzPoint;
    }
}
