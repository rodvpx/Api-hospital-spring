package rodvpx.com.github.apihospitalspring.model;

import lombok.Data;

import java.util.Date;

@Data
public class HistoricoMedico {
    private String id;
    private String consultaId;
    private Date data;
    private String descricao;
}
