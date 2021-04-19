package ms.pedido.service;

public interface ClienteService {
	
	public Double saldo(Integer idCliente);
	
	public Integer findIdClienteByIdObra(Integer idObra);
	
	public boolean situacionCrediticia(Integer idCliente);
	
	public Integer findObraByIdClienteOrCuit(Integer idCliente,  Integer cuit);
}
