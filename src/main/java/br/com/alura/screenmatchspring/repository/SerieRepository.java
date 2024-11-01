package br.com.alura.screenmatchspring.repository;

import br.com.alura.screenmatchspring.model.Categoria;
import br.com.alura.screenmatchspring.model.Episodio;
import br.com.alura.screenmatchspring.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/*
interface criada para herdar de jparepository, que faz as operações de crud
de parâmetro precisamos passar a entidade que será persistida no banco de dados e o tipo do id nela
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCase(String nomeAtor);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria categoria);

    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(Integer totalTemporada, Double avaliacao);

    //JPQL
    @Query("select s FROM Serie s WHERE s.totalTemporadas <= :totalTemporada AND s.avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEAvaliacao(Integer totalTemporada, Double avaliacao);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("select e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);

    List<Serie> findTop5ByOrderByEpisodiosDataLancamentoDesc();
}
