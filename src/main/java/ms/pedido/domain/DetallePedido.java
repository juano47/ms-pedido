package ms.pedido.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetallePedido {
    private Integer id;
    private Producto producto;
    private Integer cantidad;
    private Double precio;
}
