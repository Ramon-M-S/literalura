package com.ramon.literalura.repository;

import com.ramon.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    /*Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND (a.anoFalecimento >= :ano OR a.anoFalecimento IS NuLL)")*/
    List<Autor> findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqualOrAnoNascimentoLessThanEqualAndAnoFalecimentoIsNull(Integer anoNascimento1, Integer anoFalecimento, Integer anoNascimento2);
}
