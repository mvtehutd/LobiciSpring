package br.ufscar.dc.dsw.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import br.ufscar.dc.dsw.domain.Locadora;

@SuppressWarnings("unchecked")
public interface ILocadoraDAO extends CrudRepository<Locadora, Long>{

	Locadora findById(long id);
	
	Locadora findByCNPJ (String CNPJ);

	Locadora findByEmail (String email);

	List<Locadora> findAll();
	
	Locadora save(Locadora locadora);

	void deleteById(Long id);

	@Query("SELECT l FROM Locadora l WHERE l.cidade = :cidade")
	public List<Locadora> getLocadorasByCidade(@Param("cidade") String cidade);
}
