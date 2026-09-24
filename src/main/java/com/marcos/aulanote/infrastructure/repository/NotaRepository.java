package com.marcos.aulanote.infrastructure.repository;

import com.marcos.aulanote.infrastructure.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotaRepository extends JpaRepository<Nota, String> {
    Optional<Nota> findBySessaoId(String sessaoId);
}
