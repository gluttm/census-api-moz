package mz.co.truetech.service;
 
import java.util.*;
 
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import mz.co.truetech.entity.Permission;
import mz.co.truetech.entity.Role;
import mz.co.truetech.entity.User;
 
public class AppUserDetails implements UserDetails {
 
	private static final long serialVersionUID = 1L;
	private User user;
     
    public AppUserDetails(User user) {
        this.user = user;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
         
        for (Role role : roles) {
        	for (Permission permission: role.getPermissions()) {
        		authorities.add(new SimpleGrantedAuthority(permission.getName()));
        	}
        	authorities.add(new SimpleGrantedAuthority("ROLE_"+role.getName()));
        }
         
        return authorities;
    }
 
    @Override
    public String getPassword() {
        return user.getPassword();
    }
 
    @Override
    public String getUsername() {
        return user.getUsername();
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
 
}