package com.nexion.backend.dto;

import java.util.List;

public record GeminiResponse(List<Candidate> candidates) { // Aqui é a lista de candidatos as respostas

    public record Candidate(Content content) {
    } // cada candidato diferente, possui seu conteúdo específico..

    public record Content(List<Part> parts) {
    } // cada conteúdo, possui as suas partes..

    public record Part(String text) {
    } // essas partes se juntam e criam a resposta..
}
