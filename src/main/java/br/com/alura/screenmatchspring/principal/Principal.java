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

    private void buscarEpisodioPorSerie(){
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome");
        String nomeSerie = scanner.nextLine();

        //busca a série pelo primeiro nome encontrado, transforma tudo em minúsculo para evitar erros
        Optional<Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nomeSerie.toLowerCase()))
                .findFirst();

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

    private void listarSeriesBuscadas() {
        series = repository.findAll(); //como não buscamos mais de lista, puxamos direto do DB
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

}
