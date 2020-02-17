package com.bcp.falcon.reto.backendservices.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author Kei Takayama
 * Created on 2/17/20.
 */

@Aspect
@Component
public class RestServiceMonitor extends AbstractRestServiceMonitor {

    @Around("execution(* com.bcp.falcon.reto.backendservices.notification.expose.web.NotificationRestService.*(..)) ||" +
            "execution(* com.bcp.falcon.reto.backendservices.security.expose.web.AuthorizationRestService.*(..))")
    public Object restServicesLogger(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        return this.restLogger(proceedingJoinPoint);
    }
}
