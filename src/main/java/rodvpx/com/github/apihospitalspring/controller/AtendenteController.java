package rodvpx.com.github.apihospitalspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.service.AtendenteService;

@RestController
@RequestMapping("/atendentes")
public class AtendenteController {

    private final AtendenteService atendenteService;

    public AtendenteController(AtendenteService atendenteService) {
        this.atendenteService = atendenteService;
    }

    // Endpoint para cadastrar um novo atendente
    @PostMapping("/cadastrar")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseEntity<String>> cadastrar(@RequestBody Atendente atendente) {
        return atendenteService.cadastrar(atendente)
                .map(id -> ResponseEntity.status(HttpStatus.CREATED).body(id))
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar atendente")));
    }

    // Endpoint para buscar um atendente por email
    @GetMapping("/buscar-por-email")
    public Mono<ResponseEntity<Atendente>> buscarPorEmail(@RequestParam String email) {
        return atendenteService.buscarPorEmail(email)
                .map(atendente -> ResponseEntity.ok(atendente))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Endpoint para buscar um atendente por login
    @GetMapping("/buscar-por-login")
    public Mono<ResponseEntity<Atendente>> buscarPorLogin(@RequestParam String login) {
        return atendenteService.buscarPorLogin(login)
                .map(atendente -> ResponseEntity.ok(atendente))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Endpoint para atualizar um atendente
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> atualizar(@PathVariable String id, @RequestBody Atendente atendenteAtualizado) {
        return atendenteService.atualizar(id, atendenteAtualizado)
                .flatMap(updated -> {
                    if (updated) {
                        return Mono.just(ResponseEntity.ok(Boolean.TRUE)); // Retorna ResponseEntity com status 200 e corpo Boolean.TRUE
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE)); // Retorna ResponseEntity com 404 e corpo Boolean.FALSE
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE))); // Se o Mono estiver vazio, retorna 404 e corpo Boolean.FALSE
    }


    // Endpoint para deletar um atendente
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> deletar(@PathVariable String id) {
        return atendenteService.deletar(id)
                .flatMap(deleted -> {
                    if (deleted) {
                        return Mono.just(ResponseEntity.ok(Boolean.TRUE)); // Retorna 200 OK com Boolean.TRUE se deletado com sucesso
                    } else {
                        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE)); // Retorna 404 NOT FOUND com Boolean.FALSE se não encontrado
                    }
                })
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(Boolean.FALSE))); // Se o Mono estiver vazio, retorna 404 e Boolean.FALSE
    }


}
