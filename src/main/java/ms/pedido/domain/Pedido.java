package ms.pedido.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="PED_PEDIDO")
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
    private Integer id;
	
	@Column(name="fecha_pedido")
    private Instant fechaPedido;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="id", insertable=false, updatable=false)
    private Obra obra;
	
	@OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
	@JoinColumn(name="id", insertable=false, updatable=false)
    private List<DetallePedido> detalle;
    
	@Enumerated
	private EstadoPedido estado;
}
