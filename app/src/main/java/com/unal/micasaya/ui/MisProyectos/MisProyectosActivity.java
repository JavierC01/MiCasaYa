package com.unal.micasaya.ui.MisProyectos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.unal.micasaya.R;
import com.unal.micasaya.ui.Map.MapaActivity;

import java.util.ArrayList;
import java.util.List;

public class MisProyectosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewProjects;
    private TextView emptyProjectsMessage;
    private Button buttonCreateProjectFromEmpty;

    // Aquí almacenarías tus proyectos, quizás de una base de datos
    private List<String> projectsList = new ArrayList<>(); // Ejemplo: lista de nombres de proyectos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_proyectos);

        recyclerViewProjects = findViewById(R.id.recyclerViewProjects);
        emptyProjectsMessage = findViewById(R.id.emptyProjectsMessage);
        buttonCreateProjectFromEmpty = findViewById(R.id.buttonCreateProjectFromEmpty);

        // Simular algunos proyectos (o cargarlos de una base de datos)
        // projectsList.add("Casa de Campo - Proyecto 1");
        // projectsList.add("Remodelación Urb. XYZ - Proyecto 2");

        checkProjectsAndDisplay();

        buttonCreateProjectFromEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MisProyectosActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkProjectsAndDisplay() {
        if (projectsList.isEmpty()) {
            recyclerViewProjects.setVisibility(View.GONE);
            emptyProjectsMessage.setVisibility(View.VISIBLE);
            buttonCreateProjectFromEmpty.setVisibility(View.VISIBLE);
        } else {
            recyclerViewProjects.setVisibility(View.VISIBLE);
            emptyProjectsMessage.setVisibility(View.GONE);
            buttonCreateProjectFromEmpty.setVisibility(View.GONE);
            // Aquí deberías inicializar y configurar tu RecyclerView Adapter
            // Ejemplo: ProjectAdapter adapter = new ProjectAdapter(projectsList);
            // recyclerViewProjects.setAdapter(adapter);
        }
    }
}