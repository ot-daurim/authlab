package authlab.feature.auth.repository;

import authlab.feature.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = """
            SELECT EXISTS (
                SELECT 1 FROM users WHERE LOWER(email) = LOWER(:email)
            )
            """, nativeQuery = true)
    boolean existsByEmailIgnoreCase(@Param("email") String email);

    @Query(value = """
            SELECT EXISTS (
                SELECT 1 FROM users WHERE LOWER(username) = LOWER(:username)
            )
            """, nativeQuery = true)
    boolean existsByUsernameIgnoreCase(@Param("username") String username);
}