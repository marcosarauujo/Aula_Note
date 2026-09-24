package com.marcos.aulanote.infrastructure.entity;


import com.marcos.aulanote.infrastructure.enums.StatusSessaoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sessoes")
@Builder


public class Sessao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String titulo;
    private LocalDateTime iniciadaEm;
    private LocalDateTime finalizadaEm;
    @Enumerated(EnumType.STRING)
    private StatusSessaoEnum statusSessaoEnum;
}
