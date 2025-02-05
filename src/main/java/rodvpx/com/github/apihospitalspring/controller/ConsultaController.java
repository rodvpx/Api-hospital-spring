package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Consulta;
import rodvpx.com.github.apihospitalspring.service.AtendenteService;
import rodvpx.com.github.apihospitalspring.service.ConsultaService;
import rodvpx.com.github.apihospitalspring.service.MedicoService;
import rodvpx.com.github.apihospitalspring.service.PacienteService;

@RestController
@RequestMapping("/consultas")
@Validated
public class ConsultaController {

    private final ConsultaService consultaService;
    private final PacienteService pacienteService;
    private final MedicoService medicoService;
    private final AtendenteService atendenteService;

    public ConsultaController(ConsultaService consultaService, PacienteService pacienteService, MedicoService medicoService, AtendenteService atendenteService) {
        this.consultaService = consultaService;
        this.pacienteService = pacienteService;
        this.medicoService = medicoService;
        this.atendenteService = atendenteService;
    }


    // Agendar uma nova consulta
    @PostMapping("/agendar")
    public Mono<ResponseEntity<String>> agendar(@Valid @RequestBody Consulta consulta) {
        Mono<Boolean> pacienteExistente = pacienteService.verificarPacienteExistente(consulta.getPacienteId());
        Mono<Boolean> medicoExistente = medicoService.verificarMedicoExistente(consulta.getMedicoId());
        Mono<Boolean> atendenteExistente = atendenteService.verificarAtendenteExistente(consulta.getAtendenteId());

        return Mono.zip(pacienteExistente, medicoExistente, atendenteExistente) // Aguarda as 3 verificações
                .flatMap(result -> {
                    boolean pacienteValido = result.getT1();
                    boolean medicoValido = result.getT2();
                    boolean atendenteValido = result.getT3();

                    if (pacienteValido && medicoValido && atendenteValido) {
                        return consultaService.cadastrar(consulta)
                                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body("Consulta agendada com sucesso! ID: " + id));
                    } else {
                        String erro = "";
                        if (!pacienteValido) erro += "Paciente não encontrado. ";
                        if (!medicoValido) erro += "Médico não encontrado. ";
                        if (!atendenteValido) erro += "Atendente não encontrado.";
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro.trim()));
                    }
                })
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