package com.example.demo.repositories;

import com.example.demo.models.dto.CustomerCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerCredentialRepository extends JpaRepository<CustomerCredentials, Long> {
    Optional<CustomerCredentials> findByUsername(String username);

}
