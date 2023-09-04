package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Locadora;

@SuppressWarnings("unchecked")
public interface ILocacaoDAO extends CrudRepository<Locacao, Long>{

	Locacao findById(long id);

    Locacao findByClienteAndData(Cliente c, String  data);

    Locacao findByLocadoraAndData(Locadora l, String  data);
	
	Locacao save(Locacao compra);

    List<Locacao> findAllByCliente(Cliente c);

    List<Locacao> findAllByLocadora(Locadora l);

    List<Locacao> findAll();
}