package rodvpx.com.github.apihospitalspring.repository;

import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HistoricoMedicoRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "historicosMedicos";

    // Métodos CRUD
}