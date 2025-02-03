package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.model.Paciente;
import rodvpx.com.github.apihospitalspring.service.PacienteService;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    // Cadastrar paciente
    @PostMapping("/cadastrar")
    public Mono<ResponseEntity<String>> cadastrar(@Valid @RequestBody Paciente paciente) {
        return pacienteService.cadastrar(paciente)
                .map(id -> ResponseEntity.status(201).body(id))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body("Erro ao cadastrar paciente")));
    }

    // buscar por nome
    @GetMapping("/nome")
    public Mono<ResponseEntity<Paciente>> buscarPorNome(@RequestParam String nome) {
        return pacienteService.buscarPorNome(nome)
                .map(ResponseEntity::ok) // Retorna o paciente encontrado
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Retorna 404 caso não encontrado
    }


    // Buscar paciente por CPF
    @GetMapping("/cpf")
    public Mono<ResponseEntity<Paciente>> buscarPorCpf(@RequestParam String cpf) {
        return pacienteService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok) // Retorna o paciente encontrado
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Retorna 404 caso não encontrado
    }

    // Listar todos os pacientes
    @GetMapping("/listar")
    public Mono<ResponseEntity<Flux<Paciente>>> listar() {
        return pacienteService.listar(Paciente.class) // Usa o método genérico listar
                .collectList() // Converte o Flux em uma lista para facilitar a resposta
                .map(pacientes -> ResponseEntity.ok().body(Flux.fromIterable(pacientes))) // Retorna todos os pacientes
                .defaultIfEmpty(ResponseEntity.noContent().build()); // Retorna 204 caso não haja pacientes
    }

    // Atualizar paciente
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> atualizar(@PathVariable String id, @Valid @RequestBody Paciente pacienteAtualizado) {
        return pacienteService.atualizar(id, pacienteAtualizado)
                .map(updated -> updated ? ResponseEntity.ok(true) : ResponseEntity.status(404).body(false));
    }

    // Deletar atendente
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> deletar(@PathVariable String id) {
        return pacienteService.deletar(id)
                .map(deleted -> deleted ? ResponseEntity.ok(true) : ResponseEntity.status(404).body(false));
    }

}