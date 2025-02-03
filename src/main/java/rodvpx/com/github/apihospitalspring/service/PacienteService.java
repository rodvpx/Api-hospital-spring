package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.Firestore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Atendente;
import rodvpx.com.github.apihospitalspring.model.Paciente;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class PacienteService extends GenericService<Paciente> {

    private final Firestore firestore;

    public PacienteService(Firestore firestore) {
        this.firestore = firestore;
    }

    public Mono<Paciente> buscarPorNome(String nome) {
        return fromApiFuture(firestore.collection("pacientes")
                .whereEqualTo("nome", nome)
                .get()
        ).flatMap(querySnapshot -> querySnapshot.isEmpty()
                ? Mono.empty()
                : Mono.just(querySnapshot.getDocuments().get(0).toObject(Paciente.class))
        );
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

    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("pacientes");
    }



}
