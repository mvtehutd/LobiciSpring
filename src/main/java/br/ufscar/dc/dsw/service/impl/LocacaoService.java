package br.ufscar.dc.dsw.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.ufscar.dc.dsw.dao.ILocacaoDAO;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.service.spec.ILocacaoService;

@Service
@Transactional(readOnly = false)
public class LocacaoService implements ILocacaoService {

	@Autowired
	ILocacaoDAO dao;
	
	public void salvar(Locacao compra) {
		dao.save(compra);
	}

	@Transactional(readOnly = true)
	public Locacao buscarPorId(Long id) {
		return dao.findById(id.longValue());
	}

	@Transactional(readOnly = true)
	public Locacao buscarPorClienteEData(Cliente c, String data) {
		return dao.findByClienteAndData(c, data);
	}

	@Transactional(readOnly = true)
	public Locacao buscarPorLocadoraEData(Locadora l, String data) {
		return dao.findByLocadoraAndData(l, data);
	}

	@Transactional(readOnly = true)
	public List<Locacao> buscarTodosPorCliente(Cliente c) {
		return dao.findAllByCliente(c);
	}

	@Transactional(readOnly = true)
	public List<Locacao> buscarTodosPorLocadora(Locadora l) {
		return dao.findAllByLocadora(l);
	}
}
