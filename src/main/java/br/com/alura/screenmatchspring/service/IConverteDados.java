package br.com.alura.screenmatchspring.service;

public interface IConverteDados {
    /*
    métod0 que retorna tipo genérico e recebe parâmetro da string em json e o segundo parâmetro é a classe que eu quero
    que o json seja convertido.
     */
    <T> T converteDados(String json, Class<T> classe);
}
