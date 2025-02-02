package rodvpx.com.github.apihospitalspring.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HistoricoMedico {

    private String id;

    @NotBlank(message = "O ID da consulta não pode estar em branco")
    private String consultaId;

    @NotNull(message = "A data não pode ser nula")
    private LocalDateTime data;

    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricao;
}
