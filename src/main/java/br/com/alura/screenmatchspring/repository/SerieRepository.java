package br.com.alura.screenmatchspring.repository;

import br.com.alura.screenmatchspring.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

/*
interface criada para herdar de jparepository, que faz as operações de crud
de parâmetro precisamos passar a entidade que será persistida no banco de dados e o tipo do id nela
 */
public interface SerieRepository extends JpaRepository<Serie, Long> {
}
