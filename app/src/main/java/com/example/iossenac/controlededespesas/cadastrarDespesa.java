package com.example.iossenac.controlededespesas;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iossenac.controlededespesas.model.Despesa;
import com.example.iossenac.controlededespesas.model.Mascara;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;


public class cadastrarDespesa extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_cadastrar_despesa);
       //getSupportActionBar().setHomeButtonEnabled(true);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Cadastrar Despesa");
        //myToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String user = currentUser.getUid();

        v = v.replaceAll("[R$]", "").replaceAll("[,]", "")
                .replaceAll("[.]", "");
        Double val = Double.parseDouble(v);
        DatabaseReference mDatabase;
        mDatabase = FirebaseDatabase.getInstance().getReference("users/"+user+"/despesas");
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        String id = mDatabase.push().getKey();
        Despesa novaDespesa = new Despesa(ts,val,d);
        mDatabase.child(id).setValue(novaDespesa);

        Intent it = new Intent(this,MainActivity.class);
        startActivity(it);
        Toast.makeText(this,"Despesa gravada com sucesso",Toast.LENGTH_LONG).show();
    }


}
