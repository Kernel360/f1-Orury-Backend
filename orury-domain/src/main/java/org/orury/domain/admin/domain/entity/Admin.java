package org.orury.domain.admin.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.orury.domain.admin.domain.dto.RoleType;
import org.orury.domain.admin.domain.dto.RoleTypesConverter;
import org.orury.domain.base.db.AuditingField;

import java.util.Set;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"}, callSuper = false)
@Entity(name = "Admin")
public class Admin extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 20, nullable = false)
    private String name;

    @Column(name = "email", length = 20, nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role", length = 20, nullable = false)
    @Convert(converter = RoleTypesConverter.class)
//    @Enumerated(EnumType.STRING)
    private Set<RoleType> roleTypes;

    private Admin(String name, String email, String password, Set<RoleType> roleTypes) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roleTypes = roleTypes;
    }

    public static Admin of(String name, String email, String password, Set<RoleType> roleTypes) {
        return new Admin(name, email, password, roleTypes);
    }
}