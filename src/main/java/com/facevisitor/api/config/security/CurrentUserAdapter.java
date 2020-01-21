package com.facevisitor.api.config.security;

import com.facevisitor.api.domain.security.Authority;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CurrentUserAdapter extends User {
    private com.facevisitor.api.domain.user.User user;

    public CurrentUserAdapter(com.facevisitor.api.domain.user.User user){
        super(user.getEmail(),user.getPassword(),toGranted(user.getAuthorities()));
        System.out.println("EMAI" + user.getEmail());
        this.user = user;
    }

    public static Set<SimpleGrantedAuthority> toGranted(Set<Authority> authorities) {
        return authorities.stream().map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getRole())).collect(Collectors.toSet());
    }




}
