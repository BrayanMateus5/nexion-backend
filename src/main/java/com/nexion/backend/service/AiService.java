package com.nexion.backend.service;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.nexion.backend.dto.GeminiResponse;

@Service
public class AiService {

    private final RestClient restClient = RestClient.create();

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public String perguntar(String prompt) {
        Map<String, Object> corpo = Map.of("contents", List.of(Map.of("parts", List.of(Map.of("text", prompt)))));

        GeminiResponse resposta = restClient.post().uri(apiUrl).header("x-goog-api-key", apiKey)
                .contentType(MediaType.APPLICATION_JSON).body(corpo).retrieve().body(GeminiResponse.class);

        if (resposta == null || resposta.candidates() == null || resposta.candidates().isEmpty()) {
            throw new IllegalStateException("A IA não retornou nenhuma resposta");
        }
        return resposta.candidates().get(0).content().parts().get(0).text();
    }
}

//Só pra lembrar, aaqui no caso eu criei a parte onde eu envio a pergunta