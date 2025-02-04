package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.HistoricoMedico;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class HistoricoMedicoService extends GenericService<HistoricoMedico> {

    private final Firestore firestore;
    private final ConsultaService consultaService;

    public HistoricoMedicoService(Firestore firestore, ConsultaService consultaService) {
        this.firestore = firestore;
        this.consultaService = consultaService;
    }

    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("historicoMedico");
    }

    public Mono<HistoricoMedico> buscarPorId(String id) {
        return fromApiFuture(firestore.collection("historicoMedico").document(id).get())
                .flatMap(documentSnapshot ->
                        documentSnapshot.exists()
                                ? Mono.just(documentSnapshot.toObject(HistoricoMedico.class))
                                : Mono.empty()
                );
    }

    // Verificar se a consulta existe
    public Mono<Boolean> verificarConsultaExistente(String consultaId) {
        return fromApiFuture(firestore.collection("consultas").document(consultaId).get())
                .flatMap(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        return Mono.just(true);  // Consulta encontrada
                    } else {
                        return Mono.just(false); // Consulta não encontrada
                    }
                });
    }
}
