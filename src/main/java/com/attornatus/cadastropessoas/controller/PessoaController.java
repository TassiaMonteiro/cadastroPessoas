package com.attornatus.cadastropessoas.controller;

import com.attornatus.cadastropessoas.dto.PessoaDto;
import com.attornatus.cadastropessoas.entities.Endereco;
import com.attornatus.cadastropessoas.entities.Pessoa;
import com.attornatus.cadastropessoas.repositoy.EnderecoRepository;
import com.attornatus.cadastropessoas.repositoy.PessoaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @GetMapping
    public List<Pessoa> listarPessoas() {
        List<Pessoa> pessoas = pessoaRepository.findAll();
        return pessoas;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPessoa(@PathVariable Long id) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }

        return ResponseEntity.status(HttpStatus.OK).body(pessoaOptional.get());
    }


    @PostMapping
    public ResponseEntity<Object> adicionarPessoa(@RequestBody @Valid PessoaDto pessoaDto){
        var pessoa = new Pessoa();
        BeanUtils.copyProperties(pessoaDto, pessoa);

        var endereco = new Endereco();
        BeanUtils.copyProperties(pessoaDto.getEndereco(), endereco);

        endereco = enderecoRepository.save(endereco);
        pessoa.setEnderecos(new ArrayList<Endereco>());
        pessoa.getEnderecos().add(endereco);

        return ResponseEntity.status(HttpStatus.CREATED).body(pessoaRepository.save(pessoa));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editarPessoa(@PathVariable Long id, @RequestBody @Valid PessoaDto pessoaDto){
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(id);
        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        var pessoa = pessoaOptional.get();

        if(pessoaDto.getNome() != null && !pessoaDto.getNome().isBlank()) {
            pessoa.setNome(pessoaDto.getNome());
        }

        if(pessoaDto.getDataNascimento() != null) {
            pessoa.setDataNascimento(pessoaDto.getDataNascimento());
        }

        return ResponseEntity.status(HttpStatus.OK).body(pessoaRepository.save(pessoa));
    }

}
