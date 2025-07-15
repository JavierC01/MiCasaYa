package com.unal.micasaya.ui.Resultados;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


import com.unal.micasaya.R;
import com.unal.micasaya.ui.Home.HomeActivity;


public class ResultadosActivity extends AppCompatActivity {

    private TextView textViewRecommendations;
    private ImageView imageViewFloorPlan;
    private Button buttonSaveProject;

    // Datos recibidos del cuestionario
    private ArrayList<LatLng> polygonPoints;
    private String soilType;
    private int numFloors;
    private String seismicRisk;
    private String buildingUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        textViewRecommendations = findViewById(R.id.textViewRecommendations);
        imageViewFloorPlan = findViewById(R.id.imageViewFloorPlan);
        buttonSaveProject = findViewById(R.id.buttonSaveProject);

        // 1. Obtener datos del Intent
        getProjectDataFromIntent();

        // 2. Generar recomendaciones con Gemini
        generateRecommendationsWithGemini();

        // 3. Generar plano con IA
        generateFloorPlanWithAI();

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProject();
            }
        });
    }

    private void getProjectDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            polygonPoints = intent.getParcelableArrayListExtra("polygonPoints");
            soilType = intent.getStringExtra("soilType");
            numFloors = intent.getIntExtra("numFloors", 0);
            seismicRisk = intent.getStringExtra("seismicRisk");
            buildingUse = intent.getStringExtra("buildingUse");
        }
    }

    private void generateRecommendationsWithGemini() {


        textViewRecommendations.setText("Generando recomendaciones...");

        // Construye el prompt para Gemini con todos los datos recolectados
        String prompt = "Actúa como un experto en ingeniería estructural basado en la NSR-10 de Colombia." +
                " Dada la siguiente información para una pequeña edificación (casa):\n" +
                "- Tipo de Suelo: " + soilType + "\n" +
                "- Número de Pisos: " + numFloors + "\n" +
                "- Nivel de Riesgo Sísmico: " + seismicRisk + "\n" +
                "- Uso de la Edificación: " + buildingUse + "\n" +
                // Podrías añadir información del polígono aquí, como el área aproximada
                "- Puntos del polígono (Lat, Lng): " + (polygonPoints != null ? polygonPoints.toString() : "N/A") + "\n\n" +
                "Proporciona recomendaciones preliminares claras y concisas para:\n" +
                "1. **Cimentaciones:** Tipo sugerido y consideraciones clave.\n" +
                "2. **Columnas:** Dimensiones mínimas sugeridas (ej. 25x25cm), espaciamiento y armadura general.\n" +
                "3. **Vigas:** Dimensiones mínimas sugeridas, consideraciones de peralte.\n" +
                "4. **Pórticos:** Recomendaciones sobre el sistema estructural (ej. aporticado, muros de carga), rigidización.\n" +
                "Asegúrate de basar tus respuestas en los principios de la NSR-10 para prefactibilidad. Indica si se requiere un estudio geotécnico detallado.";

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        String simulatedRecommendations =
                                "Basado en la NSR-10 y la información proporcionada:\n\n" +
                                        "1. **Cimentaciones:** Dado un suelo tipo '" + soilType + "' y " + numFloors + " pisos, se recomienda una cimentación superficial, posiblemente zapatas aisladas o corridas, si el suelo lo permite. Si el suelo es blando/expansivo, considerar plateas o pilotes. Es **IMPRESCINDIBLE** un estudio geotécnico detallado para determinar la capacidad portante y el tipo de cimentación definitivo.\n\n" +
                                        "2. **Columnas:** Para una vivienda de " + numFloors + " pisos en zona de riesgo sísmico '" + seismicRisk + "', se sugieren secciones mínimas de 25x25 cm con armadura longitudinal de 4 varillas #5 (5/8 pulgada) y estribos #3 (3/8 pulgada) @10 cm en zonas de confinamiento y @20 cm en zona central. Verificar la cuantía mínima de acero según NSR-10.\n\n" +
                                        "3. **Vigas:** Para un uso '" + buildingUse + "', las vigas de entrepiso podrían tener un peralte mínimo de L/12 a L/14 (donde L es la luz libre). Por ejemplo, para una luz de 4m, una viga de 30x40 cm sería un buen punto de partida. Armadura mínima según NSR-10 Capítulo C.9.\n\n" +
                                        "4. **Pórticos:** Se recomienda un sistema estructural aporticado sismorresistente con pórticos de concreto reforzado. Para zonas de riesgo sísmico '" + seismicRisk + "', se deben diseñar las uniones viga-columna con confinamiento adecuado y considerar la interacción suelo-estructura. Se pueden complementar con muros de mampostería reforzada no estructurales.";
                        textViewRecommendations.setText(simulatedRecommendations);
                    }
                }, 2000); // Retraso para simular llamada a API
    }


    private void generateFloorPlanWithAI() {
        String base64Image = "";
        if (!base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewFloorPlan.setImageBitmap(decodedByte);
        } else {
            imageViewFloorPlan.setImageResource(R.drawable.placeholder_plan);
        }
        Toast.makeText(this, "Plano en planta simulado.", Toast.LENGTH_SHORT).show();
    }

    private void saveProject() {
        // Aquí implementarías la lógica para guardar todos los datos del proyecto
        // (puntos del polígono, respuestas del cuestionario, recomendaciones, etc.)
        // en tu base de datos (Firebase Firestore, SQLite, etc.).
        Toast.makeText(this, "Proyecto guardado (funcionalidad pendiente)", Toast.LENGTH_SHORT).show();

        // Tras guardar, podrías redirigir al usuario a la lista de "Mis Proyectos"
        Intent intent = new Intent(ResultadosActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Limpia el stack para volver al Home
        startActivity(intent);
        finish();
    }
}
