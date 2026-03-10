package com.ramon.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record DadosResposta(
        @JsonAlias("results") List<DadosLivro> resultados
) {
}
