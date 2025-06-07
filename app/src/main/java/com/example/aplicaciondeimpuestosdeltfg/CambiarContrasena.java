package com.example.aplicaciondeimpuestosdeltfg;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CambiarContrasena extends AppCompatActivity {
    private EditText edtContrasenaActual, edtNuevaContrasena, edtConfirmarContrasena;
    private Button btnCambiarContrasena;

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseUser usuarioActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cambiar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainCambiarContrasena), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        usuarioActual = mAuth.getCurrentUser();

        edtContrasenaActual = findViewById(R.id.etContrasenaActual);
        edtNuevaContrasena = findViewById(R.id.etNuevaContrasena);
        edtConfirmarContrasena = findViewById(R.id.etConfirmarContrasena);
        btnCambiarContrasena = findViewById(R.id.btnCambiarContrasena);

        btnCambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String contrasenaActual = edtContrasenaActual.getText().toString().trim();
                String nuevaContrasena = edtNuevaContrasena.getText().toString().trim();
                String confirmarContrasena = edtConfirmarContrasena.getText().toString().trim();

                if (TextUtils.isEmpty(contrasenaActual) || TextUtils.isEmpty(nuevaContrasena) || TextUtils.isEmpty(confirmarContrasena)) {
                    Toast.makeText(CambiarContrasena.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!nuevaContrasena.equals(confirmarContrasena)) {
                    Toast.makeText(CambiarContrasena.this, "Las nuevas contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (nuevaContrasena.length() < 6) {
                    Toast.makeText(CambiarContrasena.this, "La nueva contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Probar si el usuario actual está logeado
                if (usuarioActual != null && usuarioActual.getEmail() != null) {
                    // Volver a iniciar sesión con credenciales actuales
                    AuthCredential credential = EmailAuthProvider.getCredential(usuarioActual.getEmail(), contrasenaActual);

                    usuarioActual.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // Actualizado la contraseña
                                        usuarioActual.updatePassword(nuevaContrasena)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(CambiarContrasena.this, "Contraseña cambiada correctamente.", Toast.LENGTH_SHORT).show();
                                                            finish(); // Close the activity
                                                        } else {
                                                            Toast.makeText(CambiarContrasena.this, "Error al cambiar la contraseña: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(CambiarContrasena.this, "Error de autenticación. Verifica tu contraseña actual: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                } else {
                    // No user logged in or email is null, handle this case (e.g., redirect to login)
                    Toast.makeText(CambiarContrasena.this, "No hay usuario autenticado o el email no está disponible.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}