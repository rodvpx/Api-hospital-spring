package rodvpx.com.github.apihospitalspring.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;

import java.util.concurrent.Executors;

@Service
public class AtendenteService {

    private final Firestore firestore;

    public AtendenteService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Método genérico para converter ApiFuture para Mono
    private <T> Mono<T> fromApiFuture(ApiFuture<T> future) {
        return Mono.create(sink ->
                future.addListener(() -> {
                    try {
                        sink.success(future.get());
                    } catch (Exception e) {
                        sink.error(e);
                    }
                }, Executors.newSingleThreadExecutor())
        );
    }

    // Cadastrar um novo atendente
    public Mono<String> cadastrar(Atendente atendente) {
        atendente.setId(null);
        return fromApiFuture(firestore.collection("atendentes").add(atendente))
                .map(DocumentReference::getId);
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

    // Atualizar atendente
    public Mono<Boolean> atualizar(String id, Atendente atendenteAtualizado) {
        DocumentReference docRef = firestore.collection("atendentes").document(id);
        return fromApiFuture(docRef.get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return fromApiFuture(docRef.set(atendenteAtualizado)).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

    // Deletar atendente
    public Mono<Boolean> deletar(String id) {
        DocumentReference docRef = firestore.collection("atendentes").document(id);
        return fromApiFuture(docRef.get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return fromApiFuture(docRef.delete()).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

    // Listar todos os atendentes
    public Flux<Atendente> listar() {
        return fromApiFuture(firestore.collection("atendentes").get())
                .flatMapMany(querySnapshot -> Flux.fromIterable(querySnapshot.getDocuments())
                        .map(document -> document.toObject(Atendente.class))
                );
    }
}
