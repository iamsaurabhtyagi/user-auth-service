package com.bmt.auth.service;

import com.bmt.auth.dao.UserDao;
import com.common.exception.UserNotFoundException;
import com.common.model.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserDao userDao;

    public CustomUserDetailsService(UserDao userDao) {
        this.userDao = userDao;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByEmail(username)
                .or(() -> userDao.findByPhone(username))
                .or(() -> userDao.findByUsername(username))
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword()) // already encoded
                //.authorities("ROLE_USER")
                //.accountLocked(!user.getIsActive())
                .build();
    }
}
