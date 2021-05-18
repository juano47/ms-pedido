package ms.pedido.aspect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import ms.pedido.domain.Obra;
import ms.pedido.domain.Pedido;

@Aspect
@Component
public class LogAspect {

	private RestTemplate restTemplate = new RestTemplate();

	String puertoMsUsuario = "8084";
	String urlServer = "http://localhost";
	String api = "api";
	String server = urlServer+":"+puertoMsUsuario+"/"+api;

	private static final Logger logger = LoggerFactory.getLogger(LogAspect. class);

	@Pointcut("execution(* ms.pedido.service.*.*(..))")
	private void serviceMethods() {}

	@Pointcut("execution(public * org.springframework.data.jpa.repository.JpaRepository+.*(..))")
	private void repositoryMethods() {}

	@Before("serviceMethods()")
	public void logBeforeServiceMethod(JoinPoint jp) {
		logger.info("Se ejecutará: "+jp.getTarget().getClass()+"."+jp.getSignature().getName()+Arrays.toString(jp.getArgs()));
	}

	@Pointcut("execution(* ms.pedido.service.impl.PedidoServiceImpl.findPedidoById(..))")
	private void findPedidoById() {}

	@Pointcut("execution(* ms.pedido.service.impl.PedidoServiceImpl.findAll(..))")
	private void findAll() {}

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

	@Around("findPedidoById()")
	public Optional<Pedido> obraPorIdMsUsuario(ProceedingJoinPoint jp) throws Throwable {

		Optional<Pedido> resultadoOptional = (Optional<Pedido>) jp.proceed();

		if(resultadoOptional.isPresent()) {

			Pedido resultado = resultadoOptional.get(); 
			logger.info("Se ejecutará HttpMethod.GET - URI: /api/obra/"+resultado.getObraId()+" - ms-usuario");

			try {
				ResponseEntity<Obra> respuesta = restTemplate.exchange(server+"/obra/"+resultado.getObraId(), HttpMethod.GET,new HttpEntity<>(null) , Obra.class);

				if(respuesta.getStatusCode().equals(HttpStatus.OK)) {
					Obra nuevaObra = new Obra();
					nuevaObra.setId(respuesta.getBody().getId());
					nuevaObra.setDescripcion(respuesta.getBody().getDescripcion());
					resultado.setObra(nuevaObra);
					logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": HttpStatus.OK");

					return Optional.of(resultado);
				}
				else {
					logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": "+respuesta.getStatusCode().toString());
					return Optional.of(resultado);
				}

			}catch(Exception e1) {
				logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": "+e1.getLocalizedMessage());
				return resultadoOptional;

			}

		}
		else return resultadoOptional;

	}


	@Around("findAll()")
	public List<Pedido> findAllObrasMsUsuario(ProceedingJoinPoint jp) throws Throwable {
		List<Pedido> resultado = (List<Pedido>) jp.proceed();
		List<Pedido> nuevoResultado = new ArrayList<Pedido>();

		for(Pedido pedido : resultado) 
			nuevoResultado.add(findObraPorId(pedido));

		return nuevoResultado.stream().collect(Collectors.toList());
	}


	@Transactional(propagation=Propagation.REQUIRES_NEW)
	private Pedido findObraPorId(Pedido resultado) {
		logger.info("Se ejecutará HttpMethod.GET - URI: /api/obra/"+resultado.getObraId()+" - ms-usuario");
		try {
		ResponseEntity<Obra> respuesta = restTemplate.exchange(server+"/obra/"+resultado.getObraId(), HttpMethod.GET,new HttpEntity<>(null) , Obra.class);

		if(respuesta.getStatusCode().equals(HttpStatus.OK)) {
			Obra nuevaObra = new Obra();
			nuevaObra.setId(respuesta.getBody().getId());
			nuevaObra.setDescripcion(respuesta.getBody().getDescripcion());
			resultado.setObra(nuevaObra);
			logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": HttpStatus.OK");
			return resultado;
		}
		else {
			logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": "+respuesta.getStatusCode().toString());
			return resultado;
		}
		
		}catch(Exception e1) {
			logger.info("StatusCode respuesta ms-usuario obraPorId="+resultado.getObraId()+": "+e1.getLocalizedMessage());
			return resultado;

		}
	}

}
