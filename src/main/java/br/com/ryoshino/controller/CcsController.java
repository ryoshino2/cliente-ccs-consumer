package br.com.ryoshino.controller;

import br.com.ryoshino.model.ClienteKafka;
import br.com.ryoshino.service.CcsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@RestController
public class CcsController {
    @Autowired
    private CcsService ccsService;

    @GetMapping
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/consumirPeloKafka")
    public void consumirPeloKafka() throws IOException {
        ccsService.consumirSincrono();
    }

    @GetMapping("/consumirPeloKafkaAssincrono")
    public void consumirPeloKafkaAssincrono() throws IOException {
        ccsService.consumirPeloKafkaAssincrono();
    }

    @GetMapping("/clienteKafka")
    public List<ClienteKafka> listarClientes() {
        return ccsService.listarClientes();
    }
}