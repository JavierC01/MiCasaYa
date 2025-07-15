package com.unal.micasaya.ui.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.Cuestionario.CuestionarioActivity;

import java.util.ArrayList;
import java.util.List;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private Button buttonDrawPolygon, buttonConfirmPolygon, buttonClearPolygon;
    private List<LatLng> polygonPoints = new ArrayList<>();
    private Polygon currentPolygon;
    private boolean isDrawingMode = false;
    private List<Marker> polygonMarkers = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        buttonDrawPolygon = findViewById(R.id.buttonDrawPolygon);
        buttonConfirmPolygon = findViewById(R.id.buttonConfirmPolygon);
        buttonClearPolygon = findViewById(R.id.buttonClearPolygon);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        buttonDrawPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleDrawingMode();
            }
        });

        buttonConfirmPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmPolygon();
            }
        });

        buttonClearPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearPolygon();
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        checkLocationPermission();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (isDrawingMode) {
                    addPolygonPoint(latLng);
                }
            }
        });
    }

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            enableMyLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation();
            } else {
                Toast.makeText(this, "Permiso de ubicación denegado. No se puede centrar el mapa en tu ubicación.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void enableMyLocation() {
        if (mMap != null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
                                } else {
                                    Toast.makeText(MapaActivity.this, "No se pudo obtener la ubicación actual.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        }
    }

    private void toggleDrawingMode() {
        isDrawingMode = !isDrawingMode;
        if (isDrawingMode) {
            Toast.makeText(this, "Modo de dibujo activado. Toca el mapa para añadir puntos.", Toast.LENGTH_SHORT).show();
            buttonDrawPolygon.setText("Finalizar Dibujo");
            buttonConfirmPolygon.setVisibility(View.VISIBLE);
            buttonClearPolygon.setVisibility(View.VISIBLE);
            clearPolygon(); // Limpia cualquier polígono previo al iniciar un nuevo dibujo
        } else {
            Toast.makeText(this, "Modo de dibujo desactivado.", Toast.LENGTH_SHORT).show();
            buttonDrawPolygon.setText("Dibujar Polígono");
            // Los botones Confirmar y Limpiar se mantienen visibles si hay puntos
        }
    }

    private void addPolygonPoint(LatLng point) {
        polygonPoints.add(point);
        Marker marker = mMap.addMarker(new MarkerOptions().position(point).title("Punto " + polygonPoints.size()));
        if (marker != null) {
            polygonMarkers.add(marker);
        }
        updatePolygon();
    }

    private void updatePolygon() {
        if (currentPolygon != null) {
            currentPolygon.remove();
        }
        if (polygonPoints.size() > 1) {
            PolygonOptions polygonOptions = new PolygonOptions()
                    .addAll(polygonPoints)
                    .strokeColor(Color.BLUE)
                    .fillColor(Color.argb(90, 0, 0, 255)); // Azul semitransparente
            currentPolygon = mMap.addPolygon(polygonOptions);
        }
    }

    private void clearPolygon() {
        if (currentPolygon != null) {
            currentPolygon.remove();
            currentPolygon = null;
        }
        for (Marker marker : polygonMarkers) {
            marker.remove();
        }
        polygonMarkers.clear();
        polygonPoints.clear();
        buttonConfirmPolygon.setVisibility(View.GONE);
        buttonClearPolygon.setVisibility(View.GONE);
        buttonDrawPolygon.setText("Dibujar Polígono"); // Resetea el texto
        isDrawingMode = false; // Desactiva el modo de dibujo
    }

    private void confirmPolygon() {
        if (polygonPoints.size() < 3) {
            Toast.makeText(this, "Se necesitan al menos 3 puntos para formar un polígono.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Aquí pasas los puntos del polígono a la siguiente actividad
        ArrayList<LatLng> pointsToSend = new ArrayList<>(polygonPoints);
        Intent intent = new Intent(MapaActivity.this, CuestionarioActivity.class);
        intent.putExtra("polygonPoints", pointsToSend); // Envías la lista de puntos
        startActivity(intent);
        finish(); // Finaliza la actividad del mapa
    }
}