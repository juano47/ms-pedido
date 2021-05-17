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
		nuevo.setEstado(EstadoPedido.NUEVO);
		return pedidoRepository.save(nuevo);
	}

	@Override
	public Optional<Pedido> findPedidoById(Integer idPedido) {
		return pedidoRepository.findById(idPedido);
	}

	@Override
	public void guardarNuevoDetallePedido(Optional<Pedido> pedido, DetallePedido nuevoDetalle) {
		if(pedido.get().getDetalle()!=null) {
			pedido.get().getDetalle().add(nuevoDetalle);
		}
		else {
			List<DetallePedido> listaDetalle=new ArrayList<>();
			listaDetalle.add(nuevoDetalle);
			pedido.get().setDetalle(listaDetalle);
		}
		
		pedidoRepository.save(pedido.get());
		
	}

	@Override
	public void update(Pedido pedido, Pedido pedidoUpdate) throws Exception {
		pedidoUpdate.setId(pedido.getId());
		
		boolean pendiente= false;
		Double costoPedido= 0.0;
		for(DetallePedido dp : pedidoUpdate.getDetalle()) {
			if(dp.getCantidad()> productoService.getStock(dp.getProducto().getId())) {
				pendiente= true;
			}
			costoPedido += dp.getPrecio();
		}
		if(pendiente) {
			pedidoUpdate.setEstado(EstadoPedido.PENDIENTE);
			pedidoRepository.save(pedidoUpdate);
			
		}else {
			Integer idCliente= clienteService.findIdClienteByIdObra(pedidoUpdate.getObra().getId());
			Double saldoCliente= clienteService.saldo(idCliente);
			
			if((saldoCliente-costoPedido) >= 0.0 || clienteService.situacionCrediticia(idCliente)){
				pedidoUpdate.setEstado(EstadoPedido.ACEPTADO);
			}else {
				throw new Exception("SU SALDO NO ES SUFICIENTE PARA REALIZAR EL PEDIDO");
			}
		}
		
		pedidoRepository.save(pedidoUpdate);
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
	public List<Pedido> findPedidosByObraId(Integer idObra) {
		return pedidoRepository.findByObra_Id(idObra);
	}

	@Override
	public DetallePedido findDetallePedidoById(Pedido p, Integer idDetallePedido) throws Exception {
		
		int index=-1;
		for(int i=0; i<p.getDetalle().size(); i++) {
			if(p.getDetalle().get(i).getId()== idDetallePedido) {
				index=i;
				break;
			}
		}
		
		if(index==-1) {
			throw new Exception("NO SE ENCONTRO EL DETALLE DEL PEDIDO");
		}
		else {
			return p.getDetalle().get(index);
		}
		
	}


	@Override
	public Boolean existenPedidosPendientes(List<Integer> idObrasCliente) {

		for(Integer id : idObrasCliente) {
			for(Pedido pedido : this.pedidoRepository.findByObra_Id(id)) {
				EstadoPedido estadoPedido = pedido.getEstado();
				if(!(estadoPedido == EstadoPedido.CANCELADO || 
						estadoPedido == EstadoPedido.ENTREGADO || estadoPedido == EstadoPedido.RECHAZADO))
					return true;
			}
		}
		return false;
	}
	
	
	
}
