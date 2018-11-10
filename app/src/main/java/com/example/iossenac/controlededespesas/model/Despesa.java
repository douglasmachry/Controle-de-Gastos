package com.example.iossenac.controlededespesas.model;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by iossenac on 20/05/17.
 */

public class Despesa {
    String data;
    double valor;
    String descricao;

    public Despesa(){}

    public Despesa(String data, double valor, String descricao) {
        this.data = data;
        this.valor = valor;
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
