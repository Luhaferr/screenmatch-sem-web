package br.com.alura.screenmatchspring;

import br.com.alura.screenmatchspring.model.DadosSerie;
import br.com.alura.screenmatchspring.service.ConsumoApi;
import br.com.alura.screenmatchspring.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchspringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchspringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=ec5e903f");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.converteDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
