package br.com.fiap.cp6.repository;

import br.com.fiap.cp6.model.HistoricoMensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoricoMensagemRepository extends JpaRepository<HistoricoMensagem, Long> {

    // Requisito do professor: ordenação por data
    List<HistoricoMensagem> findAllByOrderByDataHoraDesc();
}
