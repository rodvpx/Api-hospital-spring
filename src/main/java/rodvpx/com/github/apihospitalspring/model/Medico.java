package rodvpx.com.github.apihospitalspring.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Medico {
    private String crm;
    private String nome;
    private LocalDate idade;
    private Paciente.Sexo sexo;
    private List<String> telefones;
    private List<String> especializacoes;
}
