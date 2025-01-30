package rodvpx.com.github.apihospitalspring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rodvpx.com.github.apihospitalspring.service.HistoricoMedicoService;

@RestController
@RequestMapping("/historicos")
public class HistoricoMedicoController {

    @Autowired
    private HistoricoMedicoService historicoMedicoService;

    // Endpoints CRUD
}