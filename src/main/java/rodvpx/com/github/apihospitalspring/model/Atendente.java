package rodvpx.com.github.apihospitalspring.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import lombok.Data;

import java.util.List;

@Data
@Document(collectionName = "atendentes")
public class Atendente {

        @DocumentId // Firestore vai gerar o ID automaticamente
        private String id;

        private String nome;
        private String cpf;
        private String email;
        private List<String> telefones;
        private String login;
        private String senha;
}
