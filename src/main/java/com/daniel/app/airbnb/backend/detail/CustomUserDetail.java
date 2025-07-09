package com.daniel.app.airbnb.backend.detail;

import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.util.RoleUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
public class CustomUserDetail implements UserDetails {


    private  final User user;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return RoleUtil.authorities(user);
    }

    @Override
    public boolean isAccountNonLocked() {
        return user.isVerified();
    }

    @Override
    public boolean isEnabled() {
        return user.isVerified();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

}
