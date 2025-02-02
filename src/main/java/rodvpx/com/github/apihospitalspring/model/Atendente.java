package rodvpx.com.github.apihospitalspring.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.cloud.gcp.data.firestore.Document;
import com.google.cloud.firestore.annotation.DocumentId;

import java.util.List;

@Data
@Document(collectionName = "atendentes")
public class Atendente {

        @DocumentId
        private String id;

        @NotBlank(message = "O nome não pode estar em branco")
        private String nome;

        @NotBlank(message = "O CPF não pode estar em branco")
        @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
        private String cpf;

        @NotBlank(message = "O email não pode estar em branco")
        private String email;

        @NotEmpty(message = "A lista de telefones não pode estar vazia") // Aqui está a correção
        private List<String> telefones;

        @NotBlank(message = "O login não pode estar em branco")
        private String login;

        @NotBlank(message = "A senha não pode estar em branco")
        private String senha;
}
