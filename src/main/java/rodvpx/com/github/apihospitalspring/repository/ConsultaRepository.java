package rodvpx.com.github.apihospitalspring.repository;

import com.google.cloud.firestore.Firestore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ConsultaRepository {

    @Autowired
    private Firestore firestore;

    private static final String COLLECTION_NAME = "consultas";

    // Métodos CRUD
}
