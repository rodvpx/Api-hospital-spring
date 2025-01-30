package rodvpx.com.github.apihospitalspring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.service.AtendenteService;

import java.util.concurrent.ExecutionException;

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
    public String cadastrar(@RequestBody Atendente atendente) throws ExecutionException, InterruptedException {
        return atendenteService.cadastrar(atendente);
    }

    // Endpoint para buscar um atendente por email
    @GetMapping("/buscar-por-email")
    public Atendente buscarPorEmail(@RequestParam String email) throws ExecutionException, InterruptedException {
        return atendenteService.buscarPorEmail(email);
    }

    // Endpoint para buscar um atendente por login
    @GetMapping("/buscar-por-login")
    public Atendente buscarPorLogin(@RequestParam String login) throws ExecutionException, InterruptedException {
        return atendenteService.buscarPorLogin(login);
    }

    // Endpoint para atualizar um atendente
    @PutMapping("/{id}")
    public boolean atualizar(@PathVariable String id, @RequestBody Atendente atendenteAtualizado) throws ExecutionException, InterruptedException {
        return atendenteService.atualizar(id, atendenteAtualizado);
    }

    // Endpoint para deletar um atendente
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deletar(@PathVariable String id) throws ExecutionException, InterruptedException {
        return atendenteService.deletar(id);
    }
}
