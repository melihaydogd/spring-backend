package com.project.security.model.token;

import com.project.security.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
        name = "token",
        schema = "public",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "token_unique",
                        columnNames = "token")
        }
)
public class Token {

    @Id
    @GeneratedValue
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(
            name = "token",
            nullable = false
    )
    private String token;

    @Column(
            name = "token_type",
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    @Column(
            name = "revoked",
            nullable = false,
            columnDefinition = "boolean default false"
    )
    private boolean revoked;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;
}
