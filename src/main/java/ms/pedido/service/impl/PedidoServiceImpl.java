package ms.pedido.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.pedido.dao.PedidoRepository;
import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.Pedido;
import ms.pedido.service.PedidoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepository;

	@Override
	public Pedido save(Pedido nuevo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<Pedido> findPedidoById(Integer idPedido) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Pedido pedido, Pedido pedido2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Integer idPedido) {
		// TODO Auto-generated method stub
		
	}

}
