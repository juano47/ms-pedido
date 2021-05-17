package ms.pedido.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ms.pedido.dao.PedidoRepository;
import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.EstadoPedido;
import ms.pedido.domain.Pedido;
import ms.pedido.message.queue.MessageSenderPedidos;
import ms.pedido.service.ClienteService;
import ms.pedido.service.PedidoService;
import ms.pedido.service.impl.ClienteServiceImpl;

@RestController
@RequestMapping("api/pedido")
@Api(value = "PedidoController", description = "Permite gestionar los pedidos")
public class PedidoController {

	@Autowired
	PedidoService pedidoService;

	@Autowired
	ClienteService clienteService;

	@Autowired
	MessageSenderPedidos messageSenderPedidos;

	@PostMapping
	@ApiOperation(value = "Crea un pedido")
	public ResponseEntity<?> crear(@RequestBody Pedido nuevo) {
		if (nuevo.getObra() != null && nuevo.getDetalle() != null && !nuevo.getDetalle().isEmpty()
				&& nuevo.getDetalle().get(0).getCantidad() != null && nuevo.getDetalle().get(0).getProducto() != null) {
			try {
				nuevo = pedidoService.save(nuevo);
			} catch (Exception e) {
				return ResponseEntity.status(500).body(e.getMessage());
			}
			return ResponseEntity.ok(nuevo);
		} else {
			return ResponseEntity.badRequest().body("EL PEDIDO DEBE CONTENER DATOS DE LA OBRA");
		}

	}

	@PostMapping(path = "/{idPedido}/detalle}")
	@ApiOperation(value = "Agregar un detalle a un pedido")
	public ResponseEntity<DetallePedido> agregarDetalle(@RequestBody DetallePedido nuevoDetalle,
			@PathVariable Integer idPedido) {

		Optional<Pedido> pedido = pedidoService.findPedidoById(idPedido);

		if (pedido.isPresent()) {
			pedidoService.guardarNuevoDetallePedido(pedido, nuevoDetalle);
			return ResponseEntity.ok(nuevoDetalle);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@PutMapping(path = "/{idPedido}")
	@ApiOperation(value = "Modifica un pedido")
	public ResponseEntity<?> actualizar(@RequestBody Pedido pedido, @PathVariable Integer idPedido) throws Exception {

		Optional<Pedido> pedidoDb = pedidoService.findPedidoById(idPedido);
		if (pedidoDb.isPresent()) {
			// solo se puede cancelar pedidos en estados NUEVO; CONFIRMADO O PENDIENTE
			if (pedido.getEstado().equals(EstadoPedido.CANCELADO)
					&& pedidoDb.get().getEstado().equals(EstadoPedido.NUEVO)
					&& pedidoDb.get().getEstado().equals(EstadoPedido.CONFIRMADO)
					&& pedidoDb.get().getEstado().equals(EstadoPedido.PENDIENTE)) {
				return ResponseEntity.badRequest()
						.body("Un pedido en estado " + pedidoDb.get().getEstado() +
														" no puede ser cancelado");
			}
			EstadoPedido estado = pedido.getEstado();
			pedidoService.update(pedidoDb.get(), pedido);
			if (estado.equals(EstadoPedido.CONFIRMADO)) {
				// envio a cola de mensajes
				messageSenderPedidos.enviarMsg(pedido.getDetalle());
			}
			return ResponseEntity.ok(pedido);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(path = "/{idPedido}")
	@ApiOperation(value = "Borra un pedido")
	public ResponseEntity<Pedido> borrar(@PathVariable Integer idPedido) {

		Optional<Pedido> pedidoDb = pedidoService.findPedidoById(idPedido);
		if (pedidoDb.isPresent()) {
			pedidoService.delete(pedidoDb.get());
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping(path = "/{idPedido}/detalle/{idDetalle}")
	@ApiOperation(value = "Borra el detalle de un pedido")
	public ResponseEntity<DetallePedido> borrarDetalle(@PathVariable Integer idPedido,
			@PathVariable Integer idDetalle) {

		try {
			pedidoService.deleteDetallePedido(idPedido, idDetalle);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	@ApiOperation(value = "Busca todos los pedidos")
	public List<Pedido> findAll() {
		return pedidoService.findAll();
	}

	@GetMapping(path = "/{idPedido}")
	@ApiOperation(value = "Busca un pedido por ID")
	public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer idPedido) {

		Optional<Pedido> p = pedidoService.findPedidoById(idPedido);

		if (p == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.of(p);
	}

	@GetMapping(path = "/obra/{idObra}")
	@ApiOperation(value = "Busca un pedido por ID de obra")
	public ResponseEntity<List<Pedido>> pedidoPorIdObra(@PathVariable Integer idObra) {

		// Por ahroa retorna una lista vacía porque no puedo implementar querys
		// personalizadas
		List<Pedido> pedidos = pedidoService.findPedidoByIdObra(idObra);

		return ResponseEntity.ok(pedidos);
	}

	@GetMapping(params = { "cuit", "idCliente" })
	@ApiOperation(value = "Busca pedido por cuit y/o ID del cliente")
	public ResponseEntity<List<Pedido>> obtenerEmpleados(@RequestParam(required = false) Integer cuit,
			@RequestParam(required = false) Integer idCliente) {

		Integer idObra = clienteService.findObraByIdClienteOrCuit(idCliente, cuit);

		if (idObra != null) {
			List<Pedido> pedidos = pedidoService.findPedidoByIdObra(idObra);
			return ResponseEntity.ok(pedidos);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(path = "/{idPedido}/detalle/{idDetalle}")
	@ApiOperation(value = "Busca un detalle de un pedido por ID")
	public ResponseEntity<DetallePedido> detallePorId(@PathVariable Integer idPedido, @PathVariable Integer idDetalle) {

		Optional<Pedido> p = pedidoService.findPedidoById(idPedido);

		if (p.isPresent()) {
			try {
				DetallePedido detalle = pedidoService.findDetallePedidoById(p.get(), idDetalle);
				return ResponseEntity.ok(detalle);
			} catch (Exception e) {
				return ResponseEntity.notFound().build();
			}
		} else {
			return ResponseEntity.notFound().build();
		}

	}
}
