package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.model.Paciente;

import java.util.Arrays;
import java.util.List;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class PacienteService extends GenericService<Paciente> {

    private final Firestore firestore;

    public PacienteService(Firestore firestore) {
        this.firestore = firestore;
    }

    // Cadastrar um novo paciente
    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("pacientes");
    }

    //buscar paciente pelo nome
    public Mono<Paciente> buscarPorNome(String nome) {
        // Divide o nome completo em partes
        List<String> nomeParts = Arrays.asList(nome.split(" "));

        // Pesquisa se algum paciente tem o nome que contém a parte fornecida
        return fromApiFuture(firestore.collection("pacientes").whereArrayContainsAny("nome", nomeParts).get())
                .flatMap(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        return Mono.empty(); // Se não houver documentos, retorna Mono.empty()
                    } else {
                        // Caso haja documentos, retorna o primeiro paciente encontrado
                        return Mono.just(querySnapshot.getDocuments().get(0).toObject(Paciente.class));
                    }
                });
    }

    // Buscar paciente pelo CPF
    public Mono<Paciente> buscarPorCpf(String cpf) {
        return fromApiFuture(firestore.collection("pacientes").whereEqualTo("cpf", cpf).get())
                .flatMap(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        return Mono.empty(); // Se não houver documentos, retorna Mono.empty()
                    } else {
                        // Caso haja documentos, retorna o primeiro paciente encontrado
                        return Mono.just(querySnapshot.getDocuments().get(0).toObject(Paciente.class));
                    }
                });
    }

    // Atualizar paciente
    public Mono<Boolean> atualizar(String id, Paciente pacienteAtualizado) {
        DocumentReference docRef = firestore.collection("pacientes").document(id);
        return fromApiFuture(docRef.get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return fromApiFuture(docRef.set(pacienteAtualizado)).thenReturn(true);
                    }
                    return Mono.just(false);
                });
    }

}
