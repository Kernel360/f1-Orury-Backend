package org.fastcampus.oruryadmin.domain.notice.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryadmin.domain.admin.db.model.Admin;
import org.fastcampus.oruryadmin.domain.base.db.AuditingField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Slf4j
@ToString
@Getter
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Notice extends AuditingField {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @ManyToOne(optional = false)
    private Admin admin;

    private Notice(String title, String content, Admin admin) {
        this.title = title;
        this.content = content;
        this.admin = admin;
    }

    public static Notice of(String title, String content, Admin admin) {
        return new Notice(title, content, admin);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Notice notice)) return false;
        return Objects.equals(id, notice.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    protected Notice() {
    }
}