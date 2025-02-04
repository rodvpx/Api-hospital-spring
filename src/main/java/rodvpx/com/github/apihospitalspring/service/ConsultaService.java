package rodvpx.com.github.apihospitalspring.service;

import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import rodvpx.com.github.apihospitalspring.model.Consulta;

import static rodvpx.com.github.apihospitalspring.util.ApiFutureUtils.fromApiFuture;

@Service
public class ConsultaService extends GenericService<Consulta> {

    private final Firestore firestore;

    public ConsultaService(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    protected CollectionReference getCollectionReference() {
        return firestore.collection("consultas");
    }

    public Mono<Consulta> buscarPorId(String id) {
        return fromApiFuture(firestore.collection("consultas").document(id).get())
                .flatMap(documentSnapshot ->
                        documentSnapshot.exists()
                                ? Mono.just(documentSnapshot.toObject(Consulta.class))
                                : Mono.empty()
                );
    }


}
