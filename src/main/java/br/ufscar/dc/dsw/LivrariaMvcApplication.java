package br.ufscar.dc.dsw;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.ufscar.dc.dsw.dao.ILocadoraDAO;
import br.ufscar.dc.dsw.dao.IClienteDAO;
import br.ufscar.dc.dsw.dao.ILocacaoDAO;
import br.ufscar.dc.dsw.dao.IUsuarioDAO;
import br.ufscar.dc.dsw.domain.Locadora;
import br.ufscar.dc.dsw.domain.Cliente;
import br.ufscar.dc.dsw.domain.Locacao;
import br.ufscar.dc.dsw.domain.Usuario;

@SpringBootApplication
public class LivrariaMvcApplication {

	public static void main(String[] args) {
		SpringApplication.run(LivrariaMvcApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(IUsuarioDAO usuarioDAO, IClienteDAO clienteDAO, ILocadoraDAO locadoraDAO,
			BCryptPasswordEncoder encoder, ILocacaoDAO locacaoDAO) {
		return (args) -> {

			Usuario u1 = new Usuario();
			u1.setEmail("admin@admin.com");
			u1.setPassword(encoder.encode("admin"));
			u1.setName("Administrador");
			u1.setRole("ROLE_ADMIN");
			u1.setEnabled(true);
			usuarioDAO.save(u1);

			Locadora l1 = new Locadora();
			l1.setCNPJ("12.345.678/0001-99");
			l1.setEmail("leandro@locadora.com");
			l1.setName("Locadora do Leandro");
			l1.setCidade("São Carlos");
			l1.setPassword(encoder.encode("leandro"));
			l1.setRole("ROLE_LOCADORA");
			l1.setEnabled(true);
			locadoraDAO.save(l1);

			Locadora l2 = new Locadora();
			l2.setCNPJ("12.345.678/0001-98");
			l2.setEmail("gabriele@locadora.com");
			l2.setName("Locadora do Gabriele");
			l2.setCidade("São Carlos");
			l2.setPassword(encoder.encode("gabriele"));
			l2.setRole("ROLE_LOCADORA");
			l2.setEnabled(true);
			locadoraDAO.save(l2);

			Cliente c1 = new Cliente();
			c1.setCPF("123.456.789-00");
			c1.setEmail("matheus@gmail.com");
			c1.setName("Matheus");
			c1.setDataNascimento("01/01/1998");
			c1.setPassword(encoder.encode("matheus"));
			c1.setSexo("M");
			c1.setTelefone("(11)955555555");
			c1.setRole("ROLE_CLIENTE");
			c1.setEnabled(true);
			clienteDAO.save(c1);

			Cliente c2 = new Cliente();
			c2.setCPF("987.654.321-00");
			c2.setEmail("marcos@gmail.com");
			c2.setName("Marcos");
			c2.setDataNascimento("25/11/1999");
			c2.setPassword(encoder.encode("marcos"));
			c2.setSexo("M");
			c2.setTelefone("(11)977777777");
			c2.setRole("ROLE_CLIENTE");
			c2.setEnabled(true);
			clienteDAO.save(c2);

			Calendar now = Calendar.getInstance(); // Cria um objeto de data
			now.setTime(Calendar.getInstance().getTime()); // Inicia o objeto com a data original
			now.add(Calendar.HOUR_OF_DAY, 1); // Adiciona as horas necessárias

			Locacao lc1 = new Locacao();
			lc1.setCliente(c1);
			lc1.setLocadora(l1);
			lc1.setData(new SimpleDateFormat("dd/MM/yyyy HH").format(now.getTime()) + "H");
			locacaoDAO.save(lc1);
			
			now.add(Calendar.HOUR_OF_DAY, 1);
			Locacao lc2 = new Locacao();
			lc2.setCliente(c2);
			lc2.setLocadora(l2);
			lc2.setData(new SimpleDateFormat("dd/MM/yyyy HH").format(now.getTime()) + "H");
			locacaoDAO.save(lc2);
		};
	}
}
