package com.nexion.backend.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class NotificacaoService {

    private final Map<Long, List<SseEmitter>> inscritos = new ConcurrentHashMap<>();

    public SseEmitter inscrever(Long walletId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);

        inscritos.computeIfAbsent(walletId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> remover(walletId, emitter));
        emitter.onTimeout(() -> remover(walletId, emitter));
        emitter.onError(e -> remover(walletId, emitter));

        return emitter;
    }

    public void notificar(Long walletId) {
        List<SseEmitter> lista = inscritos.get(walletId);
        if (lista == null)
            return;

        for (SseEmitter emitter : lista) {
            try {
                emitter.send(SseEmitter.event().name("transacoes").data("atualizado"));
            } catch (IOException e) {
                remover(walletId, emitter);
            }
        }
    }

    private void remover(Long walletId, SseEmitter emitter) {
        List<SseEmitter> lista = inscritos.get(walletId);
        if (lista != null) {
            lista.remove(emitter);
        }
    }
}
