package br.com.fiap.cp6.service;

import br.com.fiap.cp6.dto.ChatRequest;
import br.com.fiap.cp6.dto.ChatResponse;
import br.com.fiap.cp6.model.HistoricoMensagem;
import br.com.fiap.cp6.repository.HistoricoMensagemRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final HistoricoMensagemRepository repository;

    @Value("${spring.ai.model.chat:ollama}")
    private String provedor;

    @Value("${spring.ai.ollama.chat.options.model:llama3.2:1b}")
    private String modelo;

    // System prompt calibrado para tom assertivo e técnico
    private static final String SYSTEM_PROMPT = """
        Você é o 'PapelTech Specialist', um consultor técnico sênior da indústria de Papéis Institucionais (higiene corporativa, saúde e food service).

        PORTFÓLIO OFICIAL:
        1. Papel Toalha Interfolha: para dispensers folha a folha em banheiros corporativos, consultórios e cozinhas.
        2. Papel Toalha Bobina Institucional: 100 metros e 200 metros para dispensers de alta circulação.
        3. Papel Higiênico Institucional (Rolão):
           - Metragens: 200m, 250m e 300m.
           - Versões por Peso: 1,5 kg e 2,0 kg.
           - Ideal para banheiros de alto tráfego para reduzir a reposição pela equipe de limpeza.
        4. Lençol Hospitalar: rolo descartável para macas em clínicas médicas, odontológicas, estética e hospitais.
        5. Guardanapo TV: específico para dispensers de balcão (tipo TV) em lanchonetes e praças de alimentação.

        DIRETRIZES RIGOROSAS DE RESPOSTA:
        - NUNCA comece pedindo desculpas, nunca diga "peço desculpas pelo erro anterior" e nunca assuma culpas ou erros fictícios.
        - Seja sempre direto, seguro, assertivo e técnico desde a primeira palavra da resposta.
        - Toda pergunta do usuário é uma consulta legítima de compra ou especificação técnica.
        - Responda SEMPRE em português do Brasil, apresentando as vantagens comparativas de metragem, peso e rendimento.
        - Recuse educadamente qualquer assunto fora de papéis institucionais e higiene profissional.
        """;

    public ChatService(ChatClient.Builder chatClientBuilder, HistoricoMensagemRepository repository) {
        this.chatClient = chatClientBuilder.build();
        this.repository = repository;
    }

    public ChatResponse processarPergunta(ChatRequest request) {
        // Chamada oficial do Spring AI via ChatClient conforme documentado no roteiro
        String respostaTexto = this.chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(request.getMensagem())
                .call()
                .content();

        if (respostaTexto == null || respostaTexto.isBlank()) {
            respostaTexto = "Não foi possível obter uma resposta do assistente no momento.";
        }

        // Sanitização de vício de linguagem do modelo Llama
        respostaTexto = limparViciosDeLinguagem(respostaTexto);

        LocalDateTime agora = LocalDateTime.now();

        // Persistência no Banco de Dados JPA
        HistoricoMensagem historico = new HistoricoMensagem(
                request.getMensagem(),
                respostaTexto,
                this.provedor,
                this.modelo,
                agora
        );
        HistoricoMensagem salvo = repository.save(historico);

        return new ChatResponse(
                salvo.getId(),
                salvo.getResposta(),
                salvo.getProvedor(),
                salvo.getModelo(),
                salvo.getDataHora()
        );
    }

    public List<HistoricoMensagem> listarHistorico() {
        return repository.findAllByOrderByDataHoraDesc();
    }

    private String limparViciosDeLinguagem(String texto) {
        if (texto == null) return "";
        // Remove frases de alucinação de desculpa como "Peço desculpas pelo erro anterior..."
        String limpo = texto.replaceAll("(?i)^(peço desculpas pelo erro anterior\\.?\\s*|você está cert[oa] em questionar essa escolha\\.?\\s*)+", "");
        return limpo.substring(0, 1).toUpperCase() + limpo.substring(1);
    }
}
