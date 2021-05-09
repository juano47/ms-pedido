package ms.pedido.service.impl;

import org.springframework.stereotype.Service;

import ms.pedido.service.ClienteService;

@Service
public class ClienteServiceImpl implements ClienteService{

	@Override
	public Double saldo(Integer idCliente) {
		return 10000.0;
	}

	@Override
	public Integer findIdClienteByIdObra(Integer idObra) {
		return 1;
	}

	@Override
	public boolean situacionCrediticia(Integer idCliente) {
		return true;
	}

	@Override
	public Integer findObraByIdClienteOrCuit(Integer idCliente, Integer cuit) {
		return 1;
	}

}
