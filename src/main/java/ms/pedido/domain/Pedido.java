package ms.pedido.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
public class Pedido {

    private Integer id;
    private Instant fechaPedido;
    private Obra obra;
    private List<DetallePedido> detalle;
    private EstadoPedido estado;
}
