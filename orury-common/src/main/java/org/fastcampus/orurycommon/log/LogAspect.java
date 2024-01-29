package org.fastcampus.orurycommon.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("@annotation(org.fastcampus.orurycommon.log.Logging)")
    public void methodRuntime() {
    }

    @Around("methodRuntime()")
    public Object serviceLoggingProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();

        Object objects = proceedingJoinPoint.getArgs();
        log.info("### Method Start : {} , Parameters : {} ", methodSignature.toShortString(), objects);

        try {
            Object res = proceedingJoinPoint.proceed();
            log.info("### Method End : {} , Result : {} ", methodSignature.toShortString(), res);
            return res;
        } catch (Throwable throwable) {
            log.error("### Error Occurred in Method : {} , Message : {} ", methodSignature.toShortString(), throwable.getMessage());
            throw throwable;
        }
    }
}
