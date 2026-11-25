package authlab.feature.auth.dto.response;

import java.time.Instant;

public record RegisterResponse(Long id, String username, Instant createdAt) {
}
