package rodvpx.com.github.apihospitalspring.model;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Consulta {

    private String id;

    @NotBlank(message = "O CPF do paciente não pode estar em branco")
    private String pacienteCpf;

    @NotBlank(message = "O CRM do médico não pode estar em branco")
    private String medicoCrm;

    @NotBlank(message = "O ID do atendente não pode estar em branco")
    private String atendenteId;

    @NotNull(message = "A data da consulta não pode ser nula")
    private LocalDateTime dataConsulta;

    private LocalDateTime dataRetorno;

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
