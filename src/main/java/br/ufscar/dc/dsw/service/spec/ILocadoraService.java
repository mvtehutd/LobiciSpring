package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Locadora;

public interface ILocadoraService {

	Locadora buscarPorId(Long id);

	Locadora buscarPorEmail(String email);

	List<Locadora> buscarTodos();

	void salvar(Locadora locadora);

	void excluir(Long id);
	
	boolean locadoraTemLocacoes(Long id);
}
