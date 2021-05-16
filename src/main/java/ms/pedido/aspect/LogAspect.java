package ms.pedido.aspect;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	private static final Logger logger = LoggerFactory.getLogger(LogAspect. class);

	@Pointcut("execution(* ms.pedido.service.*.*(..))")
	private void serviceMethods() {}

	@Pointcut("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))")
	private void repositoryMethods() {}

	@Before("serviceMethods()")
	public void logBeforeServiceMethod(JoinPoint jp) {
		logger.info("Se ejecutará: "+jp.getTarget().getClass()+"."+jp.getSignature().getName()+Arrays.toString(jp.getArgs()));
	}

	@Before("repositoryMethods()")
	public void logBeforeRepositoryMethod(JoinPoint jp) {
		logger.info("Se ejecutará repositoryMethod: "+jp.getSignature().getName()+Arrays.toString(jp.getArgs()));
		
	}

	@AfterThrowing(pointcut = "serviceMethods() || repositoryMethods()", throwing = "ex")
	public void logAfterThrowing(JoinPoint jp, Exception ex) {
		if(ex instanceof DataIntegrityViolationException)
			logger.info(jp.getSignature().getName()+Arrays.toString(jp.getArgs())+" lanzó excepción: "+((DataIntegrityViolationException) ex).getMostSpecificCause().toString());
		else
			logger.info(jp.getSignature().getName()+Arrays.toString(jp.getArgs())+" lanzó excepción: "+ex.getMessage());
	}
	
	

}
