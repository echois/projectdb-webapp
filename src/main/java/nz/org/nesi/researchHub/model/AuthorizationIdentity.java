package nz.org.nesi.researchHub.model;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 10:50 AM
 */
public class AuthorizationIdentity {

    private AuthorizationPoint authzPoint;
    private String username;

    public AuthorizationIdentity() {
    }

    public AuthorizationPoint getAuthzPoint() {
        return authzPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthzPoint(final AuthorizationPoint authzPoint) {
        this.authzPoint = authzPoint;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
