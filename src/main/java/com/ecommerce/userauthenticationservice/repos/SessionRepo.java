package com.ecommerce.userauthenticationservice.repos;

import com.ecommerce.userauthenticationservice.models.Session;
import com.ecommerce.userauthenticationservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionRepo extends JpaRepository<Session, User> {
    List<Session> findByUser(User user);
    Optional<Session> findByToken(String token);
    Optional<Session> findByTokenAndUser_Id(String token,  Long userId);
}
