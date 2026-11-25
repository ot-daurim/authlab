package authlab.feature.user.dto.response;
import java.time.Instant;

public record UserProfileResponse(
        Long id,
        String username,
        Instant createdAt
) {
}
