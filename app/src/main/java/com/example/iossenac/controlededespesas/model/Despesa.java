package com.example.iossenac.controlededespesas.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by iossenac on 20/05/17.
 */

public class Despesa {
    Calendar data;
    Double valor;
    String descricao;

    public Despesa(Calendar data, Double valor, String descricao) {
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }

    public Calendar getData() {
        return data;
    }

    public void setData(Calendar data) {
        this.data = data;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
