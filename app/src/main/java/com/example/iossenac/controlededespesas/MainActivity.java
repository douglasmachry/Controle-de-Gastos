package com.example.iossenac.controlededespesas;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.example.iossenac.controlededespesas.model.Despesa;
import com.example.iossenac.controlededespesas.model.DespesaAdapter;
import com.example.iossenac.controlededespesas.model.Utils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;

import static com.example.iossenac.controlededespesas.R.menu.menu;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    protected GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;
    private AlertDialog alerta;

    private Intent login = new Intent(this, LoginActivity.class);
    public LocationUpdatesService locationUpdatesService;
    ArrayList listaDespesa = new ArrayList<>();
    private FirebaseAuth mAuth;
    DespesaAdapter adaptador;
    private  String user;
    private String TAG = "PRINCIPAL";
    private PlaceDetectionClient mPlaceDetectionClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            startActivity(login);
            finish();
        }else{
            user = currentUser.getUid();
        }

        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.inflateMenu(R.menu.menu);
        setSupportActionBar(myToolbar);


        startService(new Intent(getBaseContext(), LocationUpdatesService.class));
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,  this)
                .build();



        this.buscarDados();
        ListView listView = (ListView) findViewById(R.id.listaDespesas);
        Calendar data = Calendar.getInstance();
        //listaDespesa.add(despesa);
        //Log.d(TAG,listaDespesa.toString());
        adaptador = new DespesaAdapter(listaDespesa, this);

        //ListView lista = (ListView) findViewById(R.id.listaDespesas);


       listView.setAdapter(adaptador);
        FloatingActionButton menu = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cadastrarDespesa = new Intent(MainActivity.this, com.example.iossenac.controlededespesas.cadastrarDespesa.class);
                startActivity(cadastrarDespesa);
            }
        });
    }

    public void buscarDados() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference docRef = database.getReference("users/"+user+"/despesas");

        Despesa teste;
        // Read from the database
        docRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //listaDespesa.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Despesa despesa = postSnapshot.getValue(Despesa.class);
                    listaDespesa.add(despesa);
                    //Log.d(TAG, "Value is: " + postSnapshot.getValue());
                }
                adaptador.notifyDataSetChanged();
                //Log.d(TAG, "Value is: " + listaDespesa);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
                return;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                confirmar_logout();
                break;
            default:
                break;
        }
        return true;
    }




    private void confirmar_logout() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Confirmação");
        //define a mensagem
        builder.setMessage("Tem certeza que deseja sair?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                signOut();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                return;
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }


    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            callService();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           callService();
        } else {

        }
        return;
    }

    private  void callService(){
        Intent intent = new Intent(this, LocationUpdatesService.class);
        startService(intent);
    }




    @Override
    protected void onPause() {
        BroadcastReceiver myReceiver = null;
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();
    }
}
