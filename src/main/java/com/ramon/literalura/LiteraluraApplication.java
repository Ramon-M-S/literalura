package com.ramon.literalura;

import com.ramon.literalura.principal.Principal;
import com.ramon.literalura.repository.LivroRepository;
import com.ramon.literalura.service.ConsumoApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private LivroRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Principal principal = new Principal(repository);
        principal.exibeMenu();
    }
}
