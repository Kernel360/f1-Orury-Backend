package org.fastcampus.oruryclient.config;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserPrincipalSecurityContextFactory.class)
public @interface WithUserPrincipal {

    long id() default 1L;

    String email() default "orury@orury.com";
}
