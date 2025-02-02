package rodvpx.com.github.apihospitalspring.model;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.cloud.gcp.data.firestore.Document;
import com.google.cloud.firestore.annotation.DocumentId;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collectionName = "medicos")
public class Medico {

    @DocumentId
    private String id;

    @NotBlank(message = "O CRM não pode estar em branco")
    private String crm;

    @NotBlank(message = "O nome não pode estar em branco")
    private String nome;

    @NotNull(message = "A data de nascimento não pode ser nula")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "O sexo não pode ser nulo")
    private Paciente.Sexo sexo;

    @NotEmpty(message = "A lista de telefones não pode estar vazia")
    private List<@NotBlank(message = "O telefone não pode estar em branco") String> telefones;

    @NotEmpty(message = "A lista de especializações não pode estar vazia")
    private List<@NotBlank(message = "A especialização não pode estar em branco") String> especializacoes;
}

