package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class RecuperarContrasena extends AppCompatActivity {
    private TextInputEditText etEmailRecuperar;
    private Button btnRecuperarContrasena;
    private TextView tvVolverLogin;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recuperar_contrasena);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.recuperarContrasena), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Inicializar vistas
        etEmailRecuperar = findViewById(R.id.etEmailRecuperar);
        btnRecuperarContrasena = findViewById(R.id.btnRecuperarContrasena);
        tvVolverLogin = findViewById(R.id.tvVolverLogin);

        // Inicializar Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configurar Listener para el botón de recuperar contraseña
        btnRecuperarContrasena.setOnClickListener(view -> {
            vertificarYenviarCorreoRestablecimiento();
        });

        // Configurar Listener para el texto "Volver al inicio de sesión"
        tvVolverLogin.setOnClickListener( v -> {
            // Redirigir a la actividad de Login
            Intent intent = new Intent(RecuperarContrasena.this, Login.class);
            startActivity(intent);
            finish(); // Finaliza esta actividad para que el usuario no pueda volver atrás con el botón de retroceso
        });
    }

// Método para verificar y enviar el correo electrónico de restablecimiento de contraseña.
private void vertificarYenviarCorreoRestablecimiento() {
    String email = etEmailRecuperar.getText().toString().trim();

    // Validar si el campo de correo electrónico está vacío
    if (TextUtils.isEmpty(email)) {
        etEmailRecuperar.setError("Es obligatorio completar el ccorreo electrónico.");
        etEmailRecuperar.requestFocus();
        return;
    }

    // Validar formato del correo electrónico
    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        etEmailRecuperar.setError("Ingresa un correo electrónico válido.");
        etEmailRecuperar.requestFocus();
        return;
    }

    // Deshabilitar el botón para evitar múltiples clics mientras se procesa
    btnRecuperarContrasena.setEnabled(false);
    Toast.makeText(this, "Enviando instrucciones......", Toast.LENGTH_SHORT).show();

    // Enviar el correo de restablecimiento de contraseña
    mAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Por seguridad no debemos indicar si el correo existía o no.
                    Toast.makeText(RecuperarContrasena.this,
                            "Si el correo electrónico está registrado, recibirás un enlace de restablecimiento.",
                            Toast.LENGTH_LONG).show();
                    // Redirigir al Login después de un restablecimiento exitoso
                    Intent intent = new Intent(RecuperarContrasena.this, Login.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Manejo de errores específicos
                    if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(RecuperarContrasena.this,
                                "El correo electrónico introducido no está registrado.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // Otros errores
                        String errorMessage = "Error al enviar el correo. Por favor, intenta de nuevo.";
                        if (task.getException() != null && task.getException().getMessage() != null) {
                            // Puedes loggear el error completo para depuración: Log.e("Recuperar", "Error: " + task.getException().getMessage());
                            errorMessage += "\nDetalles: " + task.getException().getMessage();
                        }
                        Toast.makeText(RecuperarContrasena.this,
                                errorMessage,
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
}
}