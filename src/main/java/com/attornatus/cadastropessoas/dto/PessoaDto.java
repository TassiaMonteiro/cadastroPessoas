package com.attornatus.cadastropessoas.dto;

import com.attornatus.cadastropessoas.entities.Endereco;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

import java.time.LocalDate;

@Getter
@Setter
public class PessoaDto {

    @NotBlank
    private String nome;
    @NotNull
    private LocalDate dataNascimento;
    private EnderecoDto endereco;
}
