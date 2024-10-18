package br.com.alura.screenmatchspring.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

//classe responsável por converter os dados de json para objeto e vice-versa.
public class ConverteDados implements IConverteDados{
    //converte classes Java em Json e vice-versa.
    private ObjectMapper mapper = new ObjectMapper();

    /*
    métod0 para obter dados de qualquer classe futura, por isso é do tipo Generic, assim qualquer classe futura pode usar esse métod0
    se colocassemos apenas para obter dados de Séries iríamos ter que repetir código para outras classes, como filmes, atores e etc.
     */
    @Override
    public <T> T converteDados(String json, Class<T> classe) {
        try {
            //lê o valor do json e transforma na classe que o usuário indicou.
            return mapper.readValue(json, classe);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
