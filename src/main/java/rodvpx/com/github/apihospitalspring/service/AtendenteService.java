package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;

import java.util.List;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class AtendenteService extends GenericService<Atendente> {

    private final Firestore firestore;

    public AtendenteService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Buscar atendente por email
    public Mono<Atendente> buscarPorEmail(String email) {
        return fromApiFuture(firestore.collection("atendentes")
                .whereEqualTo("email", email)
                .get()
        ).flatMap(querySnapshot -> querySnapshot.isEmpty()
                ? Mono.empty()
                : Mono.just(querySnapshot.getDocuments().get(0).toObject(Atendente.class))
        );
    }

    // Buscar atendente por login
    public Mono<Atendente> buscarPorLogin(String login) {
        return fromApiFuture(firestore.collection("atendentes")
                .whereEqualTo("login", login)
                .get()
        ).flatMap(querySnapshot -> querySnapshot.isEmpty()
                ? Mono.empty()
                : Mono.just(querySnapshot.getDocuments().get(0).toObject(Atendente.class))
        );
    }

    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("atendentes");
    }

    // Verificar se o CPF já existe
    public Mono<Boolean> verificarCpfExiste(String cpf) {
        return fromApiFuture(firestore.collection("atendentes")
                .whereEqualTo("cpf", cpf)
                .get())
                .map(querySnapshot -> !querySnapshot.isEmpty())
                .defaultIfEmpty(false);
    }

    // Verificar se o Login já existe
    public Mono<Boolean> verificarLoginExiste(String login) {
        return fromApiFuture(firestore.collection("atendentes")
                .whereEqualTo("login", login)
                .get())
                .map(querySnapshot -> !querySnapshot.isEmpty())
                .defaultIfEmpty(false);
    }

    // Verificar se o Email já existe
    public Mono<Boolean> verificarEmailExiste(String email) {
        return fromApiFuture(firestore.collection("atendentes")
                .whereEqualTo("email", email)
                .get())
                .map(querySnapshot -> !querySnapshot.isEmpty())
                .defaultIfEmpty(false);
    }


}
