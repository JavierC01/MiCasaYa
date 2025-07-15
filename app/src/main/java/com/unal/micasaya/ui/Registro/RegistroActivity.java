package com.unal.micasaya.ui.Registro;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.DropBoxManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.BreakIterator;
import java.util.HashMap;
import java.util.Map;
import com.unal.micasaya.R;
import com.unal.micasaya.ui.Login.LoginActivity;

public class RegistroActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText registroName;
    private EditText registroemail;
    private EditText registropassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        registroemail = findViewById(R.id.editTextEmail);
        registropassword = findViewById(R.id.editTextPassword);
        registroName = findViewById(R.id.editTextName);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        // ...
    }

    private void registerUser() {
        String email = registroemail.getText().toString().trim();
        String password = registropassword.getText().toString().trim();
        String displayName = registroName.getText().toString().trim(); // Asumiendo que agregaste este campo

        // Validaciones de campos (vacíos, contraseñas coinciden)

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("email", email);
                            userData.put("displayName", displayName);
                            userData.put("createdAt", com.google.firebase.firestore.FieldValue.serverTimestamp());

                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(RegistroActivity.this, "Registro exitoso.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class); // O HomeActivity
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(RegistroActivity.this, "Error al guardar datos de usuario: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                        // Aún así, el usuario fue creado en Auth, podrías redirigir.
                                        Intent intent = new Intent(RegistroActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    });
                        }
                    } else {
                        Toast.makeText(RegistroActivity.this, "Error en el registro: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}
