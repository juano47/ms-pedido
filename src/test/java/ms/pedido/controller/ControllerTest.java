package ms.pedido.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.Obra;
import ms.pedido.domain.Pedido;
import ms.pedido.domain.Producto;
import ms.pedido.service.PedidoService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ControllerTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@MockBean
	private PedidoService pedidoService;
	
	@LocalServerPort
	String puerto;

	private Pedido p= new Pedido();
	private final String urlServer = "http://localhost";
	private final String apiPedido = "api/pedido";
	
	@BeforeEach
	void setUp() throws Exception {
		Obra obra= new Obra();
		obra.setId(1);
		obra.setDescripcion("Una Obra");
		
		Producto producto= new Producto();
		producto.setId(1);
		producto.setPrecio(100.00);
		producto.setDescripcion("Un producto");
		
		List<DetallePedido> listaDetalle= new ArrayList<>();
		DetallePedido detalle= new DetallePedido();
		detalle.setCantidad(2);
		detalle.setProducto(producto);
		detalle.setPrecio(detalle.getCantidad()*detalle.getProducto().getPrecio());
		listaDetalle.add(detalle);
		
		p.setDetalle(listaDetalle);
		p.setObra(obra);
		p.setFechaPedido(Instant.now());
		
		
	}
	
	@Test
	void testGuardarPedidoObraEnNull() {
		String server= urlServer+":"+puerto+"/"+apiPedido;
		
		p.setObra(null);
		
		HttpEntity<Pedido> requestPedido= new HttpEntity<>(p);
		ResponseEntity<String> respuesta= restTemplate.exchange(server, HttpMethod.POST, requestPedido, String.class);
		
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	void testGuardarDetallePedidoPedidoNoEncontrado() {

		String server= urlServer+":"+puerto+"/"+apiPedido+"/2/detalle";
		
		Optional<Pedido> opt= Optional.empty();
		
		when(pedidoService.findPedidoById(any(Integer.class))).thenReturn(opt);
		
		Producto producto= new Producto();
		producto.setId(2);
		producto.setPrecio(100.00);
		producto.setDescripcion("Otro producto");
		DetallePedido detalle= new DetallePedido();
		detalle.setCantidad(1);
		detalle.setProducto(producto);
		detalle.setPrecio(detalle.getCantidad()*detalle.getProducto().getPrecio());
		
		HttpEntity<DetallePedido> requestDetallePedido= new HttpEntity<>(detalle);
		ResponseEntity<DetallePedido> respuesta= restTemplate.exchange(server, HttpMethod.POST, requestDetallePedido, DetallePedido.class);
	
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.NOT_FOUND));
	}
	
	
	@Test
	void testActualizarPedidoPedidoNoEncontrado() {

		String server= urlServer+":"+puerto+"/"+apiPedido+"/2";
		
		Optional<Pedido> opt= Optional.empty();
		
		when(pedidoService.findPedidoById(any(Integer.class))).thenReturn(opt);
		
		HttpEntity<Pedido> requestPedido= new HttpEntity<>(p);
		ResponseEntity<String> respuesta= restTemplate.exchange(server, HttpMethod.PUT, requestPedido, String.class);
		
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.NOT_FOUND));
		
	}
	
	@Test
	void testBorrarPedidoPedidoNoEncontrado() {

		String server= urlServer+":"+puerto+"/"+apiPedido+"/2";
		
		Optional<Pedido> opt= Optional.empty();
		
		when(pedidoService.findPedidoById(any(Integer.class))).thenReturn(opt);
		
		HttpEntity<Pedido> requestPedido= new HttpEntity<>(p);
		ResponseEntity<String> respuesta= restTemplate.exchange(server, HttpMethod.DELETE, requestPedido, String.class);
		
		assertTrue(respuesta.getStatusCode().equals(HttpStatus.NOT_FOUND));
		
	}
	
	
	
	

}
