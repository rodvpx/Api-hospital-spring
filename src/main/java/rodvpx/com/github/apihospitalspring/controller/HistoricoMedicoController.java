package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.HistoricoMedico;
import rodvpx.com.github.apihospitalspring.service.ConsultaService;
import rodvpx.com.github.apihospitalspring.service.HistoricoMedicoService;

@RestController
@RequestMapping("/historicoMedico")
@Validated
public class HistoricoMedicoController {

    private final HistoricoMedicoService historicoMedicoService;
    private final ConsultaService consultaService;

    public HistoricoMedicoController(HistoricoMedicoService historicoMedicoService, ConsultaService consultaService) {
        this.historicoMedicoService = historicoMedicoService;
        this.consultaService = consultaService;
    }

    // Cadastrar um novo histórico médico
    @PostMapping("/cadastrar")
    public Mono<ResponseEntity<String>> cadastrar(@Valid @RequestBody HistoricoMedico historicoMedico) {
        return consultaService.buscarPorId(historicoMedico.getConsultaId()) // Verifica se a consulta existe
                .flatMap(consulta ->
                        historicoMedicoService.cadastrar(historicoMedico) // Se a consulta existir, cadastra o histórico médico
                                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body("Histórico médico cadastrado com sucesso! ID: " + id))
                )
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta não encontrada!"))) // Retorna 404 caso não exista a consulta
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar histórico médico")));
    }


    // Buscar histórico médico por ID
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HistoricoMedico>> buscarPorId(@PathVariable String id) {
        return historicoMedicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Listar todos os históricos médicos
    @GetMapping("/listar")
    public Flux<ResponseEntity<HistoricoMedico>> listar() {
        return historicoMedicoService.listar(HistoricoMedico.class)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Flux.just(ResponseEntity.noContent().build()));
    }

    // Atualizar um histórico médico
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> atualizar(@PathVariable String id, @Valid @RequestBody HistoricoMedico historicoAtualizado) {
        return historicoMedicoService.atualizar(id, historicoAtualizado)
                .map(updated -> updated ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }

    // Deletar um histórico médico
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> deletar(@PathVariable String id) {
        return historicoMedicoService.deletar(id)
                .map(deleted -> deleted ? ResponseEntity.ok(true) : ResponseEntity.status(HttpStatus.NOT_FOUND).body(false));
    }
}
