package br.com.carlos.Owl.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entidade que representa um livro.
 * <p>
 * Contém informações como ID, título, autor, ISBN, editora, ano de publicação, categoria e disponibilidade.
 * </p>
 */
@Entity
@Table(name = "livros")
@Getter
@Setter
@NoArgsConstructor
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String autor;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String editora;

    @Column(nullable = false)
    private Integer anoPublicacao;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private boolean disponivel = true;
}