package br.com.alura.screenmatchspring.service;

import br.com.alura.screenmatchspring.dto.EpisodioDTO;
import br.com.alura.screenmatchspring.dto.SerieDTO;
import br.com.alura.screenmatchspring.model.Categoria;
import br.com.alura.screenmatchspring.model.Episodio;
import br.com.alura.screenmatchspring.model.Serie;
import br.com.alura.screenmatchspring.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service //indica ao Spring que é uma classe gerenciada por ele
//essa classe vai ter a lógica de negócios
public class SerieService {
    @Autowired
    private SerieRepository repository; //possibilita o acesso às operações CRUD no DB

    //obtém as séries no banco de dados e transforma essa lista Serie e uma lista SerieDTO(classe record que vai mostrar os dados ao usuário web)
    private List<SerieDTO> converteDados(List<Serie> series) {
        return series.stream()
                .map(serie -> new SerieDTO(serie.getId(), serie.getTitulo(), serie.getTotalTemporadas(), serie.getAvaliacao(), serie.getGenero(), serie.getAtores(), serie.getPoster(), serie.getSinopse()))
                .collect(Collectors.toList());
    }

    private List<EpisodioDTO> converteEpisodios(List<Episodio> episodios) {
        return episodios.stream()
                .map(e -> new EpisodioDTO(e.getTemporada(), e.getNumeroEpisodio(), e.getTitulo()))
                .collect(Collectors.toList());
    }

    public List<SerieDTO> obterTodasAsSeries() {
        return converteDados(repository.findAll());
    }

    public List<SerieDTO> obterTop5Series() {
        return converteDados(repository.findTop5ByOrderByAvaliacaoDesc());
    }

    public List<SerieDTO> obterLancamentos() {
        return converteDados(repository.lancamentosMaisRecentes());
    }

    public SerieDTO obterPorId(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            return new SerieDTO(serie.getId(), serie.getTitulo(), serie.getTotalTemporadas(), serie.getAvaliacao(), serie.getGenero(), serie.getAtores(), serie.getPoster(), serie.getSinopse());
        }
        return null;
    }

    public List<EpisodioDTO> obterTodasTemporadas(Long id) {
        Optional<Serie> serieBuscada = repository.findById(id);

        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            return converteEpisodios(serie.getEpisodios());
        }
        return null;
    }

    public List<EpisodioDTO> obterTemporadasPorNumero(Long id, Long numero) {
        return converteEpisodios(repository.obterEpisodiosPorTemporada(id, numero));
    }

    public List<SerieDTO> obterSeriesPorCategoria(String categoria) {
        Categoria categoriaPt = Categoria.fromPortugues(categoria);
        return converteDados(repository.findByGenero(categoriaPt));
    }

    public List<EpisodioDTO> obterTop5Episodios(Long id) {
        return converteEpisodios(repository.topEpisodiosPorSerie(id));
    }

}
