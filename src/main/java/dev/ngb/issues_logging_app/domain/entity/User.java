package dev.ngb.issues_logging_app.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private UUID id;

    @Column(name = "username")
    private String username;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "hash_password")
    private String password;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "is_admin")
    private Boolean isAdmin = false;

    @Column(name = "is_blocked")
    private Boolean isBlocked = false;

    @OneToMany(mappedBy = "user")
    private Set<Notification> notifications = new LinkedHashSet<>();

}