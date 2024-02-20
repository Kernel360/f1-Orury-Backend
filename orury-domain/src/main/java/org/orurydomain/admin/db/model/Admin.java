package org.orurydomain.admin.db.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.orurydomain.admin.dto.RoleType;
import org.orurydomain.base.db.AuditingField;

@Slf4j
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity
public class Admin extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "email", length = 20, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType role;

    private Admin(String name, String email, String password, RoleType role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public static Admin of(String name, String email, String password, RoleType role) {
        return new Admin(name, email, password, role);
    }
}