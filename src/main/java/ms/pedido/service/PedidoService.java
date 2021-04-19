package ms.pedido.service;

import java.util.List;
import java.util.Optional;

import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.Pedido;

public interface PedidoService {

	Pedido save(Pedido nuevo) throws Exception;

	Optional<Pedido> findPedidoById(Integer idPedido);

	void updateDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle);

	void update(Pedido pedido, Pedido pedido2);

	void delete(Pedido pedido);
	
	void deleteDetallePedido(Integer idPedido, Integer idDetalle) throws Exception;
	
	List<Pedido> findPedidoByIdObra(Integer idObra);
	
	DetallePedido findDetallePedidoById(Pedido p, Integer idDetallePedido) throws Exception;
}
