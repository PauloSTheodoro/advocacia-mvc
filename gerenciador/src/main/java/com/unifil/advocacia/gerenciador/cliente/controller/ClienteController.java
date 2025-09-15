package com.unifil.advocacia.gerenciador.cliente.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.unifil.advocacia.gerenciador.cliente.model.Cliente;
import com.unifil.advocacia.gerenciador.cliente.service.ClienteService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

  private final ClienteService service;
  public ClienteController(ClienteService service) { this.service = service; }

  @GetMapping
  public String listar(Model model, @RequestParam(required = false) String q) {
    model.addAttribute("clientes",
        (q == null || q.isBlank()) ? service.listarTodos() : service.buscarPorNome(q));
    model.addAttribute("q", q);
    return "clientes/lista";
  }

  @GetMapping("/novo")
  public String novo(Model model) {
    model.addAttribute("cliente", new Cliente()); // form-backing bean
    return "clientes/form";
  }

  @PostMapping
  public String salvar(@Valid @ModelAttribute("cliente") Cliente cliente,
                       BindingResult br,
                       Model model) {
    if (br.hasErrors()) return "clientes/form";
    try {
      service.criar(cliente);
      return "redirect:/clientes";
    } catch (IllegalArgumentException e) {
      model.addAttribute("erro", e.getMessage());
      return "clientes/form";
    }
  }

  @GetMapping("/{id}/editar")
  public String editar(@PathVariable Long id, Model model) {
    model.addAttribute("cliente", service.buscarPorId(id));
    return "clientes/form";
  }

  @PostMapping("/{id}")
  public String atualizar(@PathVariable Long id,
                          @Valid @ModelAttribute("cliente") Cliente cliente,
                          BindingResult br,
                          Model model) {
    if (br.hasErrors()) return "clientes/form";
    try {
      service.atualizar(id, cliente);
      return "redirect:/clientes";
    } catch (IllegalArgumentException e) {
      model.addAttribute("erro", e.getMessage());
      return "clientes/form";
    }
  }

  @PostMapping("/{id}/excluir")
  public String excluir(@PathVariable Long id) {
    service.excluir(id);
    return "redirect:/clientes";
  }
}
