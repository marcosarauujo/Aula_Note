package com.marcos.aulanote.infrastructure.repository;

import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.enums.StatusSessaoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, String> {
    List<Sessao> findByStatus(StatusSessaoEnum statusSessaoEnum);
}
