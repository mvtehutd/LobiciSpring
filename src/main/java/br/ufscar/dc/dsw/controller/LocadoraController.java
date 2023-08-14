package br.ufscar.dc.dsw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

import br.ufscar.dc.dsw.dao.ILocadoraDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.service.spec.ILocadoraService;
import br.ufscar.dc.dsw.domain.Usuario;


@Controller
@RequestMapping("/locadoras")
public class LocadoraController {
	
	@Autowired
	private ILocadoraService service;

	@Autowired
	private IUsuarioDAO usuarioDAO;

	@Autowired
	private ILocadoraDAO locadoraDAO;
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	

	
	@GetMapping("/cadastrar")
	public String cadastrar(Locadora locadora) {
		return "locadora/cadastro";
	}
	
	@GetMapping("/listar")
	public String listar(ModelMap model) {
		model.addAttribute("locadoras",service.buscarTodos());
		return "locadora/lista";
	}
	
	@PostMapping("/salvar")
	public String salvar(@Valid Locadora locadora, BindingResult result, RedirectAttributes attr,  ModelMap model) {
		if (result.hasErrors()) {
			return "locadora/cadastro";
		}
		Usuario user = usuarioDAO.getByEmail(locadora.getEmail());
		if (user != null){
            model.addAttribute("error", "locadora.error.duplicado.label");
            model.addAttribute("message", "locadora.error.email.duplicado.label");
            return "error";
        }
		Locadora locadora1 = locadoraDAO.findByCNPJ(locadora.getCNPJ());
        if (locadora1 != null){
            model.addAttribute("error", "locadora.error.duplicado.label");
            model.addAttribute("message", "locadora.error.cnpj.duplicado.label");
            return "error";
        }
		System.out.println("password = " + locadora.getPassword());
		locadora.setPassword(encoder.encode(locadora.getPassword()));
		service.salvar(locadora);
		attr.addFlashAttribute("sucess", "locadora.create.sucess");
		return "redirect:/locadoras/listar";
	}
	
	@GetMapping("/editar/{id}")
	public String preEditar(@PathVariable("id") Long id, ModelMap model) {
		model.addAttribute("locadora", service.buscarPorId(id));
		return "locadora/cadastro";
	}
	
	@PostMapping("/editar")
	public String editar(@Valid Locadora locadora,String novoPassword, BindingResult result, RedirectAttributes attr, ModelMap model ) {
		
		if (result.hasErrors()) {
			return "locadora/cadastro";
		}
		Usuario user = usuarioDAO.getByEmail(locadora.getEmail());
		service.salvar(locadora);
		if (user != null && user.getId() != locadora.getId()){
            model.addAttribute("error", "locadora.error.duplicado.label");
            model.addAttribute("message", "locadora.error.email.duplicado.label");
            return "error";
        }
		System.out.println("password banco = " + locadora.getPassword());
		System.out.println("nova password = " + novoPassword);

		if (novoPassword != null && !novoPassword.trim().isEmpty()) {
			locadora.setPassword(encoder.encode(novoPassword));
			System.out.println("password editada = " + locadora.getPassword());
		} else {
			System.out.println("Senha não foi editada");
			System.out.println("password não editada= " + locadora.getPassword());
		}
		service.salvar(locadora);
		attr.addFlashAttribute("sucess", "locadora.edit.sucess");
		return "redirect:/locadoras/listar";
	}
	
	@GetMapping("/excluir/{id}")
	public String excluir(@PathVariable("id") Long id, ModelMap model) {
		if (service.locadoraTemLocacoes(id)) {
			model.addAttribute("fail", "locadora.delete.fail");
		} else {
			service.excluir(id);
			model.addAttribute("sucess", "locadora.delete.sucess");
		}
		return listar(model);
	}

	@PostMapping("/filtrar")
	public String filtrar(@RequestParam(name="cidade") String cidade, ModelMap model, RedirectAttributes attr) {
		List<Locadora> locadora = locadoraDAO.getLocadorasByCidade(cidade);
		System.out.println("Cidade " + cidade);
        if(locadora.isEmpty()){
            attr.addFlashAttribute("fail", "locadora.filtrar.error");
            return "redirect:/locadoras/listar";
        }
        else{
			System.out.println("Cidade " + cidade);
            model.addAttribute("locadoras", locadora);
        }
		return "locadora/lista";
	}
}
