package rodvpx.com.github.apihospitalspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rodvpx.com.github.apihospitalspring.repository.MedicoRepository;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Métodos de negócio
}