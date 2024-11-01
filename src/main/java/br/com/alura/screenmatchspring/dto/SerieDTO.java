package br.com.alura.screenmatchspring.dto;

import br.com.alura.screenmatchspring.model.Categoria;

//classe que será mostrada no front-end, ela possui apenas os dados que queremos mostrar, não precisa de regras de negócios.
public record SerieDTO(Long id,
                       String titulo,
                       Integer totalTemporadas,
                       Double avaliacao,
                       Categoria genero,
                       String atores,
                       String poster,
                       String sinopse) {
}
