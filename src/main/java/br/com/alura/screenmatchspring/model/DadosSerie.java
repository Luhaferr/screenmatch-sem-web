package br.com.alura.screenmatchspring.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //ignora o que não for encontrado de propriedades/atributos que não foram mapeados
public record DadosSerie(@JsonAlias("Title") String titulo, //jsonalias pega o nome do termo exato na api
                         @JsonAlias("totalSeasons") Integer totalTemporadas,
                         @JsonAlias("imdbRating") String avaliacao) {
}
