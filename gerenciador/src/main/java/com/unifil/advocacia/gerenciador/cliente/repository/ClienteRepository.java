package com.unifil.advocacia.gerenciador.cliente.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unifil.advocacia.gerenciador.cliente.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
  Optional<Cliente> findByCpf(String cpf);
  Optional<Cliente> findByEmail(String email);
  boolean existsByCpf(String cpf);
  boolean existsByEmail(String email);
  List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
