package br.com.fiap.cp6.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tb_historico_chat")
public class HistoricoMensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pergunta", columnDefinition = "TEXT", nullable = false)
    private String pergunta;

    @Column(name = "resposta", columnDefinition = "TEXT", nullable = false)
    private String resposta;

    @Column(name = "provedor", length = 50, nullable = false)
    private String provedor;

    @Column(name = "modelo", length = 50, nullable = false)
    private String modelo;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    public HistoricoMensagem() {
    }

    public HistoricoMensagem(String pergunta, String resposta, String provedor, String modelo, LocalDateTime dataHora) {
        this.pergunta = pergunta;
        this.resposta = resposta;
        this.provedor = provedor;
        this.modelo = modelo;
        this.dataHora = dataHora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getProvedor() {
        return provedor;
    }

    public void setProvedor(String provedor) {
        this.provedor = provedor;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }
}
