package rodvpx.com.github.apihospitalspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Medico;
import rodvpx.com.github.apihospitalspring.service.MedicoService;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    // Cadastrar médico
    @PostMapping("/cadastrar")
    public Mono<ResponseEntity<String>> cadastrar(@RequestBody Medico medico) {
        return medicoService.cadastrarMedico(medico);  // Chama o método com a verificação de CRM
    }


    // Buscar médico por CRM
    @GetMapping("/crm")
    public Mono<ResponseEntity<Medico>> buscarPorCrm(@RequestParam String crm) {
        return medicoService.buscarPorCrm(crm)
                .map(ResponseEntity::ok) // Retorna o médico encontrado
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Retorna 404 caso não encontrado
    }


    // Listar todos os médicos
    @GetMapping("/listar")
    public Mono<ResponseEntity<?>> listar() {
        return medicoService.listar(Medico.class)
                .collectList() // Converte Flux para uma lista
                .map(medicos -> medicos.isEmpty()
                        ? ResponseEntity.noContent().build() // Se não houver médicos
                        : ResponseEntity.ok(medicos)); // Retorna a lista de médicos
    }

    // Atualizar médico
    @PutMapping("/atualizar/{id}")
    public Mono<ResponseEntity<String>> atualizar(@PathVariable String id, @RequestBody Medico medico) {
        return medicoService.atualizar(id, medico)
                .flatMap(isUpdated -> {
                    if (isUpdated) {
                        return Mono.just(ResponseEntity.ok("Médico atualizado com sucesso!"));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico não encontrado"));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao atualizar médico")));
    }

    // Deletar médico
    @DeleteMapping("/deletar/{id}")
    public Mono<ResponseEntity<String>> deletar(@PathVariable String id) {
        return medicoService.deletar(id)
                .flatMap(isDeleted -> {
                    if (isDeleted) {
                        return Mono.just(ResponseEntity.ok("Médico deletado com sucesso!"));
                    }
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Médico não encontrado"));
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Erro ao deletar médico")));
    }
}
