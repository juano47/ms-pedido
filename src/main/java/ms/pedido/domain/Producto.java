package ms.pedido.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PED_MATERIAL")
//Para evitar sobreescribir tabla principal, la tabla se crea en ms_ped y no contendrá datos
public class Producto {
	
	@Id //No indico la strategy, es solo lectura
    private Integer id;
	@Column(name="descripcion")
    private String descripcion;
	@Column(name="precio")
    private Double precio;
}
