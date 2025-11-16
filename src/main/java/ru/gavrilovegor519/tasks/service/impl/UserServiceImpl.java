package ru.gavrilovegor519.tasks.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gavrilovegor519.tasks.dto.output.users.TokenDto;
import ru.gavrilovegor519.tasks.entity.User;
import ru.gavrilovegor519.tasks.exception.DuplicateUserException;
import ru.gavrilovegor519.tasks.exception.IncorrectPasswordException;
import ru.gavrilovegor519.tasks.exception.UserNotFoundException;
import ru.gavrilovegor519.tasks.repo.UserRepository;
import ru.gavrilovegor519.tasks.security.JwtUtilities;
import ru.gavrilovegor519.tasks.service.UserService;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final JwtUtilities jwtUtilities;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public TokenDto login(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        User user1 = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found."));

        if (!passwordEncoder.matches(password, user1.getPassword())) {
            throw new IncorrectPasswordException("Incorrect password!");
        }

        TokenDto tokenDto = new TokenDto();
        tokenDto.setToken(jwtUtilities.generateToken(user1.getUsername(), "ROLE_USER"));
        return tokenDto;
    }

    @Override
    @Transactional
    public void reg(User user) {
        boolean emailIsExist = userRepository.existsByEmail(user.getEmail());

        if (emailIsExist) {
            throw new DuplicateUserException("Duplicate E-Mail.");
        }

        // Encode password before saving - ensure this is done properly
        String rawPassword = user.getPassword();
        if (rawPassword != null && !rawPassword.isEmpty()) {
            String encodedPassword = passwordEncoder.encode(rawPassword);
            user.setPassword(encodedPassword);
        } else {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        userRepository.save(user);
    }
}
