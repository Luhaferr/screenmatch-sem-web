package br.com.alura.screenmatchspring.model;

import br.com.alura.screenmatchspring.service.traducao.ConsultaMyMemory;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity //anotação para indicar que essa classe é uma entidade para o DB e todos atributos serão colunas
@Table(name = "series") //anotação para que o nome da tabela não seja o nome da classe por padrão e sim "series"
public class Serie {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY) //anotação para gerar um id autoincrementado
     private Long id;
     @Column(unique = true) //anotação para dizer que essa coluna não tenha dados repetidos
     private String titulo;
     private Integer totalTemporadas;
     private Double avaliacao;
     @Enumerated(EnumType.STRING) //quando usamos enum temos que especificar se o db vai usar o numero ou a string
     private Categoria genero;
     private String atores;
     private String poster;
     private String sinopse;
     /*
     Série é o lado forte da relação com episódios, cascade faz com que as operações(CRUD) que acontecem na série, também
     ocorram em episódios, "fetch EAGER" é para que sejam feitas imediatamente
      */
     @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
     private List<Episodio> episodios = new ArrayList<>(); //como episodios é uma lista, vai precisar de uma chave estrangeria

     public Serie() {}

     public Serie(DadosSerie dadosSerie) {
          this.titulo = dadosSerie.titulo();
          this.totalTemporadas = dadosSerie.totalTemporadas();
          this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
          this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
          this.atores = dadosSerie.atores();
          this.poster = dadosSerie.poster();
          this.sinopse = ConsultaMyMemory.obterTraducao(dadosSerie.sinopse()).trim();
     }

     public List<Episodio> getEpisodios() {
          return episodios;
     }

     //garante que a lista de episódios da série seja atualizada com a lista recebida como parâmetro, e que cada episódio da lista esteja corretamente associado à série
     public void setEpisodios(List<Episodio> episodios) {
          episodios.forEach(e -> e.setSerie(this));
          this.episodios = episodios;
     }

     public Long getId() {
          return id;
     }

     public void setId(Long id) {
          this.id = id;
     }

     public String getTitulo() {
          return titulo;
     }

     public void setTitulo(String titulo) {
          this.titulo = titulo;
     }

     public Integer getTotalTemporadas() {
          return totalTemporadas;
     }

     public void setTotalTemporadas(Integer totalTemporadas) {
          this.totalTemporadas = totalTemporadas;
     }

     public Double getAvaliacao() {
          return avaliacao;
     }

     public void setAvaliacao(Double avaliacao) {
          this.avaliacao = avaliacao;
     }

     public Categoria getGenero() {
          return genero;
     }

     public void setGenero(Categoria genero) {
          this.genero = genero;
     }

     public String getAtores() {
          return atores;
     }

     public void setAtores(String atores) {
          this.atores = atores;
     }

     public String getPoster() {
          return poster;
     }

     public void setPoster(String poster) {
          this.poster = poster;
     }

     public String getSinopse() {
          return sinopse;
     }

     public void setSinopse(String sinopse) {
          this.sinopse = sinopse;
     }

     @Override
     public String toString() {
          return  "genero = " + genero +
                  ", titulo = " + titulo + '\'' +
                  ", totalTemporadas = " + totalTemporadas +
                  ", avaliacao = " + avaliacao +
                  ", atores = " + atores + '\'' +
                  ", poster = " + poster + '\'' +
                  ", sinopse = " + sinopse + '\'' +
                  ", episódios = " + episodios + '\'';
     }
}
