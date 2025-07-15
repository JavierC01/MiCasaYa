package com.unal.micasaya.ui.Login;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.unal.micasaya.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.unal.micasaya.ui.Home.HomeActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText loginemail;
    private EditText loginpassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginemail = findViewById(R.id.editTextLoginEmail);
        loginpassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    performLogin();
                }
            }
        });

    }

    private boolean validateInput() {
        String email = loginemail.getText().toString().trim();
        String password = loginpassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginemail.setError("El correo es requerido");
            loginemail.requestFocus();
            return false;
        }
        // Puedes añadir validación de formato de email aquí

        if (password.isEmpty()) {
            loginpassword.setError("La contraseña es requerida");
            loginpassword.requestFocus();
            return false;
        }
        return true;
    }

    private void performLogin() {
        String email = loginemail.getText().toString().trim();
        String password = loginpassword.getText().toString().trim();


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // FirebaseUser user = mAuth.getCurrentUser(); // Puedes obtener el usuario si lo necesitas
                        Toast.makeText(LoginActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el stack
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Error de autenticación: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
