package nz.org.nesi.researchHub.model;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 10:47 AM
 */
public class AuthenticationIdentity {

    private AuthenticationPoint authPoint;
    private String username;

    public AuthenticationIdentity() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AuthenticationPoint getAuthPoint() {
        return authPoint;
    }

    public void setAuthPoint(AuthenticationPoint authPoint) {
        this.authPoint = authPoint;
    }
}
