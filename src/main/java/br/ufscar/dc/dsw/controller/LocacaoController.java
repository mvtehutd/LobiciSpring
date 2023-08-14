package br.ufscar.dc.dsw.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.domain.Usuario;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.security.UsuarioDetails;
import br.ufscar.dc.dsw.service.spec.ILocacaoService;
import br.ufscar.dc.dsw.service.spec.ILocadoraService;
import br.ufscar.dc.dsw.service.spec.IUsuarioService;
import br.ufscar.dc.dsw.service.EmailService;
import br.ufscar.dc.dsw.service.spec.IClienteService;

@Controller
@RequestMapping("/locacoes")
public class LocacaoController {
	
	@Autowired
	private ILocacaoService service;
	
	@Autowired
	private IClienteService clienteService;

	@Autowired
	private ILocadoraService locadoraService;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private EmailService emailService;
	
	@GetMapping("/cadastrar")
	public String cadastrar(Locacao locacao, ModelMap model) {
		locacao.setCliente(getCliente());
		model.addAttribute("locadoras", locadoraService.buscarTodos());
		return "locacao/cadastro";
	}
	
	private Cliente getCliente() {
		UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return clienteService.buscarPorEmail(usuarioDetails.getUsuario().getEmail());
	}

	private Locadora getLocadora() {
		UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return locadoraService.buscarPorEmail(usuarioDetails.getUsuario().getEmail());
	}

	private Usuario getUsuario() {
		UsuarioDetails usuarioDetails = (UsuarioDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		return usuarioService.buscarPorEmail(usuarioDetails.getUsuario().getEmail());
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		List<Locacao> locacoes = new ArrayList<>();
		Usuario usuario = getUsuario();

		if(usuario.getRole().equals("ROLE_CLIENTE")){
			locacoes = service.buscarTodosPorCliente(getCliente());
		}
		if(usuario.getRole().equals("ROLE_LOCADORA")){
			locacoes = service.buscarTodosPorLocadora(getLocadora());
		}
					
		model.addAttribute("locacoes", locacoes);
		
		return "locacao/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Locacao locacao, String hora, BindingResult result, RedirectAttributes attr) throws UnsupportedEncodingException {
		
		if (result.hasErrors()) {
			return "locacao/cadastro";
		}
		String data[] = locacao.getData().split("-");
		locacao.setData(data[2] + "/" + data[1] + "/" + data[0] + " " + hora + "H");

		if(service.buscarPorClienteEData(locacao.getCliente(), locacao.getData()) != null){
			attr.addFlashAttribute("fail", "locacao.cliente.indisponivel");
		} else if(service.buscarPorLocadoraEData(locacao.getLocadora(), locacao.getData()) != null){
			attr.addFlashAttribute("fail", "locacao.locadora.indisponivel");
		} else{
			service.salvar(locacao);
			InternetAddress from = new InternetAddress("marcos.vendrame@estudante.ufscar.br", "Lobici Spring");
		    InternetAddress toCliente = new InternetAddress(locacao.getCliente().getEmail(), locacao.getCliente().getName());
			InternetAddress toLocadora = new InternetAddress(locacao.getLocadora().getEmail(), locacao.getLocadora().getName());
					
			String subject = "Locação Realizada";

			String body = "Locação Realizada com Sucesso:\n\n";
			body += "Data: " + locacao.getData() + "\n";
			body += "Cliente: " + locacao.getCliente().getName() + "\n";
			body += "Locadora: " + locacao.getLocadora().getName();

			// Envio sem anexo
			emailService.send(from, toCliente, subject, body);
			emailService.send(from, toLocadora, subject, body);
			
			attr.addFlashAttribute("sucess", "locacao.create.sucess");
		}
		
		return "redirect:/locacoes/listar";
	}
	
	@ModelAttribute("clientes")
	public List<Cliente> listaClientes() {
		return clienteService.buscarTodos();
	}
}