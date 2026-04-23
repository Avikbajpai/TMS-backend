package com.nexustms.repository;

import com.nexustms.model.User;
import com.nexustms.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByRole(UserRole role);

    Optional<User> findByUsername(String username);
}
