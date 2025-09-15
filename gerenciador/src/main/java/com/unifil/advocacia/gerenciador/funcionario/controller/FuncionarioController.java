package com.unifil.advocacia.gerenciador.funcionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.unifil.advocacia.gerenciador.exception.BadRequestException;
import com.unifil.advocacia.gerenciador.exception.NotFoundException;
import com.unifil.advocacia.gerenciador.funcionario.dto.PostFuncionario;
import com.unifil.advocacia.gerenciador.funcionario.dto.PutFuncionario;
import com.unifil.advocacia.gerenciador.funcionario.model.Funcionario;
import com.unifil.advocacia.gerenciador.funcionario.service.FuncionarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/funcionarios")
@PreAuthorize("hasRole('ADMINISTRADOR')")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    // Listar funcionários
    @GetMapping
    public String listarFuncionarios(Model model, @ModelAttribute("errorMessage") String errorMessage) {
        model.addAttribute("funcionarios", funcionarioService.listarFuncionarios());
        if (errorMessage != null && !errorMessage.isBlank()) {
            model.addAttribute("errorMessage", errorMessage);
        }
        return "funcionarios/listar";
    }

    // Exibir formulário de criação
    @GetMapping("/novo")
    public String novoFuncionarioForm(Model model) {
        if (!model.containsAttribute("funcionario")) {
            model.addAttribute("funcionario", new PostFuncionario("", "", "", null));
        }
        return "funcionarios/form";
    }

    // Criar funcionário
    @PostMapping
    public String salvarFuncionario(@ModelAttribute @Valid PostFuncionario funcionario, Model model) {
        try {
            funcionarioService.salvarFuncionario(funcionario);
            return "redirect:/funcionarios";
        } catch (BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("funcionario", funcionario);
            return "funcionarios/form";
        }
    }

    // Exibir formulário de edição
    @GetMapping("/editar/{id}")
    public String editarFuncionarioForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Funcionario funcionario = funcionarioService.buscarFuncionarioPorId(id);
            model.addAttribute("funcionario", funcionario);
            return "funcionarios/editar";
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/funcionarios";
        }
    }

    // Atualizar funcionário
    @PostMapping("/editar/{id}")
    public String atualizarFuncionario(@PathVariable Long id, @ModelAttribute @Valid PutFuncionario funcionario, Model model) {
        try {
            funcionarioService.atualizarFuncionario(id, funcionario);
            return "redirect:/funcionarios";
        } catch (NotFoundException | BadRequestException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("funcionario", funcionario);
            return "funcionarios/editar";
        }
    }

    // Excluir funcionário
    @GetMapping("/excluir/{id}")
    public String excluirFuncionario(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            funcionarioService.excluirFuncionario(id);
        } catch (NotFoundException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/funcionarios";
    }
}
