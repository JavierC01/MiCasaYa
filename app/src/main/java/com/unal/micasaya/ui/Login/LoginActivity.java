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
    // ... UI elements ...

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        loginemail = findViewById(R.id.editTextEmail);
        loginpassword = findViewById(R.id.editTextPassword);
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
