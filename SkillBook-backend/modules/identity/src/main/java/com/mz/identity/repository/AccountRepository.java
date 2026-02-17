package com.mz.identity.repository;

import com.mz.identity.mappers.AuthMapper;
import com.mz.identity.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByEmail(String email);
}
