package authlab.feature.auth.service;

import authlab.feature.auth.dto.request.RegisterRequest;
import authlab.feature.auth.dto.response.RegisterResponse;

public interface RegistrationService {
    RegisterResponse register(RegisterRequest request);
}