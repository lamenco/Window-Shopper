package com.example.demo.aop;

import com.example.demo.service.LogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    private final LogService logService;

    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    @Pointcut("execution(* com.example.demo.web.OfferController.addOffer(..))")
    public  void    detailsPointCut(){

    }
    @Pointcut("execution(* com.example.demo.web.OfferController.addDoorOffer(..))")
    public  void    detailsPointCutDoor(){

    }
    @After("detailsPointCut()")
    public void afterAdvice(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String  action =  joinPoint.getSignature().getName();
        logService.createLog(action);
    }
    @After("detailsPointCutDoor()")
    public void afterAdviceDoor(JoinPoint joinPoint){
        Object[] args = joinPoint.getArgs();
        String  action =  joinPoint.getSignature().getName();
        logService.createLog(action);
    }
}
