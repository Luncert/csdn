package org.luncert.csdn2.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ArticleServiceAspect {

    private static final String PACKAGE_NAME = "org.luncert.csdn2.service.ArticleService";

    private final static Logger logger =
        LoggerFactory.getLogger(ArticleServiceAspect.class);

    @Pointcut("execution(public * " + PACKAGE_NAME + ".getArticle())")
    public void getArticle(){}

    @Around("getArticle()")
    public Object handleGetArticle(ProceedingJoinPoint pjp)
    {
        try {
            return pjp.proceed();
        } catch (Throwable e) {
            return null;
        }
    }
    
}