package ms.pedido.service;

import java.util.Optional;

import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.Pedido;

public interface PedidoService {

	Pedido save(Pedido nuevo);

	Optional<Pedido> findPedidoById(Integer idPedido);

	void updateDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle);

	void update(Pedido pedido, Pedido pedido2);

	void delete(Integer idPedido);

}
