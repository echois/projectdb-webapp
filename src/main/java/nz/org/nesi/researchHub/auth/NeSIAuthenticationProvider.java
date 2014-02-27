package nz.org.nesi.researchHub.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 1:40 PM
 */
public class NeSIAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(
            final UserDetails userDetails,
            final UsernamePasswordAuthenticationToken authentication) {

        final NeSIUserDetailsImpl udi = (NeSIUserDetailsImpl) userDetails;
        udi.setAuthentication(authentication);
        return;

    }
}
