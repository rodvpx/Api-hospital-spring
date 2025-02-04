package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.model.Consulta;
import rodvpx.com.github.apihospitalspring.service.ConsultaService;

@RestController
@RequestMapping("/consultas")
@Validated
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    // Agendar uma nova consulta
    @PostMapping("/agendar")
    public Mono<ResponseEntity<String>> agendar(@Valid @RequestBody Consulta consulta) {
        return consultaService.cadastrar(consulta)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body("Consulta agendada com sucesso! ID: " + id))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao agendar consulta")));
    }

    // Buscar consulta por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<Consulta>> buscarPorId(@PathVariable String id) {
        return consultaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    // Listar todas as consultas
    @GetMapping("/listar")
    public Flux<ResponseEntity<Consulta>> listar() {
        return consultaService.listar(Consulta.class)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Flux.just(ResponseEntity.noContent().build()));
    }

    // Atualizar uma consulta
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> atualizar(@PathVariable String id, @Valid @RequestBody Consulta consultaAtualizada) {
        return consultaService.atualizar(id, consultaAtualizada)
                .map(updated -> updated ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

    // Cancelar uma consulta
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> cancelar(@PathVariable String id) {
        return consultaService.deletar(id)
                .map(deleted -> deleted ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }
}