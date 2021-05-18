package ms.pedido.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Getter
@Setter
@Entity
@Table(name="PED_PEDIDO")
public class Pedido {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id_pedido")
    private Integer id;
	
	@Column(name="fecha_pedido")
    private Instant fechaPedido;
	
	@Transient
    private Obra obra;
	
	@Column(name="id_obra")
	private Integer obraId;
	
	@Basic(fetch=FetchType.EAGER)
	@OneToMany(orphanRemoval = true, mappedBy = "pedido", cascade = CascadeType.ALL)
	@JsonManagedReference
    private List<DetallePedido> detalle;
    
	@Enumerated
	private EstadoPedido estado;
}
