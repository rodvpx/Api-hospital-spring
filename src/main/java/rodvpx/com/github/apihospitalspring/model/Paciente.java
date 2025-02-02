package rodvpx.com.github.apihospitalspring.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.cloud.gcp.data.firestore.Document;
import com.google.cloud.firestore.annotation.DocumentId;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collectionName = "pacientes")
public class Paciente {

    @DocumentId
    private String id;

    @NotBlank(message = "O CPF não pode estar em branco")
    @Size(min = 11, max = 11, message = "O CPF deve ter 11 dígitos")
    private String cpf;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "O sexo não pode ser nulo")
    private Sexo sexo;

    @NotEmpty(message = "A lista de telefones não pode estar vazia")
    private List<@NotBlank(message = "O telefone não pode estar em branco") String> telefones;

    @NotEmpty(message = "A lista de endereços não pode estar vazia")
    private List<@Valid Endereco> enderecos;

    @NotEmpty(message = "A lista de contatos de emergência não pode estar vazia")
    private List<@Valid ContatoEmergencia> contatosEmergencia;

    public enum Sexo {
        MASCULINO, FEMININO, OUTRO
    }


    @Data
    public class Endereco {

        @NotBlank(message = "A rua não pode estar em branco")
        private String rua;

        @NotBlank(message = "O número não pode estar em branco")
        private String numero;

        @NotBlank(message = "O bairro não pode estar em branco")
        private String bairro;

        @NotBlank(message = "O CEP não pode estar em branco")
        private String cep;
    }

    @Data
    public class ContatoEmergencia {

        @NotBlank(message = "O nome do contato de emergência não pode estar em branco")
        private String nome;

        @NotBlank(message = "O telefone do contato de emergência não pode estar em branco")
        private String telefone;
    }
}