package com.unal.micasaya.ui.Home;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.MainActivity;
import com.unal.micasaya.ui.Map.MapaActivity;
import com.unal.micasaya.ui.MisProyectos.MisProyectosActivity;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeMessage;
    private Button buttonCreateProject, buttonMyProjects, buttonMaterials, buttonLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        welcomeMessage = findViewById(R.id.welcomeMessage);
        buttonCreateProject = findViewById(R.id.buttonCreateProject);
        buttonMyProjects = findViewById(R.id.buttonMyProjects);
        buttonMaterials = findViewById(R.id.buttonMaterials);
        buttonLogout = findViewById(R.id.buttonLogout);

        // Puedes personalizar el mensaje de bienvenida aquí, quizás obteniendo el nombre del usuario de la sesión

        welcomeMessage.setText("Bienvenido, de nuevo!");

        buttonCreateProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MapaActivity.class);
                startActivity(intent);
            }
        });

        buttonMyProjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, MisProyectosActivity.class);
                startActivity(intent);
            }
        });

        buttonMaterials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "Abriendo sección de Materiales (aún no implementada)", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MaterialsActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase Auth; FirebaseAuth.getInstance().signOut();
                Toast.makeText(HomeActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Borra el stack de actividades
                startActivity(intent);
                finish();
            }
        });
    }
}
