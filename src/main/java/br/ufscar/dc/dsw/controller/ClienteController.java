package br.ufscar.dc.dsw.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.service.spec.ILocadoraService;
import br.ufscar.dc.dsw.service.spec.IClienteService;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private ILocadoraService locadoraService;

	@Autowired
	private BCryptPasswordEncoder encoder;

	@GetMapping("/cadastrar")
	public String cadastrar(Cliente cliente) {
		return "cliente/cadastro";
	}

	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("clientes", clienteService.buscarTodos());
		return "cliente/lista";
	}

	@PostMapping("/salvar")
	public String salvar(@Valid Cliente cliente, BindingResult result, RedirectAttributes attr, ModelMap model) {

		if (result.hasErrors()) {
			return "cliente/cadastro";
		}
		Cliente clienteaux = clienteService.buscarPorEmail(cliente.getEmail());
		if (clienteaux != null){
            model.addAttribute("error", "cliente.error.duplicado.label");
            model.addAttribute("message", "cliente.error.email.duplicado.label");
            return "error";
        }
		clienteaux = clienteService.buscarPorCPF(cliente.getCPF());
        if (clienteaux != null){
            model.addAttribute("error", "cliente.error.duplicado.label");
            model.addAttribute("message", "cliente.error.cpf.duplicado.label");
            return "error";
        }

		cliente.setPassword(encoder.encode(cliente.getPassword()));
		clienteService.salvar(cliente);
		attr.addFlashAttribute("sucess", "cliente.create.sucess");
		return "redirect:/clientes/listar";
	}

	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("cliente", clienteService.buscarPorId(id));
		return "cliente/cadastro";
	}

	@PostMapping("/editar")
	public String editar(@Valid Cliente cliente, String novoPassword, BindingResult result, RedirectAttributes attr, ModelMap model) {

		// Apenas rejeita se o problema não for com o CPF (CPF campo read-only) 
		if (result.hasErrors()) {
			return "cliente/cadastro";
		}

		Cliente clienteaux = clienteService.buscarPorEmail(cliente.getEmail());
		if (clienteaux != null && clienteaux.getId() != cliente.getId()){
            model.addAttribute("error", "cliente.error.duplicado.label");
            model.addAttribute("message", "cliente.error.email.duplicado.label");
            return "error";
        }

		if (novoPassword != null && !novoPassword.trim().isEmpty()) {
			cliente.setPassword(encoder.encode(novoPassword));
		}
		clienteService.salvar(cliente);
		attr.addFlashAttribute("sucess", "cliente.edit.sucess");
		return "redirect:/clientes/listar";
	}

	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		if (clienteService.clienteTemLocacoes(id)) {
			model.addAttribute("fail", "cliente.delete.fail");
		} else {
			clienteService.excluir(id);
			model.addAttribute("sucess", "cliente.delete.sucess");
		}
		return listar(model);
	}

	@ModelAttribute("locadoras")
	public List<Locadora> listaLocadoras() {
		return locadoraService.buscarTodos();
	}
}
