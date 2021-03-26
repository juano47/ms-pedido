package ms.pedido.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Producto {
    private Integer id;
    private String descripcion;
    private Double precio;
}
