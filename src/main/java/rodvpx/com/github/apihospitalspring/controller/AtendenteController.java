package rodvpx.com.github.apihospitalspring.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.service.AtendenteService;

@RestController
@RequestMapping("/atendentes")
@Validated // Habilita validação para métodos que usam @Valid
public class AtendenteController {

    private final AtendenteService atendenteService;

    public AtendenteController(AtendenteService atendenteService) {
        this.atendenteService = atendenteService;
    }

    // Cadastrar atendente
    @PostMapping("/cadastrar")
    public Mono<ResponseEntity<String>> cadastrar(@Valid @RequestBody Atendente atendente) {
        // Verificando se o CPF já existe
        return atendenteService.verificarCpfExiste(atendente.getCpf())
                .flatMap(cpfExiste -> {
                    if (cpfExiste) {
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: CPF já cadastrado!"));
                    }
                    // Verificando se o login já existe
                    return atendenteService.verificarLoginExiste(atendente.getLogin())
                            .flatMap(loginExiste -> {
                                if (loginExiste) {
                                    return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Login já em uso!"));
                                }
                                // Verificando se o email já existe
                                return atendenteService.verificarEmailExiste(atendente.getEmail())
                                        .flatMap(emailExiste -> {
                                            if (emailExiste) {
                                                return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body("Erro: Email já cadastrado!"));
                                            }
                                            // Se todos os campos forem válidos, cadastrar o atendente
                                            return atendenteService.cadastrar(atendente)
                                                    .map(id -> ResponseEntity.status(HttpStatus.CREATED).body("Atendente cadastrado com sucesso! ID: " + id));
                                        });
                            });
                })
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao cadastrar atendente")));
    }



    // Buscar atendente por email
    @GetMapping("/email")
    public Mono<ResponseEntity<Atendente>> buscarPorEmail(@RequestParam String email) {
        return atendenteService.buscarPorEmail(email)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Buscar atendente por login
    @GetMapping("/login")
    public Mono<ResponseEntity<Atendente>> buscarPorLogin(@RequestParam String login) {
        return atendenteService.buscarPorLogin(login)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Listar atendentes
    @GetMapping("/listar")
    public Flux<ResponseEntity<Atendente>> listar() {
        return atendenteService.listar(Atendente.class)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Flux.just(ResponseEntity.noContent().build())); // Retorna 204 se não houver atendentes
    }

    // Atualizar atendente
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> atualizar(@PathVariable String id, @Valid @RequestBody Atendente atendenteAtualizado) {
        return atendenteService.atualizar(id, atendenteAtualizado)
                .map(updated -> updated ? ResponseEntity.ok(true) : ResponseEntity.status(404).body(false));
    }


    // Deletar atendente
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Boolean>> deletar(@PathVariable String id) {
        return atendenteService.deletar(id)
                .map(deleted -> deleted ? ResponseEntity.ok(true) : ResponseEntity.status(404).body(false));
    }

}
