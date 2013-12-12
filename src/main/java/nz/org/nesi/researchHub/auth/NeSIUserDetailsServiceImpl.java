package nz.org.nesi.researchHub.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 1:39 PM
 */
public class NeSIUserDetailsServiceImpl implements UserDetailsService {


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new NeSIUserDetailsImpl(username);
    }
}
