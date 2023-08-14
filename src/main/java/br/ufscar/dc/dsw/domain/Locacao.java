package br.ufscar.dc.dsw.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@Entity
@Table(name = "Locacao", uniqueConstraints = {@UniqueConstraint(name = "ClienteData", columnNames ={"data", "cliente_id"}),
@UniqueConstraint(name = "LocadoraData", columnNames ={"data", "locadora_id"})})
public class Locacao extends AbstractEntity<Long> {

	@NotNull
	@Column(nullable = false, length = 19, unique = true)
	private String data;
    
	@NotNull(message = "{NotNull.locacao.cliente}")
	@ManyToOne
	@JoinColumn(name = "cliente_id")
	private Cliente cliente;

	@NotNull(message = "{NotNull.locacao.locadora}")
	@ManyToOne
	@JoinColumn(name = "locadora_id")
	private Locadora locadora;

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Locadora getLocadora() {
        return locadora;
    }

    public void setLocadora(Locadora locadora) {
        this.locadora = locadora;
    }
	
}
