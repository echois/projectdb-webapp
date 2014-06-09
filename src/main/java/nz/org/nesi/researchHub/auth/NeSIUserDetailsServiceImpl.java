package nz.org.nesi.researchHub.auth;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 1:39 PM
 */
public class NeSIUserDetailsServiceImpl implements UserDetailsService {


	private final Map<String, String> users = Maps.newHashMap();

    public NeSIUserDetailsServiceImpl() {
		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("/home/markus/users.properties");
			prop.load(input);

			for (final String name: prop.stringPropertyNames())
				users.put(name, prop.getProperty(name));

		} catch (Exception e) {
			throw new RuntimeException(e);
		}

    }




    @Override
    public UserDetails loadUserByUsername(final String username)
            throws UsernameNotFoundException {

		String password = users.get(username);
		if ( password == null ) {
			throw new UsernameNotFoundException("Can't find username: "+username);
		}

        NeSIUserDetailsImpl u = new NeSIUserDetailsImpl(username);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
		u.setAuthentication(token);
		return u;
    }
}
