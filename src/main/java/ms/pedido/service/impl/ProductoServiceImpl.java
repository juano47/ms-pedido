package ms.pedido.service.impl;

import ms.pedido.service.ProductoService;

public class ProductoServiceImpl implements ProductoService{

	@Override
	public Integer getStock(Integer idProducto) {
		return 10;
	}

}
