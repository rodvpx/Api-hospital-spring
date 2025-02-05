package rodvpx.com.github.apihospitalspring.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.util.ApiFutureUtils;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class GenericService<T> {

    @Autowired
    protected Firestore firestore; // Injetando Firestore

    // Método abstrato para obter a coleção do Firestore
    protected abstract CollectionReference getCollectionReference();

    // Método genérico para cadastrar qualquer modelo
    public Mono<String> cadastrar(T entity) {
        return Mono.defer(() -> {
            try {
                // Define o ID como null para o Firestore gerar automaticamente
                ApiFuture<DocumentReference> future = getCollectionReference().add(entity);
                return ApiFutureUtils.fromApiFuture(future)
                        .map(docRef -> docRef.getId()); // Retorna o ID gerado
            } catch (Exception e) {
                return Mono.error(e);
            }
        });
    }

    // Método genérico para atualizar um documento
    public Mono<Boolean> atualizar(String id, T entityAtualizado) {
        DocumentReference docRef = getCollectionReference().document(id);
        return ApiFutureUtils.fromApiFuture(docRef.get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return ApiFutureUtils.fromApiFuture(docRef.set(entityAtualizado)).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

    // Método genérico para deletar um documento
    public Mono<Boolean> deletar(String id) {
        DocumentReference docRef = getCollectionReference().document(id);
        return ApiFutureUtils.fromApiFuture(docRef.get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return ApiFutureUtils.fromApiFuture(docRef.delete()).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

    // Método genérico para listar todos os documentos de uma coleção
    public Flux<T> listar(Class<T> clazz) {
        return ApiFutureUtils.fromApiFuture(getCollectionReference().get())
                .flatMapMany(querySnapshot -> Flux.fromIterable(querySnapshot.getDocuments())
                        .map(document -> document.toObject(clazz))
                );
    }
}
