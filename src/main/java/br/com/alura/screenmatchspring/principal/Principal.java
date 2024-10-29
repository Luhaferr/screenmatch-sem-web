package br.com.alura.screenmatchspring.principal;

import br.com.alura.screenmatchspring.model.*;
import br.com.alura.screenmatchspring.repository.SerieRepository;
import br.com.alura.screenmatchspring.service.ConsumoApi;
import br.com.alura.screenmatchspring.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>(); //precisamos declarar aqui para que usemos essa lista globalmente na classe
    private Optional<Serie> serieBusca;

    //construtor criado para que a classe principal usufrua do crud fornecido pelo serierepository qnd precisar
    public Principal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar séries por título
                    5 - Buscar séries por ator
                    6 - Buscar top 5 séries
                    7 - Buscar séries por categoria
                    8 - Buscar séries por máximo de temporada e avaliação
                    9 - Buscar episódio por trecho
                    10 - Buscar top 5 episódios por série
                    11 - Buscar episódios a partir de uma data
                    
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    buscarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        //dadosSeries.add(dados); não é mais necessário salvar em lista, queremos salvar no DB
        repository.save(serie); //salva a série buscada no db
        System.out.println(serie);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.converteDados(json, DadosSerie.class);
        return dados;
    }

    //busca uma série no banco de dados
    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome");
        String nomeSerie = scanner.nextLine();
        //derived queries
        serieBusca = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBusca.isPresent()) {
            System.out.println("Dados da série: " + serieBusca.get());
        } else {
            System.out.println("Série não encontrada!");
        }
    }


    private void buscarSeriePorAtor() {
        System.out.println("Escolha uma série por ator");
        String nomeAtor = scanner.nextLine();

        List<Serie> seriesEncontradas = repository.findByAtoresContainingIgnoreCase(nomeAtor);

        System.out.println("Séries em que " + nomeAtor + " trabalhou:");

        if (seriesEncontradas.isEmpty()) {
            System.out.println("Nenhuma série encontrada com: " + nomeAtor);
        } else {
            seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
        }
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escolha séries por gênero");
        String nomeGenero = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);
        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop = repository.findTop5ByOrderByAvaliacaoDesc();
        seriesTop.forEach(s -> System.out.println(s.getTitulo() + ", avaliação: " + s.getAvaliacao()));
    }

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome");
        String nomeSerie = scanner.nextLine();

        //busca a série pelo nome ignorando maiúsculas
        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        //verifica se o conteiner acima possui um valor
        if (serie.isPresent()) {

            //se houver valor, ele atribui a série encontrada à variável
            Serie serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>(); //lista com numero temporada e sua lista de episodios

            //pra cada série encontrada, preenche os atributos da série
            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                //faz a requisição à API
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                //converte o json vindo da API em classe
                DadosTemporada dadosTemporada = conversor.converteDados(json, DadosTemporada.class);
                //popula a lista com os dados agr em objeto
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            //salva as informações de cada episódio da temporada buscada
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.listaEpisodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);

        } else {
            System.out.println("Série não encontrada!");
        }
    }

    private void buscarEpisodioPorTrecho() {
        System.out.println("Qual o nome do episódio para busca?");
        String trechoEpisodio = scanner.nextLine();
        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                e.getSerie().getTitulo(), e.getTemporada(),
                e.getNumEpisodio(), e.getTitulo()));
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll(); //como não buscamos mais de lista, puxamos direto do DB
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    public void topEpisodiosPorSerie() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(e -> System.out.printf("Série: %s Temporada %s - Episódio %s - %s - Avaliação %s\n",
                    e.getSerie().getTitulo(), e.getTemporada(),
                    e.getNumEpisodio(), e.getTitulo(), e.getAvaliacao()));
        }
    }

    public void buscarEpisodiosDepoisDaData() {
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()) {
            Serie serie = serieBusca.get();
            System.out.println("Digite o ano limite de lançamento");
            int anoLancamento = scanner.nextInt();
            scanner.nextLine();

            List<Episodio> episodiosAno = repository.episodiosPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }

    public void buscarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Digite quantas temporadas a série deve ter no máximo");
        Integer maxTemporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Digite a avaliação mínima que a série deve ter");
        Double minAvaliacao = scanner.nextDouble();
        scanner.nextLine();
        List<Serie> serieBuscada = repository.seriesPorTemporadaEAvaliacao(maxTemporadas, minAvaliacao);

        System.out.println("*** Séries filtradas ***");
        serieBuscada.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
    }

}
