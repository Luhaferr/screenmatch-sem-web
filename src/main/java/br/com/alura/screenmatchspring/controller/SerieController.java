package br.com.alura.screenmatchspring.controller;

import br.com.alura.screenmatchspring.dto.SerieDTO;
import br.com.alura.screenmatchspring.service.SerieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
}
