package com.daniel.app.airbnb.backend.service;


import com.daniel.app.airbnb.backend.dto.BecomeHostDto;
import com.daniel.app.airbnb.backend.exception.NoSuchUserExp;
import com.daniel.app.airbnb.backend.model.enums.Role;
import com.daniel.app.airbnb.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BecomeHostService {
    private final UserRepository userRepository;

    public String becomeHost(BecomeHostDto becomeHostDto) {
        var newUser = userRepository.findUserByEmail(becomeHostDto.getEmail());
        if (newUser.isEmpty() || !newUser.get().isVerified()) {
            throw new NoSuchUserExp("please create account or make sure you " +
                    "are verified to become host");
        }
        var save = newUser.get();
        save.setSuperhost(true);
        save.getRoles().add(Role.ROLE_HOST);
        save.setRoles(save.getRoles());
        userRepository.save(save);
        return "you are now a host, starts creating houses for people to " +
                "book";

    }
}
