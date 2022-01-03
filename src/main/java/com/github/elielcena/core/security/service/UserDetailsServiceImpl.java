package com.github.elielcena.core.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.elielcena.domain.model.User;
import com.github.elielcena.domain.repository.UserRepository;

/**
 * @author elielcena
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String MSG_RESOURCE_NOT_FOUND = "User Not Found with username %s";

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(MSG_RESOURCE_NOT_FOUND, username)));

        return UserDetailsImpl.build(user);
    }

}
