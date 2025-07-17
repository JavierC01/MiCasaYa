package com.unal.micasaya.ui.Resultados;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log; // Cambiado para usar android.util.Log
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Ejemplo, si añades EditTexts
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.Resultados.Project; // ¡IMPORTANTE! Asegúrate de que esta clase exista y esté correcta
import com.unal.micasaya.ui.Home.HomeActivity;
import com.unal.micasaya.ui.MisProyectos.MisProyectosActivity; // Importar si navegas aquí

import java.util.ArrayList;
import java.util.HashMap; // ¡IMPORTANTE!
import java.util.List;
import java.util.Map;

public class ResultadosActivity extends AppCompatActivity {

    private static final String TAG = "ResultadosActivity"; // ¡IMPORTANTE!

    private TextView textViewRecommendations;
    private ImageView imageViewFloorPlan;
    private Button buttonSaveProject;
    // private EditText editTextProjectName; // Ejemplo
    // private EditText editTextProjectDescription; // Ejemplo

    // private List<LatLng> confirmedPolygonPoints; // Ya no se usa directamente si usas polygonPoints
    private FirebaseFirestore db;
    private ArrayList<LatLng> polygonPoints; // Esto se carga desde el Intent
    private String soilType;
    private int numFloors;
    private String seismicRisk;
    private String buildingUse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
        setContentView(R.layout.activity_resultados);

        textViewRecommendations = findViewById(R.id.textViewRecommendations);
        imageViewFloorPlan = findViewById(R.id.imageViewFloorPlan);
        buttonSaveProject = findViewById(R.id.buttonSaveProject);
        // editTextProjectName = findViewById(R.id.editTextProjectName); // Ejemplo
        // editTextProjectDescription = findViewById(R.id.editTextProjectDescription); // Ejemplo


        getProjectDataFromIntent();
        generateRecommendationsWithGemini();
        generateFloorPlanWithAI();

        buttonSaveProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Necesitas obtener el nombre y descripción del proyecto
                // String name = editTextProjectName.getText().toString(); // Ejemplo
                // String description = editTextProjectDescription.getText().toString(); // Ejemplo

                // ** VALORES TEMPORALES - DEBES OBTENERLOS DE LA UI O DEL INTENT SI CORRESPONDE **
                String projectNameFromSomewhere = "Proyecto " + System.currentTimeMillis();
                String projectDescriptionFromSomewhere = "Descripción para " + projectNameFromSomewhere;
                if (buildingUse != null && !buildingUse.isEmpty()) {
                    projectDescriptionFromSomewhere += " - Uso: " + buildingUse;
                }


                saveProjectToFirestore(projectNameFromSomewhere, projectDescriptionFromSomewhere);
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

            // Podrías inicializar confirmedPolygonPoints aquí si lo necesitas en otro lugar,
            // pero para guardar, es más directo usar polygonPoints.
            // if (polygonPoints != null) {
            // confirmedPolygonPoints = new ArrayList<>(polygonPoints);
            // }
        }
    }

    // ... generateRecommendationsWithGemini() y generateFloorPlanWithAI() sin cambios ...
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
        String base64Image = ""; // Esto simula que no hay imagen
        if (!base64Image.isEmpty()) {
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageViewFloorPlan.setImageBitmap(decodedByte);
        } else {
            // Asegúrate de que tienes un drawable llamado 'placeholder_plan' en res/drawable
            imageViewFloorPlan.setImageResource(R.drawable.placeholder_plan);
        }
        Toast.makeText(this, "Plano en planta simulado.", Toast.LENGTH_SHORT).show();
    }


    private void saveProjectToFirestore(String projectName, String projectDescription) {
        if (projectName == null || projectName.trim().isEmpty()) {
            Toast.makeText(this, "El nombre del proyecto no puede estar vacío.", Toast.LENGTH_SHORT).show();
            return;
        }

        List<Map<String, Double>> firestorePoints = new ArrayList<>();
        if (polygonPoints != null) { // Usar polygonPoints que viene del Intent
            for (com.google.android.gms.maps.model.LatLng point : polygonPoints) {
                Map<String, Double> pointMap = new HashMap<>();
                pointMap.put("latitude", point.latitude);
                pointMap.put("longitude", point.longitude);
                firestorePoints.add(pointMap);
            }
        }

        // Crear el objeto Project (asegúrate de que la clase Project exista y tenga los campos correctos)
        // También puedes añadir los otros campos como soilType, numFloors, etc., a tu clase Project
        // y pasarlos al constructor o mediante setters.
        Project newProject = new Project(projectName, projectDescription, firestorePoints);
        // Ejemplo si Project tiene más campos:
        // newProject.setSoilType(soilType);
        // newProject.setNumFloors(numFloors);
        // ... etc.

        db.collection("projects")
                .add(newProject)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                    Toast.makeText(ResultadosActivity.this, "Proyecto '" + projectName + "' guardado.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(ResultadosActivity.this, MisProyectosActivity.class); // O HomeActivity, según prefieras
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error adding document", e);
                    Toast.makeText(ResultadosActivity.this, "Error al guardar proyecto: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}