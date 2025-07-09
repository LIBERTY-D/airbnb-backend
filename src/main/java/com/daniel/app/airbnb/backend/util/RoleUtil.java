package com.daniel.app.airbnb.backend.util;

import com.daniel.app.airbnb.backend.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.validation.SimpleErrors;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RoleUtil {


    public static Set<SimpleGrantedAuthority> authorities(User user){
        return user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.name())).collect(Collectors.toSet());
    }
}
