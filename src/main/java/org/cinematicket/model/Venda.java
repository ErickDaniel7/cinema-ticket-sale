package org.cinematicket.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vendas")
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomePessoa;
    private String nomeFilme;

    private LocalDateTime dataHoraFilme;
    private String localFilme;

    private String numeroIngresso;
    private BigDecimal valorIngresso;

    private boolean cancelado = false;

    public Venda() {}

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomePessoa() {
        return nomePessoa;
    }

    public void setNomePessoa(String nomePessoa) {
        this.nomePessoa = nomePessoa;
    }

    public String getNomeFilme() {
        return nomeFilme;
    }

    public void setNomeFilme(String nomeFilme) {
        this.nomeFilme = nomeFilme;
    }

    public LocalDateTime getDataHoraFilme() {
        return dataHoraFilme;
    }

    public void setDataHoraFilme(LocalDateTime dataHoraFilme) {
        this.dataHoraFilme = dataHoraFilme;
    }

    public String getLocalFilme() {
        return localFilme;
    }

    public void setLocalFilme(String localFilme) {
        this.localFilme = localFilme;
    }

    public String getNumeroIngresso() {
        return numeroIngresso;
    }

    public void setNumeroIngresso(String numeroIngresso) {
        this.numeroIngresso = numeroIngresso;
    }

    public BigDecimal getValorIngresso() {
        return valorIngresso;
    }

    public void setValorIngresso(BigDecimal valorIngresso) {
        this.valorIngresso = valorIngresso;
    }

    public boolean isCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }
}
