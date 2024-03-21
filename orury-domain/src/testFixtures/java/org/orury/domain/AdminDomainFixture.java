package org.orury.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Builder;
import lombok.Getter;
import org.orury.domain.admin.domain.dto.RoleType;
import org.orury.domain.admin.domain.entity.Admin;

import java.util.Set;

public class AdminDomainFixture {

    private AdminDomainFixture() {
        throw new IllegalStateException("Utility class");
    }

    private static final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Getter
    @Builder
    public static class TestAdmin {
        private @Builder.Default Long id = 1L;
        private @Builder.Default String name = "name";
        private @Builder.Default String email = "email";
        private @Builder.Default String password = "pw";
        private @Builder.Default Set<RoleType> roleTypes = Set.of(RoleType.ADMIN);

        public static TestAdmin.TestAdminBuilder createAdmin() {
            return TestAdmin.builder();
        }

        public Admin get() {
            return mapper.convertValue(this, Admin.class);
        }
    }
}
