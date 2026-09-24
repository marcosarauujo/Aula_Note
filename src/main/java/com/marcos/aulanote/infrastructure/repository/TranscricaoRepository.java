package com.marcos.aulanote.infrastructure.repository;

import com.marcos.aulanote.infrastructure.entity.Transcricao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscricaoRepository extends JpaRepository<Transcricao, String> {
    List<Transcricao> findBySessaoId(String sessaoId);

}
