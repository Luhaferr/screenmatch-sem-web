package br.com.alura.screenmatchspring.principal;

import br.com.alura.screenmatchspring.model.DadosEpisodio;
import br.com.alura.screenmatchspring.model.DadosSerie;
import br.com.alura.screenmatchspring.model.DadosTemporada;
import br.com.alura.screenmatchspring.model.Episodio;
import br.com.alura.screenmatchspring.service.ConsumoApi;
import br.com.alura.screenmatchspring.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

//        //filtra o top 5 melhores episódios
//        System.out.println("\nTop 10 episódios");
//        dadosEpisodios.stream()
//                //filtra para remover os episodios sem avaliação
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                //ajuda a debugar
//                .peek(e -> System.out.println("primeiro filtro(N/A) " + e))
//                //ordena decrescente por avaliação
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                //limita aos 5 primeiros elementos da lista
//                .limit(10)
//                .peek(e -> System.out.println("Ordenação " + e))
//                //transforma os titulos para caixa alta
//                .map(e -> e.titulo().toUpperCase())
//                //imprime na tela
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                //"achata" as listas em uma lista só, itera por todas listas e joga os elementos em outra lista.
                .flatMap(t -> t.listaEpisodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite um trecho do titulo do episódio");
//        String trechoTitulo = scanner.nextLine();

//        Optional<Episodio> episodioBuscado = episodios.stream()
//                //filtra para buscar episódio caso tenha o trecho do titulo. uppercase para evitar erros de maiúscula
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episódio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episódio não encontrado!");
//        }

//        System.out.println("Quer buscar episódios a partir de qual data?");
//        int ano = scanner.nextInt();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream()
//                .filter(e -> e.getDataLancamento() != null && e.getDataLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data Lançamento: " + e.getDataLancamento().format(formatador)
//                ));

        //faz a média de avaliações por temporada
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média: " + est.getAverage());
        System.out.println("Melhor episódio: " + est.getMax());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());

    }
}
