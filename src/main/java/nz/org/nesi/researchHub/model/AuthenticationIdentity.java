package nz.org.nesi.researchHub.model;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 10:47 AM
 */
public class AuthenticationIdentity {

    private AuthenticationPoint authPoint;
    private String username;

    public AuthenticationIdentity() {
    }

    public AuthenticationPoint getAuthPoint() {
        return authPoint;
    }

    public String getUsername() {
        return username;
    }

    public void setAuthPoint(final AuthenticationPoint authPoint) {
        this.authPoint = authPoint;
    }

    public void setUsername(final String username) {
        this.username = username;
    }
}
