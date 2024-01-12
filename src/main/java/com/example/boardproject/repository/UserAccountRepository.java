package com.example.boardproject.repository;

import com.example.boardproject.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author daecheol song
 * @since 1.0
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}
