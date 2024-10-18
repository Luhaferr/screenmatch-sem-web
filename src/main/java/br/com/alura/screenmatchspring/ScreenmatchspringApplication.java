package br.com.alura.screenmatchspring;

import br.com.alura.screenmatchspring.model.DadosEpisodio;
import br.com.alura.screenmatchspring.model.DadosSerie;
import br.com.alura.screenmatchspring.model.DadosTemporada;
import br.com.alura.screenmatchspring.service.ConsumoApi;
import br.com.alura.screenmatchspring.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchspringApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchspringApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		String json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&apikey=ec5e903f");
		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		//a classe já é instanciada recebendo os dados json convertidos em objeto para preencher as informações da classe.
		DadosSerie dados = conversor.converteDados(json, DadosSerie.class);
		System.out.println(dados);

		json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=1&episode=2&apikey=ec5e903f");
		DadosEpisodio dadosEpisodio = conversor.converteDados(json, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();
		for (int i = 1; i < dados.totalTemporadas(); i++) {
			json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&apikey=ec5e903f");
			DadosTemporada dadosTemporada = conversor.converteDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		//escreve as temporadas
		temporadas.forEach(System.out::println);
	}
}
