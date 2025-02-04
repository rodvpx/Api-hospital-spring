package rodvpx.com.github.apihospitalspring.model;

import com.google.cloud.firestore.annotation.DocumentId;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.cloud.gcp.data.firestore.Document;


@Data
@Document(collectionName = "historicoMedico")
public class HistoricoMedico {

    @DocumentId
    private String id;

    @NotBlank(message = "O ID da consulta não pode estar em branco")
    private String consultaId;

    @NotNull(message = "A data não pode ser nula")
    private String data;

    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricao;
}
