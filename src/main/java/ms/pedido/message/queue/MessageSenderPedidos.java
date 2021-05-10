package ms.pedido.message.queue;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ms.pedido.domain.DetallePedido;

@Service
public class MessageSenderPedidos {
	
	private final static String queue = "queue-pedidos";
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	public void enviarMsg(DetallePedido detallePedido) {
		amqpTemplate.convertAndSend(queue, detallePedido);
	}

}
