package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Paciente;
import rodvpx.com.github.apihospitalspring.service.PacienteService;


@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cadastrar")
    public Mono<ResponseEntity<String>> cadastrar(@Valid @RequestBody Paciente paciente, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessages = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessages.append(error.getDefaultMessage()).append("\n"));
            return Mono.just(ResponseEntity.badRequest().body(errorMessages.toString()));
        }

        return pacienteService.cadastrar(paciente)
                .map(savedPaciente -> ResponseEntity.ok("Paciente cadastrado com sucesso"))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar paciente"));
    }


    @GetMapping("/nome")
    public Mono<ResponseEntity<?>> buscarPorNome(@RequestParam String nome) {
        return pacienteService.buscarPorNome(nome)
                .doOnNext(paciente -> System.out.println("Paciente encontrado: " + paciente)) // Log para verificar o paciente encontrado
                .flatMap(paciente -> {
                    if (paciente == null) {
                        return Mono.just(ResponseEntity.notFound().build()); // Retorna 404 caso não encontre o paciente
                    }
                    return Mono.just(ResponseEntity.ok(paciente)); // Retorna 200 com o paciente encontrado
                })
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Garante que o 404 será retornado caso não encontre nada
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
}