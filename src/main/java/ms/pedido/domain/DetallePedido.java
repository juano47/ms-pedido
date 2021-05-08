package ms.pedido.domain;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="detalle_pedido")
public class DetallePedido {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@Column(name="cantidad")
    private Integer cantidad;
	@Column(name="cantidad")
    private Double precio;
    
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id")
    private Producto producto;
}
