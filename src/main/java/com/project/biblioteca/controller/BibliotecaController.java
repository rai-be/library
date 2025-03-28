package com.project.biblioteca.controller;

import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.service.BibliotecaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/biblioteca")
public class BibliotecaController {

    private final BibliotecaService bibliotecaService;

    public BibliotecaController(BibliotecaService bibliotecaService) {
        this.bibliotecaService = bibliotecaService;
    }

    @PostMapping("/livro")
    public Livro cadastrarLivro(@RequestBody Livro livro) {
        return bibliotecaService.cadastrarLivro(livro);
    }

    @PostMapping("/usuario")
    public Usuario cadastrarUsuario(@RequestBody Usuario usuario) {
        return bibliotecaService.cadastrarUsuario(usuario);
    }

    @GetMapping("/livros")
    public List<Livro> listarLivros() {
        return bibliotecaService.listarLivrosDisponiveis();
    }

    @PutMapping("/livro/{id}")
    public Livro atualizarLivro(@PathVariable Long id, @RequestBody Livro livro) {
        return bibliotecaService.atualizarLivro(id, livro);
    }

    @PutMapping("/usuario/{id}")
    public Usuario atualizarUsuario(@PathVariable Long id, @RequestBody Usuario usuario) {
        return bibliotecaService.atualizarUsuario(id, usuario);
    }

    @DeleteMapping("/livro/{id}")
    public void deletarLivro(@PathVariable Long id) {
        bibliotecaService.deletarLivro(id);
    }

    @DeleteMapping("/usuario/{id}")
    public void deletarUsuario(@PathVariable Long id) {
        bibliotecaService.deletarUsuario(id);
    }

    @PostMapping("/emprestar")
    public String emprestarLivro(@RequestParam Long idUsuario, @RequestParam Long idLivro) {
        Usuario usuario = bibliotecaService.buscarUsuario(idUsuario);
        Livro livro = bibliotecaService.buscarLivro(idLivro);

        boolean emprestado = bibliotecaService.emprestarLivro(livro, usuario);

        if (emprestado) {
            return "Empréstimo realizado com sucesso!";
        } else {
            return "Não foi possível realizar o empréstimo. Verifique se o livro está disponível ou se o usuário já possui 3 livros emprestados.";
        }
    }

    @PostMapping("/devolver")
    public String devolverLivro(@RequestParam Long idUsuario, @RequestParam Long idLivro) {
        Usuario usuario = bibliotecaService.buscarUsuario(idUsuario);
        Livro livro = bibliotecaService.buscarLivro(idLivro);

        bibliotecaService.devolverLivro(livro, usuario);
        return "Livro devolvido com sucesso!";
    }
}
