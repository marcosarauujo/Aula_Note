package com.marcos.aulanote.infrastructure.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transcricoes")
@Builder

public class Transcricao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(columnDefinition = "TEXT")
    private String conteudo;
    private LocalDateTime criadaEm;

    @ManyToOne
    @JoinColumn(name = "sessao_id")
    private Sessao sessao;
}
