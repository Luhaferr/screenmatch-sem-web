package br.com.alura.screenmatchspring;

import br.com.alura.screenmatchspring.principal.Principal;
import br.com.alura.screenmatchspring.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchspringApplication implements CommandLineRunner {
	@Autowired //implementa a injestão de dependência e delega ao spring a responsabilidade de instanciar uma classe que vamos usar
	/*
	isso só tá aqui pq o spring ja gerencia essa classe, geralmente existe uma outra para fazer melhor.
	fazemos isso pq serierepository é uma interface que herda outra interface e n pode ser instanciada de outra forma noutro lugar
	puxamos ela aqui para que ela faça as operações crud quando for necessário
	 */
	private SerieRepository repository;

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchspringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.exibeMenu();

	}
}
