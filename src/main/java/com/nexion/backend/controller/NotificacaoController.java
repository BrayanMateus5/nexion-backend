package com.nexion.backend.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.nexion.backend.service.NotificacaoService;
import com.nexion.backend.service.WalletAccessService;

@RestController
@RequestMapping("/api/v1/wallets/{walletId}")
public class NotificacaoController {

    private final NotificacaoService notificacaoService;
    private final WalletAccessService walletAccessService;

    public NotificacaoController(NotificacaoService notificacaoService, WalletAccessService walletAccessService) {
        this.notificacaoService = notificacaoService;
        this.walletAccessService = walletAccessService;
    }

    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@PathVariable Long walletId) {
        walletAccessService.verificarMembro(walletId);
        return notificacaoService.inscrever(walletId);
    }

}
