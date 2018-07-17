package com.androidmorefast.pc.appmostrardireccionmapa;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class GestionarCoordenada extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final String TAG = GestionarCoordenada.class.getSimpleName();
    private TextView lat, lng;
    private Button obtenerUbicacion, verMapa;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private static int UPDATE_INTERVAL = 10000;
    private static int FATEST_INTERVAL = 5000;
    private static int DISPLACEMENT = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestionar_coordenada);

        obtenerUbicacion = (Button) findViewById(R.id.btnAtualizarUbicacion);
        verMapa = (Button) findViewById(R.id.btnVerMapa);
        lat = (TextView) findViewById(R.id.txtLatitude);
        lng = (TextView) findViewById(R.id.txtLongitude);
        if (checkPlayServices()) {
            buildGoogleApiClient();
            createLocationRequest();
        }
        obtenerUbicacion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                togglePeriodLocationUpdates();

            }
        });

        verMapa.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(GestionarCoordenada.this, MapasActivity.class);
                startActivity(i);
                finish();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
            displayLocation();
            Log.d("onStart ", "GoogleApiClient se ha creado");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
        if (mGoogleApiClient.isConnected() && mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }


    private void displayLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }else{
            //Do Your Stuff

        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        Log.d("Ultima Ubicación ", "encontrada");
        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();
            lat.setText(latitude + "");
            lng.setText(longitude + "");

        } else {
            lat.setText("0.0");
            lng.setText("0.0");
        }


    }


    private void togglePeriodLocationUpdates() {
        if (!mRequestLocationUpdates) {
            obtenerUbicacion.setText(getString(R.string.btn_detener_actualizaciones));

            mRequestLocationUpdates = true;

            startLocationUpdates();

        } else {
            obtenerUbicacion.setText(getString(R.string.btn_actualizacion_ubicacion));

            mRequestLocationUpdates = false;

            stopLocationUpdates();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        Log.d("GoogleApiClient ", " se ha creado");
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
        Log.d("Solicitud de ubicacion", " es creado");
    }

    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "Este dispositivo no es compatible.");
                finish();
            }
            return false;
        }
        return true;
    }


    protected void startLocationUpdates() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }else{
            //Do Your Stuff

        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d("La ubicacion ", "esta actualizada");
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        if (mRequestLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }




    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        Toast.makeText(getApplicationContext(), "Ubicación cambiada!", Toast.LENGTH_SHORT).show();

        displayLocation();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Conexion fallida: " + connectionResult.getErrorCode());
    }

}
