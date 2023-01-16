package com.attornatus.cadastropessoas.controller;

import com.attornatus.cadastropessoas.dto.EnderecoDto;
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
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @GetMapping("/enderecoPrincipal/{pessoaId}")
    public ResponseEntity<Object> buscarEnderecoPrincipal(@PathVariable Long pessoaId) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);

        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }

        List<Endereco> enderecos = pessoaOptional.get().getEnderecos();

        if(enderecos == null || enderecos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereco encontrado para a pessoa");
        }

        Endereco enderecoPrincipal = null;
        for(Endereco end : enderecos) {
            if(end.getIsPrincipal()) {
                enderecoPrincipal = end;
                break;
            }
        }

        if(enderecoPrincipal == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum endereco marcado como principal");
        }

        return ResponseEntity.status(HttpStatus.OK).body(enderecoPrincipal);
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<Object> listar(@PathVariable Long pessoaId) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);

        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }

        List<Endereco> enderecos = pessoaOptional.get().getEnderecos();

        return ResponseEntity.status(HttpStatus.OK).body(enderecos);
    }

    @PostMapping("/{pessoaId}")
    public ResponseEntity<Object> adicionar(@PathVariable Long pessoaId, @RequestBody @Valid EnderecoDto enderecoDto){
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);

        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }
        var pessoa = pessoaOptional.get();

        var endereco = new Endereco();
        BeanUtils.copyProperties(enderecoDto, endereco);
        enderecoRepository.save(endereco);

        if(endereco.getIsPrincipal()) {
            List<Endereco> enderecos = pessoa.getEnderecos();

            //Só um endereco pode ser o principal
            enderecos.stream().forEach( end -> end.setIsPrincipal(false));
        }

        pessoa.getEnderecos().add(endereco);

        pessoaRepository.save(pessoa);

        return ResponseEntity.status(HttpStatus.CREATED).body(endereco);
    }

    @PutMapping("/{pessoaId}/{enderecoId}")
    public ResponseEntity<Object> listar(@PathVariable Long pessoaId, @PathVariable Long enderecoId,  @RequestBody @Valid EnderecoDto enderecoDto) {
        Optional<Pessoa> pessoaOptional = pessoaRepository.findById(pessoaId);

        if (!pessoaOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada");
        }

        var pessoa = pessoaOptional.get();
        Endereco endereco = null;

        for(Endereco end : pessoa.getEnderecos()) {
            if(end.getId() == enderecoId) {
                endereco = end;
                break;
            }
        }

        if (endereco == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado para a pessoa");
        }

        if(enderecoDto.getCep() != null && !enderecoDto.getCep().isBlank()) {
            endereco.setCep(enderecoDto.getCep());
        }

        if(enderecoDto.getCidade() != null && !enderecoDto.getCidade().isBlank()) {
            endereco.setCidade(enderecoDto.getCidade());
        }

        if(enderecoDto.getLogradouro() != null && !enderecoDto.getLogradouro().isBlank()) {
            endereco.setLogradouro(enderecoDto.getLogradouro());
        }

        if(enderecoDto.getNumero() != null && !enderecoDto.getNumero().isBlank()) {
            endereco.setNumero(enderecoDto.getNumero());
        }

        if(enderecoDto.getIsPrincipal() != null) {

            if(enderecoDto.getIsPrincipal()) {
                //Só um endereco pode ser o principal
                pessoa.getEnderecos().stream().forEach( end -> end.setIsPrincipal(false));

            }
            endereco.setIsPrincipal(enderecoDto.getIsPrincipal());
        }

        pessoaRepository.save(pessoa);
        //enderecoRepository.save(endereco);

        return ResponseEntity.status(HttpStatus.OK).body(endereco);
    }
}
