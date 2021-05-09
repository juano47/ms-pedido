package ms.pedido.service.impl;

import org.springframework.stereotype.Service;

import ms.pedido.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{

	@Override
	public Integer getStock(Integer idProducto) {
		return 10;
	}

}
