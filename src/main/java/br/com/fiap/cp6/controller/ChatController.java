package br.com.fiap.cp6.controller;

import br.com.fiap.cp6.dto.ChatRequest;
import br.com.fiap.cp6.dto.ChatResponse;
import br.com.fiap.cp6.model.HistoricoMensagem;
import br.com.fiap.cp6.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // Requisito: POST /api/chat recebe pergunta em JSON e devolve resposta em JSON
    @PostMapping("/chat")
    public ResponseEntity<ChatResponse> enviarMensagem(@Valid @RequestBody ChatRequest request) {
        ChatResponse response = chatService.processarPergunta(request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Requisito: GET /api/historico com ordenação por data
    @GetMapping("/historico")
    public ResponseEntity<List<HistoricoMensagem>> consultarHistorico() {
        List<HistoricoMensagem> historico = chatService.listarHistorico();
        return ResponseEntity.ok(historico);
    }
}
