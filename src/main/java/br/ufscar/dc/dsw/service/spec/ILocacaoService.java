package br.ufscar.dc.dsw.service.spec;

import java.util.List;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Locadora;

public interface ILocacaoService {

	Locacao buscarPorId(Long id);

	public Locacao buscarPorClienteEData(Cliente c, String data);

	public Locacao buscarPorLocadoraEData(Locadora l, String data);

	List<Locacao> buscarTodosPorCliente(Cliente c);

	List<Locacao> buscarTodosPorLocadora(Locadora l);
	
	void salvar(Locacao locadora);
}
