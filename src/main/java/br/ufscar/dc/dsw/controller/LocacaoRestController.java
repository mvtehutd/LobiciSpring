package br.ufscar.dc.dsw.controller;

import java.util.List;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.service.spec.IClienteService;
import br.ufscar.dc.dsw.service.spec.ILocacaoService;
import br.ufscar.dc.dsw.service.spec.ILocadoraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LocacaoRestController {

    @Autowired
    private ILocacaoService locacaoService;
    @Autowired
    private ILocadoraService locadoraService;
    @Autowired
    private IClienteService clienteService;

    @GetMapping(path = "/locacoes")
    public ResponseEntity<List<Locacao>> lista() {
        List<Locacao> lista = (List<Locacao>) locacaoService.buscarTodos();
        if (lista.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(lista);
    }

    @GetMapping(path = "/locacoes/{id}")
    public ResponseEntity<Locacao> listaPorId(@PathVariable("id") long id) {
        Locacao locacao = locacaoService.buscarPorId(id);
        if (locacao == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locacao);
    }



    @GetMapping(path = "/locacoes/clientes/{id}")
    public ResponseEntity<List<Locacao>> listaPorCliente(@PathVariable("id") long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        List<Locacao> clientes = locacaoService.buscarTodosPorCliente(cliente);
        if (clientes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(clientes);
    }

    @GetMapping(path = "/locacoes/locadoras/{id}")
    public ResponseEntity<List<Locacao>> listaPorLocadora(@PathVariable("id") long id) {
        Locadora locadora = locadoraService.buscarPorId(id);
        List<Locacao> locacoes = locacaoService.buscarTodosPorLocadora(locadora);
        if (locacoes.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(locacoes);
    }
}
