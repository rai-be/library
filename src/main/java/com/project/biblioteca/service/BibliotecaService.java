package com.project.biblioteca.service;

import com.project.biblioteca.model.Livro;
import com.project.biblioteca.model.Usuario;
import com.project.biblioteca.repository.LivroRepository;
import com.project.biblioteca.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BibliotecaService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Livro cadastrarLivro(Livro livro) {
        return livroRepository.save(livro);
    }

    public Usuario cadastrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public String realizarEmprestimo(String isbn, Long idUsuario) {
        Optional<Livro> livroOpt = livroRepository.findByIsbn(isbn);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (livroOpt.isPresent() && usuarioOpt.isPresent()) {
            Livro livro = livroOpt.get();
            Usuario usuario = usuarioOpt.get();

            if (!livro.isDisponivel()) {
                return "Livro indisponível para empréstimo.";
            }

            if (usuario.getLivrosEmprestados().size() >= 3) {
                return "Usuário já atingiu o limite de 3 livros emprestados.";
            }

            livro.emprestar();
            usuario.adicionarLivro(livro);

            livroRepository.save(livro);
            usuarioRepository.save(usuario);

            return "Empréstimo realizado com sucesso!";
        }

        return "Livro ou usuário não encontrado.";
    }

    public String realizarDevolucao(String isbn, Long idUsuario) {
        Optional<Livro> livroOpt = livroRepository.findByIsbn(isbn);
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);

        if (livroOpt.isPresent() && usuarioOpt.isPresent()) {
            Livro livro = livroOpt.get();
            Usuario usuario = usuarioOpt.get();

            if (!usuario.getLivrosEmprestados().contains(livro)) {
                return "Usuário não pegou esse livro emprestado.";
            }

            livro.devolver();
            usuario.removerLivro(livro);

            livroRepository.save(livro);
            usuarioRepository.save(usuario);

            return "Devolução realizada com sucesso!";
        }

        return "Livro ou usuário não encontrado.";
    }

    public List<Livro> listarLivrosDisponiveis() {
        return livroRepository.findByDisponivelTrue();
    }

    public Usuario atualizarUsuario(Long id, Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            Usuario usuarioAtualizado = usuarioExistente.get();
            usuarioAtualizado.setNome(usuario.getNome());
            return usuarioRepository.save(usuarioAtualizado);
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    public Livro atualizarLivro(Long id, Livro livro) {
        Optional<Livro> livroExistente = livroRepository.findById(id);
        if (livroExistente.isPresent()) {
            Livro livroAtualizado = livroExistente.get();
            livroAtualizado.setTitulo(livro.getTitulo());
            livroAtualizado.setAutor(livro.getAutor());
            livroAtualizado.setIsbn(livro.getIsbn());
            livroAtualizado.setDisponivel(livro.isDisponivel());
            return livroRepository.save(livroAtualizado);
        } else {
            throw new RuntimeException("Livro não encontrado.");
        }
    }

    public void deletarLivro(Long id) {
        Optional<Livro> livroExistente = livroRepository.findById(id);
        if (livroExistente.isPresent()) {
            livroRepository.deleteById(id);
        } else {
            throw new RuntimeException("Livro não encontrado.");
        }
    }

    public void deletarUsuario(Long id) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findById(id);
        if (usuarioExistente.isPresent()) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    public boolean emprestarLivro(Livro livro, Usuario usuario) {
        String resultado = realizarEmprestimo(livro.getIsbn(), usuario.getId());
        return "Empréstimo realizado com sucesso!".equals(resultado);
    }

    public Usuario buscarUsuario(Long idUsuario) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(idUsuario);
        if (usuarioOpt.isPresent()) {
            return usuarioOpt.get();
        } else {
            throw new RuntimeException("Usuário não encontrado.");
        }
    }

    public Livro buscarLivro(Long idLivro) {
        Optional<Livro> livroOpt = livroRepository.findById(idLivro);
        if (livroOpt.isPresent()) {
            return livroOpt.get();
        } else {
            throw new RuntimeException("Livro não encontrado.");
        }
    }

    public void devolverLivro(Livro livro, Usuario usuario) {
        realizarDevolucao(livro.getIsbn(), usuario.getId());
    }

}
