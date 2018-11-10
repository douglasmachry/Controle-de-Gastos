package com.example.iossenac.controlededespesas;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.iossenac.controlededespesas.model.Despesa;
import com.example.iossenac.controlededespesas.model.Mascara;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;


public class cadastrarDespesa extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_despesa);
        getSupportActionBar().setHomeButtonEnabled(true);
        final EditText valorMonetario = (EditText)findViewById(R.id.editTextValor);
        valorMonetario.addTextChangedListener(new Mascara(valorMonetario));

        };

    public void confirmarDespesa(View view) {
        final EditText despesa = (EditText)findViewById(R.id.editTextDespesa);
        final EditText valor = (EditText)findViewById(R.id.editTextValor);
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Confirmar despesa:")
                .setMessage("Despesa: "+despesa.getText().toString()+"\n" +
                        "Valor: "+valor.getText().toString())
                .setPositiveButton("Ok", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cadastrarDespesa(despesa.getText().toString(),valor.getText().toString());
                    }

                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    public void cadastrarDespesa(String d, String v){
        v = v.replaceAll("[R$]", "").replaceAll("[,]", "")
                .replaceAll("[.]", "");
        Double val = Double.parseDouble(v);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("despesas");
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String id = mDatabase.push().getKey();
        Despesa novaDespesa = new Despesa(ts,val,d);
        mDatabase.child(id).setValue(novaDespesa);
    }


}
