package com.example.iossenac.controlededespesas;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.example.iossenac.controlededespesas.model.Despesa;
import com.example.iossenac.controlededespesas.model.DespesaAdapter;
import com.example.iossenac.controlededespesas.model.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    protected GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleApiClient;
    public LocationUpdatesService locationUpdatesService;
    ArrayList listaDespesa = new ArrayList<>();
    DespesaAdapter adaptador;
    private String TAG = "PRINCIPAL";
    private PlaceDetectionClient mPlaceDetectionClient;

    public void buscarDados() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference docRef = database.getReference("despesas");
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
                Log.d(TAG, "Value is: " + listaDespesa);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
