package br.com.fiap.cp6.controller;

import br.com.fiap.cp6.dto.ChatRequest;
import br.com.fiap.cp6.dto.ChatResponse;
import br.com.fiap.cp6.model.HistoricoMensagem;
import br.com.fiap.cp6.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChatService chatService;

    @Test
    @DisplayName("Deve processar pergunta válida e retornar 200 OK com payload estruturado")
    void deveProcessarPerguntaValida() throws Exception {
        ChatRequest request = new ChatRequest("Qual papel toalha indicar para clínicas médicas?");
        ChatResponse mockResponse = new ChatResponse(
                1L,
                "Para clínicas médicas, recomendamos o Papel Toalha Interfolha ou Lençol Hospitalar descartável.",
                "ollama",
                "llama3.2:1b",
                LocalDateTime.now()
        );

        Mockito.when(chatService.processarPergunta(Mockito.any(ChatRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.provedor").value("ollama"))
                .andExpect(jsonPath("$.modelo").value("llama3.2:1b"))
                .andExpect(jsonPath("$.resposta").exists());
    }

    @Test
    @DisplayName("Deve rejeitar mensagem vazia com status 400 Bad Request (Validação)")
    void deveRejeitarMensagemVazia() throws Exception {
        ChatRequest requestInvalido = new ChatRequest("   ");

        mockMvc.perform(post("/api/chat")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.erros.mensagem").exists());
    }

    @Test
    @DisplayName("Deve consultar o histórico com status 200 OK")
    void deveConsultarHistorico() throws Exception {
        HistoricoMensagem h1 = new HistoricoMensagem(
                "Indicação de higiênico 300m",
                "O rolão de 300m é indicado para altíssimo fluxo.",
                "ollama",
                "llama3.2:1b",
                LocalDateTime.now()
        );
        h1.setId(10L);

        Mockito.when(chatService.listarHistorico()).thenReturn(List.of(h1));

        mockMvc.perform(get("/api/historico"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].pergunta").value("Indicação de higiênico 300m"));
    }
}
