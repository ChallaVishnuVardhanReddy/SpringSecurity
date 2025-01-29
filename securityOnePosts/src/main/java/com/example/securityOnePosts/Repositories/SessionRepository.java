package com.example.securityOnePosts.Repositories;

import com.example.securityOnePosts.Entities.Session;
import com.example.securityOnePosts.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session,Long> {
    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
