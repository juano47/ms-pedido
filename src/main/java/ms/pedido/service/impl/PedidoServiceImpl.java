package ms.pedido.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.pedido.dao.PedidoRepository;
import ms.pedido.domain.DetallePedido;
import ms.pedido.domain.EstadoPedido;
import ms.pedido.domain.Pedido;
import ms.pedido.service.ClienteService;
import ms.pedido.service.PedidoService;
import ms.pedido.service.ProductoService;

@Service
public class PedidoServiceImpl implements PedidoService {
	
	@Autowired
	PedidoRepository pedidoRepository;
	
	@Autowired
	ProductoService productoService;
	
	@Autowired
	ClienteService clienteService;

	@Override
	public Pedido save(Pedido nuevo) throws Exception {
		boolean pendiente= false;
		Double costoPedido= 0.0;
		for(DetallePedido dp : nuevo.getDetalle()) {
			if(dp.getCantidad()> productoService.getStock(dp.getProducto().getId())) {
				pendiente= true;
			}
			costoPedido += dp.getPrecio();
		}
		if(pendiente) {
			EstadoPedido estado= new EstadoPedido();
			estado.setId(2);
			estado.setEstado("PENDIENTE");
			nuevo.setEstado(estado);
		}
		Integer idCliente= clienteService.findIdClienteByIdObra(nuevo.getObra().getId());
		Double saldoCliente= clienteService.saldo(idCliente);
		
		if((saldoCliente-costoPedido) >= 0.0){
			EstadoPedido estado= new EstadoPedido();
			estado.setId(1);
			estado.setEstado("ACEPTADO");
			nuevo.setEstado(estado);
		}
		else if(clienteService.situacionCrediticia(idCliente)) {
			EstadoPedido estado= new EstadoPedido();
			estado.setId(1);
			estado.setEstado("ACEPTADO");
			nuevo.setEstado(estado);
		}
		else {
			throw new Exception("SU SALDO NO ES SUFICIENTE PARA REALIZAR EL PEDIDO");
		}
		
		pedidoRepository.save(nuevo);
		return nuevo;
	}

	@Override
	public Optional<Pedido> findPedidoById(Integer idPedido) {
		return pedidoRepository.findById(idPedido);
	}

	@Override
	public void updateDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle) {
		pedido.get().getDetalle().add(nuevoDetalle);
		pedidoRepository.save(pedido.get());
		
	}

	@Override
	public void update(Pedido pedido, Pedido pedido2) {
		pedido2.setId(pedido.getId());
		pedidoRepository.save(pedido2);
		
	}

	@Override
	public void delete(Pedido pedido) {
		pedidoRepository.delete(pedido);
		
	}
	
	@Override
	public void deleteDetallePedido(Integer idPedido, Integer idDetalle) throws Exception {
		
		Integer index=-1;
		Optional<Pedido> pedido= pedidoRepository.findById(idPedido);
		
		if(pedido.isPresent()) {
			for(int i=0; i<pedido.get().getDetalle().size(); i++) {
				if(pedido.get().getDetalle().get(i).getId()==idDetalle) {
					index=i;
					break;
				}
			}
			if(index==-1) {
				throw new Exception("NO SE ENCUENTRA EL DETALLE");
			}
			else {
				pedido.get().getDetalle().remove(index);
				pedidoRepository.save(pedido.get());
			}
		}
		else {
			throw new Exception("NO SE ENCUETRA EL PEDIDO");
		}
		
	}

	@Override
	public List<Pedido> findPedidoByIdObra(Integer idObra) {
		List<Pedido> pedidos = new ArrayList<Pedido>();
		
		return pedidos;
	}
	
	

}
