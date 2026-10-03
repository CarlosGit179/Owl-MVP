package br.com.carlos.Owl.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import br.com.carlos.Owl.repository.UserRepository;

/** Loads application users by login for Spring Security authentication. */
@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * Looks up the account associated with the supplied login.
     *
     * @param username login to look up
     * @return the matching user details, or {@code null} when no account exists
     */
    @Override
    public UserDetails loadUserByUsername(String username) {
        UserDetails user = userRepository.findByLogin(username);

        return user;
    }

}
