package br.com.fiap.cp6.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Requisito 4: Validação de mensagens vazias e tamanho máximo (400 Bad Request)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("timestamp", LocalDateTime.now());

        Map<String, String> erros = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erros.put(error.getField(), error.getDefaultMessage());
        }
        body.put("erros", erros);
        body.put("mensagem", "Erro de validação nos campos informados.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    // Requisito 4: Tratar falhas de conexão e indisponibilidade do modelo (503 Service Unavailable / 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        body.put("timestamp", LocalDateTime.now());
        body.put("erro", "Serviço de Inteligência Artificial indisponível");
        body.put("mensagem", "Não foi possível conectar ao provedor de IA local (Ollama). Verifique se o serviço está ativo em http://localhost:11434.");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }
}
