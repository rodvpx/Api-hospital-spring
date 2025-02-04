package rodvpx.com.github.apihospitalspring.model;

import com.google.cloud.firestore.annotation.DocumentId;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.cloud.gcp.data.firestore.Document;

@Data
@Document(collectionName = "consultas")
public class Consulta {

    @DocumentId
    private String id;

    @NotBlank(message = "O ID do paciente não pode estar em branco")
    private String pacienteId;

    @NotBlank(message = "O ID do médico não pode estar em branco")
    private String medicoId;

    @NotBlank(message = "O ID do atendente não pode estar em branco")
    private String atendenteId;

    @NotNull(message = "A data da consulta não pode ser nula")
    private String dataConsulta;

    private String dataRetorno;

    @NotNull(message = "O status da consulta não pode ser nulo")
    private Status status;

    private String diagnostico;
    private String observacao;

    public enum Status {
        AGENDADA,
        EM_ANDAMENTO,
        CONCLUIDA,
        CANCELADA
    }
}
