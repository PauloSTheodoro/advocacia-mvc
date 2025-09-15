package com.unifil.advocacia.gerenciador.cliente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.unifil.advocacia.gerenciador.cliente.model.Cliente;
import com.unifil.advocacia.gerenciador.cliente.repository.ClienteRepository;
import com.unifil.advocacia.gerenciador.exception.NotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class ClienteService {
@Autowired
  private ClienteRepository repo;

  public List<Cliente> listarTodos() { return repo.findAll(); }

 
  public Cliente buscarPorId(Long id) {
    return repo.findById(id).orElseThrow(() ->
        new EntityNotFoundException("Cliente não encontrado: id=" + id));
  }

  @Transactional
  public Cliente criar(@Valid Cliente c) {
    normalizar(c);
    validarUnicos(c.getCpf(), c.getEmail(), null);
    return repo.save(c);
  }

  @Transactional
  public Cliente atualizar(Long id, @Valid Cliente novos) {
    Cliente c = buscarPorId(id);
    normalizar(novos);
    validarUnicos(novos.getCpf(), novos.getEmail(), id);

    c.setNome(novos.getNome());
    c.setCpf(novos.getCpf());
    c.setTelefone(novos.getTelefone());
    c.setEmail(novos.getEmail());
    c.setDataNascimento(novos.getDataNascimento());
    c.setEndereco(novos.getEndereco());
    c.setObservacoes(novos.getObservacoes());

    return repo.save(c);
  }

  @Transactional
  public void excluir(Long id) {
    if (!repo.existsById(id)) throw new NotFoundException("Cliente não encontrado: id=" + id);
    repo.deleteById(id);
  }

 
  public List<Cliente> buscarPorNome(String nome) {
    return repo.findByNomeContainingIgnoreCase(nome);
  }

  // helpers
  private void normalizar(Cliente c) {
    if (c.getCpf() != null) c.setCpf(c.getCpf().replaceAll("\\D",""));
    if (c.getEmail() != null) c.setEmail(c.getEmail().trim().toLowerCase());
    if (c.getNome() != null) c.setNome(c.getNome().trim());
    if (c.getTelefone() != null) c.setTelefone(c.getTelefone().trim());
  }

  private void validarUnicos(String cpf, String email, Long idAtual) {
    if (cpf != null) repo.findByCpf(cpf).ifPresent(ex -> {
      if (idAtual == null || !ex.getId().equals(idAtual))
        throw new IllegalArgumentException("CPF já cadastrado");
    });
    if (email != null) repo.findByEmail(email).ifPresent(ex -> {
      if (idAtual == null || !ex.getId().equals(idAtual))
        throw new IllegalArgumentException("E-mail já cadastrado");
    });
  }
}
