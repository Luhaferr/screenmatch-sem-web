package br.com.alura.screenmatchspring.repository;

import br.com.alura.screenmatchspring.model.Categoria;
import br.com.alura.screenmatchspring.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

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
}
