package br.com.fiap.cp6.dto;

import java.time.LocalDateTime;

public class ChatResponse {

    private Long id;
    private String resposta;
    private String provedor;
    private String modelo;
    private LocalDateTime dataHora;

    public ChatResponse() {
    }

    public ChatResponse(Long id, String resposta, String provedor, String modelo, LocalDateTime dataHora) {
        this.id = id;
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
