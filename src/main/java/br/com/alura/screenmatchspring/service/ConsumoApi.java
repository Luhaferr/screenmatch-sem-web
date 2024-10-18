package br.com.alura.screenmatchspring.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {
    //faz uma chamada HTTP para uma API e retorna os dados que foram obtidos no formato de string, nesse caso um JSON.
    public String obterDados(String endereco) {
        //instância da classe responsável por ENVIAR a requisição HTTP para a API
        HttpClient client = HttpClient.newHttpClient();

        /*
        Construção da requisição HTTP
        HttpRequest.newBuilder(): cria um construtor para a requisição HTTP.
        uri(URI.create(endereco)): define o endereço (URI) da API que será consumida. O parâmetro endereco é uma string que contém a URL completa da API que você quer acessar.
        build(): finaliza a construção da requisição.
         */
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endereco))
                .build();

        /*
        recebe a resposta da requisição, está iniciado fora do bloco como variavel global para que possa ser acessado depois.
         */
        HttpResponse<String> response = null;
        try {
            /*
            client.send tem 2 parâmetros, envio da requisição HTTP(request) e a resposta dessa request
            convertida em String (HttpResponse.BodyHandlers.ofString()), nesse caso o corpo da resposta.
             */
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*
         acessando o corpo da resposta HTTP e armazenando-o na variável json.
         O métod0 body() retorna o conteúdo da resposta, que neste caso é uma string
         (pois foi definido no body handler que o corpo seria convertido para String).
         */
        String json = response.body();
        return json;
    }
}
