package nz.org.nesi.researchHub.auth;

import com.google.common.collect.Lists;
import nz.org.nesi.researchHub.model.Group;
import nz.org.nesi.researchHub.model.GroupMembership;
import nz.org.nesi.researchHub.model.Person;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * Project: project_management
 * <p/>
 * Written by: Markus Binsteiner
 * Date: 11/12/13
 * Time: 1:47 PM
 */
public class NeSIUserDetailsImpl implements UserDetails {

    private String username;
    private Person person = new Person();
    private UsernamePasswordAuthenticationToken authentication;

    private List<GrantedAuthority> authorities = Lists.newArrayList();

    public NeSIUserDetailsImpl(String username) {

        this.username = username;

        person.setNesiUniqueId(username);
        GroupMembership gm = new GroupMembership();
        gm.setGroup(new Group("admin"));

        person.addGroup(gm);

        for ( final GroupMembership m : person.getGroups() ) {
            authorities.add(new GrantedAuthority(){

                @Override
                public String getAuthority() {
                    return "PERM_"+m.getGroup().getGroupName();
                }
            });
        }
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {



        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPassword() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

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

    public void setAuthentication(UsernamePasswordAuthenticationToken authentication) {
        this.authentication = authentication;
    }

    public UsernamePasswordAuthenticationToken getAuthentication() {
        return authentication;
    }
}
