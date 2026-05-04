package org.example.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.domain.user.RoleType;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserJpaEntity extends BaseEntity {
    private String userName;

    @Enumerated(EnumType.STRING)
    private RoleType role;
}