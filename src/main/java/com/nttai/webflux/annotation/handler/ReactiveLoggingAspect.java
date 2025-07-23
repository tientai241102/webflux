package com.nttai.webflux.annotation.handler;


import com.nttai.webflux.annotation.ReactiveLog;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class ReactiveLoggingAspect {

    @Around("execution(* *(..)) && @annotation(reactiveLog)")
    public Object logReactiveMethods(ProceedingJoinPoint joinPoint, ReactiveLog reactiveLog) throws Throwable {
        Object result = joinPoint.proceed();
        if (result instanceof Mono) {
            return ((Mono<?>) result)
                    .doOnSubscribe(subscription -> System.out.println("Mono started: " + joinPoint.getSignature()))
                    .doOnTerminate(() -> System.out.println("Mono completed: " + joinPoint.getSignature()));
        } else if (result instanceof Flux) {
            return ((Flux<?>) result)
                    .doOnSubscribe(subscription -> System.out.println("Flux started: " + joinPoint.getSignature()))
                    .doOnTerminate(() -> System.out.println("Flux completed: " + joinPoint.getSignature()));
        }
        return result;
    }
}