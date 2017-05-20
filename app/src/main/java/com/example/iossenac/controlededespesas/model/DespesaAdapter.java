package com.example.iossenac.controlededespesas.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.iossenac.controlededespesas.R;

import java.util.List;

/**
 * Created by iossenac on 20/05/17.
 */

public class DespesaAdapter extends BaseAdapter {
    private List<Despesa> listaDespesas;
    private Context contexto;

    public DespesaAdapter(List<Despesa> listaDespesas, Context contexto) {
        this.listaDespesas = listaDespesas;
        this.contexto = contexto;
    }

    @Override
    public int getCount() {
        return listaDespesas.size();
    }

    @Override
    public Object getItem(int position) {
        return listaDespesas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Despesa Despesa = listaDespesas.get(position);

        LayoutInflater inflater = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.list_item,null);

        TextView textValor = (TextView) view.findViewById(R.id.textViewValor);
        textValor.setText(Despesa.getValor().toString());

        TextView textDescricao = (TextView) view.findViewById(R.id.textViewDescricao);
        textDescricao.setText(Despesa.getDescricao());

        TextView textData = (TextView) view.findViewById(R.id.textViewData);
        textData.setText(Despesa.getData().getTime().toString());

        return view;
    }
}