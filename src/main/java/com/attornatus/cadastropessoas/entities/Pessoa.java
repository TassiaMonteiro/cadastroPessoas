package com.attornatus.cadastropessoas.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import javax.persistence.*;

@Entity
@Getter
@Setter
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private LocalDate dataNascimento;

    @OneToMany
    private List<Endereco> enderecos;

}
