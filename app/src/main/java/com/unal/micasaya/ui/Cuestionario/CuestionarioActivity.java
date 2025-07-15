package com.unal.micasaya.ui.Cuestionario;


import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.Resultados.ResultadosActivity;

import java.util.ArrayList;

public class CuestionarioActivity extends AppCompatActivity {

    private ArrayList<LatLng> polygonPoints;
    private RadioGroup radioGroupSoilType, radioGroupBuildingUse;
    private EditText editTextNumFloors;
    private Spinner spinnerSeismicRisk;
    private Button buttonSubmitQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuestionario);

        if (getIntent().hasExtra("polygonPoints")) {
            polygonPoints = getIntent().getParcelableArrayListExtra("polygonPoints");

            if (polygonPoints != null && !polygonPoints.isEmpty()) {
                Toast.makeText(this, "Polígono recibido con " + polygonPoints.size() + " puntos.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "No se recibieron puntos del polígono.", Toast.LENGTH_LONG).show();

        }

        radioGroupSoilType = findViewById(R.id.radioGroupSoilType);
        radioGroupBuildingUse = findViewById(R.id.radioGroupBuildingUse);
        editTextNumFloors = findViewById(R.id.editTextNumFloors);
        spinnerSeismicRisk = findViewById(R.id.spinnerSeismicRisk);
        buttonSubmitQuestions = findViewById(R.id.buttonSubmitQuestions);

        buttonSubmitQuestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatherAndSubmitAnswers();
            }
        });
    }

    private void gatherAndSubmitAnswers() {
        // Recoger las respuestas
        int selectedSoilTypeId = radioGroupSoilType.getCheckedRadioButtonId();
        String soilType = "";
        if (selectedSoilTypeId != -1) {
            soilType = ((RadioButton) findViewById(selectedSoilTypeId)).getText().toString();
        }

        String numFloorsStr = editTextNumFloors.getText().toString().trim();
        int numFloors = 0;
        if (!numFloorsStr.isEmpty()) {
            numFloors = Integer.parseInt(numFloorsStr);
        }

        String seismicRisk = spinnerSeismicRisk.getSelectedItem().toString();

        int selectedBuildingUseId = radioGroupBuildingUse.getCheckedRadioButtonId();
        String buildingUse = "";
        if (selectedBuildingUseId != -1) {
            buildingUse = ((RadioButton) findViewById(selectedBuildingUseId)).getText().toString();
        }

        // Validaciones básicas
        if (soilType.isEmpty() || numFloors <= 0 || buildingUse.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todas las preguntas.", Toast.LENGTH_SHORT).show();
            return;
        }


        Intent intent = new Intent(CuestionarioActivity.this, ResultadosActivity.class);
        intent.putExtra("polygonPoints", polygonPoints); // Re-envía los puntos del polígono
        intent.putExtra("soilType", soilType);
        intent.putExtra("numFloors", numFloors);
        intent.putExtra("seismicRisk", seismicRisk);
        intent.putExtra("buildingUse", buildingUse);

        startActivity(intent);
        finish();
    }
}
