package rodvpx.com.github.apihospitalspring.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class Paciente {
    private String cpf;
    private String nome;
    private LocalDate idade;
    private Sexo sexo;
    private List<String> telefones;
    private List<Endereco> enderecos;
    private List<ContatoEmergencia> contatosEmergencia;


    public enum Sexo {
        MASCULINO, FEMININO, OUTRO
    }
}

@Data
class Endereco {
    private String rua;
    private String numero;
    private String bairro;
    private String cep;
}

@Data
class ContatoEmergencia {
    private String nome;
    private String telefone;
}
