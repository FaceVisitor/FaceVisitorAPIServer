package com.facevisitor.api.domain.security;

import com.facevisitor.api.domain.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SecurityUser extends org.springframework.security.core.userdetails.User {


    private Long id;

    private String email;

    private String name;


    private Collection<? extends GrantedAuthority> authorities;

    public SecurityUser(Long id,String email, String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.id = id;
        this.email = email;
        this.name = username;
    }

    public static SecurityUser create(User user) {
        List<GrantedAuthority> authorities = user.getAuthorities().stream().map(role ->
                new SimpleGrantedAuthority(role.getRole())
        ).collect(Collectors.toList());

        return new SecurityUser(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPassword(),
                authorities
        );
    }

}
