package com.daniel.app.airbnb.backend.detail;

import com.daniel.app.airbnb.backend.model.User;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailWithFullListings(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not " +
                        "found"));
        return new CustomUserDetail(user);
    }
}
