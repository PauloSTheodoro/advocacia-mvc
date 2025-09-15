package com.unifil.advocacia.gerenciador.cliente.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class PostCliente {
  @NotBlank @Size(max = 150) public String nome;
  @NotBlank @Size(min = 10, max = 11) public String cpf;
  @NotBlank @Size(max = 20) public String telefone;
  @NotBlank @Email @Size(max = 150) public String email;
  @Past public LocalDate dataNascimento;
  @Size(max = 255) public String endereco;
  @Size(max = 1000) public String observacoes;

    public PostCliente() {
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }
}
