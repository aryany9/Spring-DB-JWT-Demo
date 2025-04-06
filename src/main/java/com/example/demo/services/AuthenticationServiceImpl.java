package com.example.demo.services;

import com.example.demo.models.dto.CustomUserDetails;
import com.example.demo.models.dto.CustomerCredentials;
import com.example.demo.models.dto.Token;
import com.example.demo.models.request.AuthenticationRequest;
import com.example.demo.models.response.AuthenticationResponse;
import com.example.demo.repositories.CustomerCredentialRepository;
import com.example.demo.repositories.TokenRepository;
import com.example.demo.securities.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private CustomerCredentialRepository credentialsRepo;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private PasswordEncoder encoder;


    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.debug("Authenticating");
        var credentials = credentialsRepo.findByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username"));

        if (!encoder.matches(request.password(), credentials.getPassword())) {
            log.debug("request password: {} and credentials password: {}", request.password(), credentials.getPassword());
            log.debug("Invalid Password");
            throw new BadCredentialsException("Invalid password");
        }

        var userDetails = new CustomUserDetails(credentials);

        var accessToken = jwtUtil.generateToken(userDetails);
        var refreshToken = jwtUtil.generateRefreshToken(userDetails);

        saveToken(credentials, refreshToken);

        return new AuthenticationResponse(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponse refreshToken(String refreshToken) {
        var username = jwtUtil.extractUser(refreshToken)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        var credentials = credentialsRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!jwtUtil.isRefreshTokenValid(refreshToken, credentials)) {
            throw new RuntimeException("Invalid or expired refresh token");
        }
        var userDetails = new CustomUserDetails(credentials);
        var newAccessToken = jwtUtil.generateToken(userDetails);
        return new AuthenticationResponse(newAccessToken, refreshToken);
    }

    @Override
    public void saveToken(CustomerCredentials credentials, String token) {
        Token t = new Token();
        t.setToken(token);
        t.setExpired(false);
        t.setRevoked(false);
        t.setCredentials(credentials);
        tokenRepository.save(t);
    }
}
