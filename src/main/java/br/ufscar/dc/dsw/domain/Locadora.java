package br.ufscar.dc.dsw.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Locadora")
public class Locadora extends Usuario {

	public Locadora() {
        this.setRole("ROLE_LOCADORA");
        this.setEnabled(true);
    }
	
	@NotBlank
	@Size(min = 18, max = 18, message = "{Size.locadora.CNPJ}")
	@Column(nullable = false, unique = true, length = 60)
	private String CNPJ;

	@NotBlank(message="{Blank.locadora.cidade}")
    @Column(nullable = false, length = 64)
    private String cidade;

	@OneToMany(mappedBy = "locadora")
	private List<Locacao> locacoes;
	
	public String getCNPJ() {
		return CNPJ;
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}

    public List<Locacao> listarLocacoes() {
        return locacoes;
    }

    public void setLocacoes(List<Locacao> locacoes) {
        this.locacoes = locacoes;
    }

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	
}