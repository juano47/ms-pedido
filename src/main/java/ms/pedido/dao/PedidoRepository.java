package ms.pedido.dao;

import org.springframework.stereotype.Repository;

import frsf.isi.dan.InMemoryRepository;
import ms.pedido.domain.Pedido;

@Repository
public class PedidoRepository extends InMemoryRepository<Pedido>{

	@Override
	public Integer getId(Pedido entity) {
		return entity.getId();
	}

	@Override
	public void setId(Pedido entity, Integer id) {
		entity. setId(id);
	}
	
}
