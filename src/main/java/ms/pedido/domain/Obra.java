package ms.pedido.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="PED_OBRA")
//Para evitar sobreescribir tabla principal, la tabla se crea en ms_ped y no contendrá datos
public class Obra {
	
	@Id //No indico la strategy, es solo lectura
    private Integer id;
    private String descripcion;
}
