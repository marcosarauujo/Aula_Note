package com.marcos.aulanote.business;

import com.marcos.aulanote.business.dto.out.TranscricaoDTOResponse;
import com.marcos.aulanote.business.mapper.TranscricaoMapper;
import com.marcos.aulanote.infrastructure.client.WhisperClient;
import com.marcos.aulanote.infrastructure.entity.Sessao;
import com.marcos.aulanote.infrastructure.entity.Transcricao;
import com.marcos.aulanote.infrastructure.exception.ResourceNotFoundException;
import com.marcos.aulanote.infrastructure.repository.SessaoRepository;
import com.marcos.aulanote.infrastructure.repository.TranscricaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j


public class TranscricaoService {
    private final TranscricaoRepository transcricaoRepository;
    private final TranscricaoMapper transcricaoMapper;
    private final SessaoRepository sessaoRepository;
    private final WhisperClient whisperClient;


    // O DTORequest agora não tem texto, vamos receber bytes!
// Então muda o parâmetro para byte[] e um nome.
    public TranscricaoDTOResponse salvarTranscricao(byte[] audioBytes, String nomeArquivo, String sessaoId) {
        Sessao sessao = sessaoRepository.findById(sessaoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Sessão não encontrada com o id: " + sessaoId));
        // Chama o Whisper para transcrever
        String conteudoTranscrito = whisperClient.transcrever(audioBytes, nomeArquivo);
        Transcricao transcricao = Transcricao.builder()
                .conteudo(conteudoTranscrito)
                .criadaEm(LocalDateTime.now())
                .sessao(sessao)
                .build();
        log.info("Transcrição salva para sessão: {}", sessaoId);
        return transcricaoMapper.paraDTOResponse(transcricaoRepository.save(transcricao));
    }

    public List<TranscricaoDTOResponse> bucasrorSessao(String sessaoId) {
        return transcricaoRepository.findBySessaoId(sessaoId)
                .stream()
                .map(transcricaoMapper::paraDTOResponse)
                .toList();
    }
}
