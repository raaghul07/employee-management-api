
package com.raaghul.employeemanagement.service;

import com.raaghul.employeemanagement.dto.RegisterRequestDTO;
import com.raaghul.employeemanagement.dto.LoginRequestDTO;
import com.raaghul.employeemanagement.model.AppUser;
import com.raaghul.employeemanagement.repository.AppUserRepository;
import com.raaghul.employeemanagement.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequestDTO request) {

        String username = request.username().trim();

        if (userRepository.existsByUsername(username)) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Username already exists"
            );
        }

        String hashedPassword = passwordEncoder.encode(
            request.password()
        );

        AppUser user = new AppUser(
            username,
            hashedPassword
        );

        userRepository.save(user);
    }

    public String login(LoginRequestDTO request) {

        AppUser user = userRepository
            .findByUsername(request.username().trim())
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password"
            ));

        boolean passwordMatches = passwordEncoder.matches(
            request.password(),
            user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Invalid username or password"
            );
        }

        return jwtService.generateToken(user.getUsername());
    }
}
