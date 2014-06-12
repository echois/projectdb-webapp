package nz.org.nesi.researchHub.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
@EnableWebMvcSecurity
public class AuthConfig extends WebSecurityConfigurerAdapter {


	@Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .csrf().disable();
  }

	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(new NeSIUserDetailsServiceImpl());

    }
}
