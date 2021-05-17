package ms.pedido.service;

import java.util.List;
import java.util.Optional;

import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.Pedido;

public interface PedidoService {

	Pedido save(Pedido nuevo) throws Exception;

	Optional<Pedido> findPedidoById(Integer idPedido);

	void guardarNuevoDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle);

	void update(Pedido pedido, Pedido pedido2);

	void delete(Pedido pedido);
	
	void deleteDetallePedido(Integer idPedido, Integer idDetalle) throws Exception;
	
	DetallePedido findDetallePedidoById(Pedido p, Integer idDetallePedido) throws Exception;

	List<Pedido> findPedidosByObraId(Integer idObra);

	Boolean existenPedidosPendientes(List<Integer> idObrasCliente);
}
