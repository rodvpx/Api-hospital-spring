package rodvpx.com.github.apihospitalspring.model;

import com.google.cloud.firestore.annotation.DocumentId;
import com.google.cloud.spring.data.firestore.Document;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
@Document(collectionName = "atendentes")
public class Atendente {

        @DocumentId // Firestore vai gerar o ID automaticamente
        private String id;

        @NotBlank
        private String nome;
        @NotBlank
        private String cpf;
        @NotBlank
        private String email;
        @NotBlank
        private List<String> telefones;
        @NotBlank
        private String login;
        @NotBlank
        private String senha;
}
