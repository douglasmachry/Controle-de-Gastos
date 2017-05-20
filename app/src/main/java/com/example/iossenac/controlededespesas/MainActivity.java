package com.example.iossenac.controlededespesas;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.example.iossenac.controlededespesas.model.Despesa;
import com.example.iossenac.controlededespesas.model.DespesaAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Despesa> listaDespesa = new ArrayList<>();
    DespesaAdapter adaptador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listaDespesas);
        Calendar data = Calendar.getInstance();
        listaDespesa.add(new Despesa(data,4.9,"Lanche"));
        adaptador =  new DespesaAdapter(listaDespesa,this);

        ListView lista = (ListView) findViewById(R.id.listaDespesas);


        lista.setAdapter(adaptador);
    }

}
