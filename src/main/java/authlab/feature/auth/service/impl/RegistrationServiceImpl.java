package authlab.feature.auth.service.impl;

import lombok.RequiredArgsConstructor;
import authlab.feature.auth.dto.request.RegisterRequest;
import authlab.feature.auth.dto.response.RegisterResponse;
import authlab.feature.auth.service.RegistrationService;
import authlab.feature.user.entity.User;
import authlab.feature.user.repository.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private static final String EMAIL_INDEX = "ux_users_email_lower";
    private static final String USERNAME_INDEX = "ux_users_username_lower";
    private static final String CONFLICT_MESSAGE = "Email or username already in use";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmailIgnoreCase(request.email())
                || userRepository.existsByUsernameIgnoreCase(request.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLICT_MESSAGE);
        }

        String passwordHash = passwordEncoder.encode(request.password());
        User user = new User(request.username(), request.email(), passwordHash);

        try {
            User saved = userRepository.saveAndFlush(user);
            return new RegisterResponse(saved.getId(), saved.getUsername(), saved.getCreatedAt());
        } catch (DataIntegrityViolationException exception) {
            if (isRegistrationConflict(exception)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, CONFLICT_MESSAGE, exception);
            }
            throw exception;
        }
    }

    private boolean isRegistrationConflict(DataIntegrityViolationException exception) {
        for (Throwable cause = exception.getCause(); cause != null; cause = cause.getCause()) {
            if (cause instanceof ConstraintViolationException violation) {
                String constraintName = violation.getConstraintName();
                return EMAIL_INDEX.equals(constraintName) || USERNAME_INDEX.equals(constraintName);
            }
        }
        return false;
    }
}
