package br.ufscar.dc.dsw.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Table(name = "Cliente")
public class Cliente extends Usuario {

	@NotBlank
	@Size(min = 14, max = 14, message = "{Size.cliente.CPF}")
	@Column(nullable = false, unique = true, length = 60)
	private String CPF;

    @NotBlank
    @Column(nullable = false, length = 13)
    private String telefone;

	@NotBlank
    @Column(nullable = false, length = 1)
    private String sexo;

	@NotNull
	@Column(nullable = false, length = 19)
	private String dataNascimento;

    @OneToMany(mappedBy = "cliente")
	private List<Locacao> locacoes;

    public Cliente() {
        this.setRole("ROLE_CLIENTE");
        this.setEnabled(true);
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public List<Locacao> getLocacoes() {
        return locacoes;
    }
	
}
