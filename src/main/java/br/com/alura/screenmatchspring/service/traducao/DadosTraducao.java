package br.com.alura.screenmatchspring.service.traducao;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosTraducao(@JsonAlias(value = "responseData") DadosResposta dadosResposta) {
}
