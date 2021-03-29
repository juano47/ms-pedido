package ms.pedido.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;

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
import ms.pedido.domain.Pedido;

@RestController
@RequestMapping("api/pedido")
@Api(value="PedidoController", description= "Permite gestionar los pedidos")
public class PedidoController {

	private static final List<Pedido> listaPedidos = new ArrayList<>();
	private static Integer ID_GEN = 1;
	
	@PostMapping
	@ApiOperation(value="Crea un pedido")
	public ResponseEntity<Pedido> crear(@RequestBody Pedido nuevo){
		System.out.println("Pedido: "+nuevo);
		nuevo.setId(ID_GEN++);
		listaPedidos.add(nuevo);
		return ResponseEntity.ok(nuevo);
	}
	
	
	@PostMapping(path = "/{idPedido}/detalle}")
	@ApiOperation(value="Agregar un detale a un pedido")
	public ResponseEntity<DetallePedido> agregarDetalle(@RequestBody DetallePedido nuevoDetalle,
			@PathVariable Integer idPedido){
		
		Optional<Pedido> p= listaPedidos.stream()
				.filter(unPedido -> unPedido.getId().equals(idPedido))
				.findFirst();
		
		p.get().getDetalle().add(nuevoDetalle);
		
		return ResponseEntity.ok(nuevoDetalle);
	}
	
	@PutMapping(path="/{idPedido}")
	@ApiOperation(value="Modifica un pedido")
	public ResponseEntity<Pedido> actualizar(@RequestBody Pedido pedido, 
			@PathVariable Integer idPedido){
		
		OptionalInt indexOpt= IntStream.range(0, listaPedidos.size())
				.filter(i -> listaPedidos.get(i).getId().equals(idPedido))
				.findFirst();
		
		if(indexOpt.isPresent()) {
			listaPedidos.set(indexOpt.getAsInt(), pedido);
			return ResponseEntity.ok(pedido);
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping(path="/{idPedido}")
	@ApiOperation(value="Borra un pedido")
	public ResponseEntity<Pedido> borrar(@PathVariable Integer idPedido){
		
		OptionalInt indexOpt= IntStream.range(0, listaPedidos.size())
				.filter(i -> listaPedidos.get(i).getId().equals(idPedido))
				.findFirst();
		
		if(indexOpt.isPresent()) {
			listaPedidos.remove(indexOpt.getAsInt());
			return ResponseEntity.ok().build();
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping(path="/{idPedido}/detalle/{idDetalle}")
	@ApiOperation(value="Borra el detalle de un pedido")
	public ResponseEntity<DetallePedido> borrarDetalle(@PathVariable Integer idPedido, @PathVariable Integer idDetalle){
		
		OptionalInt indexOpt= IntStream.range(0, listaPedidos.size())
				.filter(i -> listaPedidos.get(i).getId().equals(idPedido))
				.findFirst();
		
		if(indexOpt.isPresent()) {
			OptionalInt indexOptDetalle= IntStream.range(0, listaPedidos.get(indexOpt.getAsInt()).getDetalle().size())
					.filter(i -> listaPedidos.get(indexOpt.getAsInt()).getDetalle().get(i).getId().equals(idDetalle))
					.findFirst();
			
			if(indexOptDetalle.isPresent()) {
				listaPedidos.get(indexOpt.getAsInt()).getDetalle().remove(indexOptDetalle.getAsInt());
				
				return ResponseEntity.ok().build();
			}
			else {
				return ResponseEntity.notFound().build();
			}
		}
		else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping(path="/{idPedido}")
	@ApiOperation(value="Busca un pedido por ID")
	public ResponseEntity<Pedido> pedidoPorId(@PathVariable Integer idPedido){
		
		Optional<Pedido> p= listaPedidos.stream()
				.filter(unPedido -> unPedido.getId().equals(idPedido))
				.findFirst();
		
		return ResponseEntity.of(p);
	}
	
	@GetMapping(path="/obra/{idObra}")
	@ApiOperation(value="Busca un pedido por ID de obra")
	public ResponseEntity<Pedido> pedidoPorIdObra(@PathVariable Integer idObra){
		
		Optional<Pedido> p= listaPedidos.stream()
				.filter(unPedido -> unPedido.getObra().getId().equals(idObra))
				.findFirst();
		
		return ResponseEntity.of(p);
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
