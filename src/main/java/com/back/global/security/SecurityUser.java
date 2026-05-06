package com.back.global.security;

import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class SecurityUser extends User {

    private int id;
    private String nickname;

    public SecurityUser(int id, String username, @Nullable String password, String nickname, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.id = id;
        this.nickname = nickname;
    }
    public int getId() {
        return id;
    }

    public String getNickname() {
        return nickname;
    }
}
