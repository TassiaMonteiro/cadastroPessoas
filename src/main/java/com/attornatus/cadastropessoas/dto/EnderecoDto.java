package com.attornatus.cadastropessoas.dto;

import com.sun.istack.NotNull;;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EnderecoDto {

    @NotBlank
    private String logradouro;
    @NotBlank
    private String cep;
    @NotBlank
    private String numero;
    @NotBlank
    private String cidade;
    @NotNull
    private Boolean isPrincipal;

    private PessoaDto pessoa;
}
