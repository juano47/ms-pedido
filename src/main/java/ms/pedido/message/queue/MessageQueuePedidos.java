package ms.pedido.message.queue;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageQueuePedidos implements Serializable {
	private int cantidad;
	private int productoId;
}
