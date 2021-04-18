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
import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.EstadoPedido;
import ms.pedido.domain.Pedido;
import ms.pedido.service.PedidoService;

@RestController
@RequestMapping("api/pedido")
@Api(value="PedidoController", description= "Permite gestionar los pedidos")
public class PedidoController {


	@Autowired
	PedidoService pedidoService;
	
	
	@PostMapping
	@ApiOperation(value="Crea un pedido")
	public ResponseEntity<String> crear(@RequestBody Pedido nuevo){
		if(nuevo.getObra()!=null) {
			System.out.println("Pedido: "+nuevo);
			try {
				nuevo = pedidoService.save(nuevo);
			}
			catch(Exception e) {
				return ResponseEntity.status(200).body(e.getMessage());
			}
			return ResponseEntity.ok("EL PEDIDO SE HA CREAD CON EXITO");
		}
		else {
			return ResponseEntity.badRequest().body("EL PEDIDO DEBE CONTENER DATOS DE LA OBRA");
		}
		
	}
	
	
	@PostMapping(path = "/{idPedido}/detalle}")
	@ApiOperation(value="Agregar un detalle a un pedido")
	public ResponseEntity<DetallePedido> agregarDetalle(@RequestBody DetallePedido nuevoDetalle,
			@PathVariable Integer idPedido){
		
		Optional<Pedido> pedido = pedidoService.findPedidoById(idPedido);
		
		if(pedido.isPresent()) {
			pedidoService.updateDetallePedido(pedido, nuevoDetalle);
			return ResponseEntity.ok(nuevoDetalle);
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping(path="/{idPedido}")
	@ApiOperation(value="Modifica un pedido")
	public ResponseEntity<Pedido> actualizar(@RequestBody Pedido pedido, 
			@PathVariable Integer idPedido){
		
		Optional<Pedido> pedidoDb = pedidoService.findPedidoById(idPedido);
        if(pedidoDb.isPresent()){
            pedidoService.update(pedidoDb.get(), pedido);
            return ResponseEntity.ok(pedido);
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping(path="/{idPedido}")
	@ApiOperation(value="Borra un pedido")
	public ResponseEntity<Pedido> borrar(@PathVariable Integer idPedido){
		
		Optional<Pedido> pedidoDb = pedidoService.findPedidoById(idPedido);
        if(pedidoDb.isPresent()){
        	pedidoService.delete(pedidoDb.get());
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
	}
	
	@DeleteMapping(path="/{idPedido}/detalle/{idDetalle}")
	@ApiOperation(value="Borra el detalle de un pedido")
	public ResponseEntity<DetallePedido> borrarDetalle(@PathVariable Integer idPedido, @PathVariable Integer idDetalle){
		
		try {
			pedidoService.deleteDetallePedido(idPedido, idDetalle);
		}
		catch(Exception e) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping(path="/{idPedido}")
	@ApiOperation(value="Busca un pedido por ID")
	public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer idPedido){
		
		Optional<Pedido> p= pedidoService.findPedidoById(idPedido);
		
		if(p==null) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.of(p);
	}
	
	@GetMapping(path="/obra/{idObra}")
	@ApiOperation(value="Busca un pedido por ID de obra")
	public ResponseEntity<List<Pedido>> pedidoPorIdObra(@PathVariable Integer idObra){
		
		//Por ahroa retorna una lista vacía porque no puedo implementar querys personalizadas
		List<Pedido> pedidos= pedidoService.findPedidoByIdObra(idObra);
		
		return ResponseEntity.ok(pedidos);
	}
	
	@GetMapping(params = {"cuit", "idCliente"})
	@ApiOperation(value="Busca pedido por cuit y/o ID del cliente")
	public ResponseEntity<Pedido> obtenerEmpleados(@RequestParam (required = false) Integer cuit, 
			@RequestParam (required=false) Integer idCliente){
		
		//no se como llegar del pedido al cliente
		
		return null;
	}
	
	@GetMapping(path="/{idPedido}/detalle/{idDetalle}")
	@ApiOperation(value="Busca un detalle de un pedido por ID")
	public ResponseEntity<DetallePedido> detallePorId(@PathVariable Integer idPedido, @PathVariable Integer idDetalle){
		
		OptionalInt indexOpt= IntStream.range(0, listaPedidos.size())
				.filter(i -> listaPedidos.get(i).getId().equals(idPedido))
				.findFirst();
		
		if(indexOpt.isPresent()) {
			
			Optional<DetallePedido> dp= listaPedidos.get(indexOpt.getAsInt()).getDetalle().stream()
					.filter(unDetalle -> unDetalle.getId().equals(idDetalle))
					.findFirst();
			
			return ResponseEntity.of(dp);
			
		}
		else {
			return ResponseEntity.notFound().build();
		}
		
	}
}
