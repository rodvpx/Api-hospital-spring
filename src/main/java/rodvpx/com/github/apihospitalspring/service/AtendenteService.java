package rodvpx.com.github.apihospitalspring.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;

@Service
public class AtendenteService {

    private final Firestore firestore;

    // Injeção correta do Firestore via construtor
    public AtendenteService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Cadastrar um novo atendente
    public Mono<String> cadastrar(Atendente atendente) {
        atendente.setId(null); // Garante que Firestore gere o ID automaticamente
        return Mono.defer(() -> Mono.fromCallable(() -> {
            ApiFuture<DocumentReference> future = firestore.collection("atendentes").add(atendente);
            return future.get().getId(); // Retorna o ID gerado
        }));
    }

    // Buscar um atendente por email
    public Mono<Atendente> buscarPorEmail(String email) {
        CollectionReference atendentes = firestore.collection("atendentes");
        Query query = atendentes.whereEqualTo("email", email);
        return Mono.defer(() -> Mono.fromCallable(() -> {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
                return document.toObject(Atendente.class);
            }
            return null; // Ou lançar uma exceção
        }));
    }

    // Buscar um atendente por login
    public Mono<Atendente> buscarPorLogin(String login) {
        CollectionReference atendentes = firestore.collection("atendentes");
        Query query = atendentes.whereEqualTo("login", login);
        return Mono.defer(() -> Mono.fromCallable(() -> {
            ApiFuture<QuerySnapshot> querySnapshot = query.get();
            for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
                return document.toObject(Atendente.class);
            }
            return null; // Ou lançar uma exceção
        }));
    }

    public Mono<Boolean> atualizar(String id, Atendente atendenteAtualizado) {
        DocumentReference docRef = firestore.collection("atendentes").document(id);

        return Mono.fromCallable(() -> {
            // Verificar se o documento existe antes de tentar atualizar
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                // Atualizar o documento se ele existir
                ApiFuture<WriteResult> writeResult = docRef.set(atendenteAtualizado);
                return writeResult.get() != null; // Retorna true se a atualização foi bem-sucedida
            } else {
                return false; // Retorna false se o documento não existir
            }
        });
    }



    public Mono<Boolean> deletar(String id) {
        DocumentReference docRef = firestore.collection("atendentes").document(id);

        return Mono.fromCallable(() -> {
            // Verificar se o documento existe antes de tentar deletar
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot documentSnapshot = future.get();

            if (documentSnapshot.exists()) {
                // Se o documento existir, deleta
                ApiFuture<WriteResult> writeResult = docRef.delete();
                writeResult.get(); // Executa a operação de exclusão
                return true; // Retorna true se a exclusão foi bem-sucedida
            } else {
                return false; // Se o documento não existir, retorna false
            }
        });
    }

    public Flux<Atendente> listar() {
        return Mono.fromCallable(() -> firestore.collection("atendentes").get().get()) // Busca todos os atendentes
                .flatMapMany(querySnapshot -> Flux.fromIterable(querySnapshot.getDocuments())
                        .map(document -> document.toObject(Atendente.class)) // Converte documentos para Atendente
                );
    }


}
