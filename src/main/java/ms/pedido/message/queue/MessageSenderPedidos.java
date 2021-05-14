package ms.pedido.message.queue;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;
import ms.pedido.domain.DetallePedido;

@Log4j2
@Service
public class MessageSenderPedidos {
	
	private final static String queue = "queue-pedidos";
	
	@Autowired
	AmqpTemplate amqpTemplate;
	
	@Async
	public void enviarMsg(List<DetallePedido> detallesPedido) {
		try {
			
			List<MessageQueuePedidos> messageList = detallesPedido.stream().map(detalle ->{
				return new MessageQueuePedidos(detalle.getCantidad(), detalle.getProducto().getId());
			}).collect(Collectors.toList());
					
			ObjectMapper mapper = new ObjectMapper();
			String jsonMessage = mapper.writeValueAsString(messageList);
			amqpTemplate.convertAndSend(queue, jsonMessage);
			log.info("Mensaje enviado a cola pedidos");
		}catch(Exception e){
			log.error("Error el enviar mensaje a cola pedidos" + e.getMessage());
			e.printStackTrace();
		}
	}

}
