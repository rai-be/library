package com.project.biblioteca.repository;

import com.project.biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {
    List<Livro> findByDisponivelTrue();

    Optional<Livro> findByIsbn(String isbn);
}

