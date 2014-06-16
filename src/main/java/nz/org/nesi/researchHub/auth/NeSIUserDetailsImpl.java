package nz.org.nesi.researchHub.auth;

import java.util.Collection;
import java.util.List;

import nz.org.nesi.researchHub.model.Group;
import nz.org.nesi.researchHub.model.GroupMembership;
import nz.org.nesi.researchHub.model.Person;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.google.common.collect.Lists;

/**
 * Project: project_management
 *
 * Written by: Markus Binsteiner Date: 11/12/13 Time: 1:47 PM
 */
public class NeSIUserDetailsImpl implements UserDetails {

    private UsernamePasswordAuthenticationToken authentication;
    private final List<GrantedAuthority> authorities = Lists.newArrayList();
    private final Person person = new Person();

    private String username;

    public NeSIUserDetailsImpl(final String username) {

        this.username = username;

        person.setNesiUniqueId(username);
        final GroupMembership gm = new GroupMembership();
        gm.setGroup(new Group("admin"));

        person.addGroup(gm);

        for (final GroupMembership m : person.getGroups()) {
            authorities.add(new GrantedAuthority() {

                @Override
                public String getAuthority() {
                    return "PERM_" + m.getGroup().getGroupName();
                }
            });
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return authentication;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities;
    }

    @Override
    public String getPassword() {
        return (String)this.authentication.getCredentials();
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setAuthentication(
            final UsernamePasswordAuthenticationToken authentication) {
        this.authentication = authentication;
    }
}
