package br.com.alura.screenmatchspring.controller;

import br.com.alura.screenmatchspring.dto.EpisodioDTO;
import br.com.alura.screenmatchspring.dto.SerieDTO;
import br.com.alura.screenmatchspring.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//classe responsável por controlar as requisições HTTP(GET, POST, ETC) à nossa API relacionado à rota /serie
@RestController //anotação para indicar que o controlador REST é essa classe e ela é gerenciada pelo Spring. permite que a classe manipue requisições HTTP
@RequestMapping("/series")
public class SerieController {
    @Autowired
    private SerieService service;

    /*
    1 - mapeia requisições HTTP do tipo GET pro métod0 abaixo. Nesse caso, o caminho definido é /series,
    então qualquer requisição GET para a URL /series será direcionada a esse métod0.
    2 - Quando o Spring Boot detecta uma requisição GET para o caminho /series, ele chama o métod0 obterSeries.
    3 - O valor dentro de @GetMapping (no caso, "/series") define a rota (ou endpoint) que será utilizada para acessar o métod0 associado.
    */
    @GetMapping
    public List<SerieDTO> obterSeries() {
        return service.obterTodasAsSeries();
    }

    @GetMapping("/top5")
    public List<SerieDTO> obterTop5Series() {
        return service.obterTop5Series();
    }

    @GetMapping("/lancamentos")
    public List<SerieDTO> obterlancamentos() {
        return service.obterLancamentos();
    }

    @GetMapping("/{id}") //quando vamos colocar um parâmetro que irá variar na url usamos chaves {} e o nome do param.
    public SerieDTO obterSeriePorId(@PathVariable Long id) {
        return service.obterPorId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id) {
        return service.obterTodasTemporadas(id);
    }

    @GetMapping("/{id}/temporadas/{numero}")
    public List<EpisodioDTO> obterTemporadasPorNumero(@PathVariable Long id, @PathVariable Long numero) {
        return service.obterTemporadasPorNumero(id, numero);
    }
}
