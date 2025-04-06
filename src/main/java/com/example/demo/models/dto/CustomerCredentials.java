package com.example.demo.models.dto;

import com.example.demo.type.Role;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CustomerCredentials {
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

}
