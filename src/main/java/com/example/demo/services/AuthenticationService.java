package com.example.demo.services;

import com.example.demo.models.dto.CustomerCredentials;
import com.example.demo.models.request.AuthenticationRequest;
import com.example.demo.models.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    AuthenticationResponse refreshToken(String refreshToken);
    void saveToken(CustomerCredentials credentials, String token);
}
