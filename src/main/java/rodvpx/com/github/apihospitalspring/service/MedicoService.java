package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Medico;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class MedicoService extends GenericService<Medico> {

    @Autowired
    private Firestore firestore;

    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("medicos"); // Referência para a coleção "medicos" no Firestore
    }

    // Buscar médico por CRM
    public Mono<Medico> buscarPorCrm(String crm) {
        return fromApiFuture(firestore.collection("medicos").whereEqualTo("crm", crm).get())
                .flatMap(querySnapshot -> querySnapshot.isEmpty()
                        ? Mono.empty()
                        : Mono.just(querySnapshot.getDocuments().get(0).toObject(Medico.class)));
    }

    // Verificar se o CRM já existe
    public Mono<ResponseEntity<String>> verificarCrmExistente(String crm) {
        return fromApiFuture(firestore.collection("medicos")
                .whereEqualTo("crm", crm) // Verifica se já existe um médico com o mesmo CRM
                .get())
                .flatMap(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Se o CRM já existir, retorna erro
                        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("Erro: CRM já cadastrado!"));
                    }
                    return Mono.empty();
                });
    }

    // Cadastrar médico com verificação de CRM
    public Mono<ResponseEntity<String>> cadastrarMedico(Medico medico) {
        return verificarCrmExistente(medico.getCrm())
                .switchIfEmpty(
                        // Se o CRM não existir, continua com o cadastro
                        cadastrar(medico)
                                .map(id -> ResponseEntity.status(HttpStatus.CREATED)
                                        .body("Médico cadastrado com sucesso! ID: " + id))
                                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                        .body("Erro ao cadastrar médico")))
                );
    }
}
