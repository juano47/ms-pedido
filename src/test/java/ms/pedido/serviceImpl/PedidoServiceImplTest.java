package ms.pedido.serviceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
 

import ms.pedido.dao.PedidoRepository;
import ms.pedido.domain.Obra;
import ms.pedido.domain.Pedido;
import ms.pedido.domain.Producto;
import ms.pedido.domain.DetallePedido;
import ms.pedido.service.ClienteService;
import ms.pedido.service.PedidoService;
import ms.pedido.service.ProductoService;

@SpringBootTest
public class PedidoServiceImplTest {
	
	@Autowired
	PedidoService pedidoService;
	
	@MockBean
	ClienteService clienteService;
	
	@MockBean
	ProductoService productoService;
	
	@MockBean
	PedidoRepository pedidoRepository;
	
	Pedido p= new Pedido();
	
	
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
	void savePedidoConExito(){
		
		when(productoService.getStock(any(Integer.class))).thenReturn(10);
		when(clienteService.saldo(any(Integer.class))).thenReturn(500.00);
		
		Pedido pedidoResultado= null;
		Exception excep=null;
		
		try {
			pedidoResultado= pedidoService.save(p);
		} catch (Exception e) {
			excep=e;
		}
		
		assertNull(excep);
		assertEquals("ACEPTADO", pedidoResultado.getEstado().getEstado());
		verify(pedidoRepository, times(1)).save(any(Pedido.class));
	}
	
	@Test
	void savePedidoException() {
		
		when(clienteService.saldo(any(Integer.class))).thenReturn(100.00);
		
		Pedido pedidoResultado=null;
		Exception excep=null;
		
		try {
			pedidoResultado= pedidoService.save(p);
		} catch (Exception e) {
			excep=e;
		}
		
		assertNotNull(excep);
		assertNull(pedidoResultado);
		verify(pedidoRepository, never()).save(any(Pedido.class));
	}
	
	@Test
	void deleteDetallePedidoExito() {
		Exception excep= null;
		
		p.getDetalle().get(0).setId(1);
		
		Optional<Pedido> opt= Optional.of(p);
		
		when(pedidoRepository.findById(any(Integer.class))).thenReturn(opt);
		
		try {
			pedidoService.deleteDetallePedido(1, 1);
		}
		catch(Exception e) {
			excep=e;
		}
		
		assertNull(excep);
		verify(pedidoRepository, times(1)).save(any(Pedido.class));	
	}
	
	@Test
	void deleteDetallePedidoPedidoNoEncontrado() {
		Exception excep= null;
		
		Optional<Pedido> opt= Optional.empty();
		
		when(pedidoRepository.findById(any(Integer.class))).thenReturn(opt);
		
		try {
			pedidoService.deleteDetallePedido(1, 1);
		}
		catch(Exception e) {
			excep=e;
		}
		
		assertNotNull(excep);
		verify(pedidoRepository, never()).save(any(Pedido.class));	
	}
	
	@Test
	void deleteDetallePedidoDetalleNoEncontrado() {
		Exception excep= null;
		
		Optional<Pedido> opt= Optional.of(p);
		
		when(pedidoRepository.findById(any(Integer.class))).thenReturn(opt);
		
		try {
			pedidoService.deleteDetallePedido(1, 3);
		}
		catch(Exception e) {
			excep=e;
		}
		
		assertNotNull(excep);
		verify(pedidoRepository, never()).save(any(Pedido.class));	
	}
}
