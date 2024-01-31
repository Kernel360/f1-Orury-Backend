package org.fastcampus.orurycommon.log;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Around(value = "within(@org.springframework.stereotype.Service *)")
    public Object serviceLoggingProcess(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        String methodSignature = proceedingJoinPoint.getSignature().toShortString();
        Object objects = proceedingJoinPoint.getArgs();
        log.info("### Method Start : {} , Parameters : {} ", methodSignature, objects);

        try {
            Object res = proceedingJoinPoint.proceed();
            log.info("### Method End : {} , Result : {} ", methodSignature, res);
            return res;
        } catch (Throwable throwable) {
            log.error("### Error Occurred in Method : {} , Message : {} ", methodSignature, throwable.getMessage(), throwable);
            throw throwable;
        }
    }
}
