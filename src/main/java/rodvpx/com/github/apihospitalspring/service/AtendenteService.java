package rodvpx.com.github.apihospitalspring.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;
import rodvpx.com.github.apihospitalspring.model.Atendente;

import java.util.concurrent.ExecutionException;

@Service
public class AtendenteService {

    private final Firestore firestore;

    // Injeção correta do Firestore via construtor
    public AtendenteService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Cadastrar um novo atendente
    public String cadastrar(Atendente atendente) throws ExecutionException, InterruptedException {
        atendente.setId(null); // Garante que Firestore gere o ID automaticamente
        ApiFuture<DocumentReference> future = firestore.collection("atendentes").add(atendente);
        return future.get().getId(); // Retorna o ID gerado
    }

    // Buscar um atendente por email
    public Atendente buscarPorEmail(String email) throws ExecutionException, InterruptedException {
        CollectionReference atendentes = firestore.collection("atendentes");
        Query query = atendentes.whereEqualTo("email", email);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            return document.toObject(Atendente.class);
        }
        return null; // Ou lançar uma exceção
    }

    // Buscar um atendente por login
    public Atendente buscarPorLogin(String login) throws ExecutionException, InterruptedException {
        CollectionReference atendentes = firestore.collection("atendentes");
        Query query = atendentes.whereEqualTo("login", login);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        for (QueryDocumentSnapshot document : querySnapshot.get().getDocuments()) {
            return document.toObject(Atendente.class);
        }
        return null; // Ou lançar uma exceção
    }

    // Atualizar um atendente
    public boolean atualizar(String id, Atendente atendenteAtualizado) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("atendentes").document(id);
        ApiFuture<WriteResult> writeResult = docRef.set(atendenteAtualizado);
        return writeResult.get() != null;
    }

    // Deletar um atendente
    public boolean deletar(String id) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("atendentes").document(id);
        ApiFuture<WriteResult> writeResult = docRef.delete();
        return writeResult.get() != null;
    }
}
