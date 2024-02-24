package ru.sberbank.jd.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.sberbank.jd.user.model.UserInfo;
import ru.sberbank.jd.user.repository.UserRepository;

/**
 * User details from database UserInfo.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo userInfo = repository.findByEmail(username);

        if (userInfo == null) {
            throw new UsernameNotFoundException("User with email " + username + " doesn't exist");
        }

        return new CustomUserDetails(userInfo);
    }
}
