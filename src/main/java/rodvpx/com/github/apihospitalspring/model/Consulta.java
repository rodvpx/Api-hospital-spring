package rodvpx.com.github.apihospitalspring.model;

import lombok.Data;

import java.util.Date;

@Data
public class Consulta {
    private String id;
    private String pacienteCpf;
    private String medicoCrm;
    private String atendenteId;
    private Date dataConsulta;
    private Date dataRetorno;
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
