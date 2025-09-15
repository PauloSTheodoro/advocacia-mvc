package com.unifil.advocacia.gerenciador.cliente.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes"
  )
@Getter 
@Setter
public class Cliente {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank @Size(max = 120)
  private String nome;

  @Size(min = 10, max = 11) // com/sem máscara
  @Column( length = 11, nullable=true, unique=true )
  private String cpf;

  @NotBlank @Size(max = 20)
  private String telefone;

  @NotBlank @Email @Size(max = 120)
  private String email;

  @Past
  private LocalDate dataNascimento;

  @Size(max = 230)
  private String endereco;

  @Size(max = 600)
  private String observacoes;

  private LocalDateTime criadoEm;
  private LocalDateTime atualizadoEm;

  @PrePersist void onCreate(){ criadoEm = LocalDateTime.now(); }
  @PreUpdate  void onUpdate(){ atualizadoEm = LocalDateTime.now(); }


}
