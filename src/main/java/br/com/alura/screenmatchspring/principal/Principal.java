package br.com.alura.screenmatchspring.principal;

import br.com.alura.screenmatchspring.model.DadosEpisodio;
import br.com.alura.screenmatchspring.model.DadosSerie;
import br.com.alura.screenmatchspring.model.DadosTemporada;
import br.com.alura.screenmatchspring.service.ConsumoApi;
import br.com.alura.screenmatchspring.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ec5e903f";

    public void exibeMenu() {
        System.out.println("Digite o nome da série para busca");
        String nomeSerie = scanner.nextLine();
        String json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        //a classe já é instanciada recebendo os dados json convertidos em objeto para preencher as informações da classe.
        DadosSerie dados = conversor.converteDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i = 1; i <= dados.totalTemporadas(); i++) {
            json = consumoApi.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.converteDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        //escreve as temporadas com um lambda usando method reference
        temporadas.forEach(System.out::println);

//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).listaEpisodios();
//            for (int j = 0; j < episodiosTemporada.size(); j++) {
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        /*
        lambda que faz a mesma coisa que o for acima, sem necessidade de inferir tipo, o compilador já faz automaticamente
        o t é o parâmetro e fica sempre na esquerda da arrow "->", é só um apelido, poderia ser outro nome qualquer ou até mesmo
        temporadas para ficar mais legível ainda.
         */
        temporadas.forEach(t -> t.listaEpisodios().forEach(e -> System.out.println(e.titulo())));

        //passa por várias listas e adiciona a uma outra lista
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.listaEpisodios().stream())
                .collect(Collectors.toList());

        //filtra o top 5 melhores episódios
        System.out.println("\nTop 5 episódios");
        dadosEpisodios.stream()
                //filtra para remover os episodios sem avaliação
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                //ordena decrescente por avaliação
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                //limita aos 5 primeiros elementos da lista
                .limit(5)
                //imprime na tela
                .forEach(System.out::println);
    }
}
