package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private TextInputEditText etUsuarioLogin, etContrasenaLogin;
    private Button btIniciarSesionlogin;
    private TextView tvRegistrarse;

    private FirebaseAuth firebaseAuth; // Instancia de FirebaseAuth para la autenticación

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialización de las vistas
        etUsuarioLogin = findViewById(R.id.etUsuarioLogin);
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin);
        btIniciarSesionlogin = findViewById(R.id.btIniciarSesionLogin);
        tvRegistrarse = findViewById(R.id.btRegistroLogin);

        //Inicialización de la autentificación
        firebaseAuth = FirebaseAuth.getInstance();

        //Configuración de los listeners para los botones
        btIniciarSesionlogin.setOnClickListener(view -> signInWithEmail());

        // Redirección a la pantalla de registro
        tvRegistrarse.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Registro.class);
            startActivity(intent);
        });
    }

    //Método para iniciar sesión con email y contraseña
    private void signInWithEmail() {
        String email = etUsuarioLogin.getText().toString().trim();
        String password = etContrasenaLogin.getText().toString().trim();

        //Verificación de campos vacíos
        if(email.isEmpty()||password.isEmpty()){
            Toast.makeText(Login.this, "Por favor, ingresa un email y contraseña.", Toast.LENGTH_SHORT).show();
        }
        //Validación del email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(Login.this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
        }
        //Validación de formato de la contraseña
        if(password.length() < 6){
            Toast.makeText(Login.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
        }

        //Iniciar sesión con email y password
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Toast.makeText(Login.this, "Inicio de sesión exitoso. ", Toast.LENGTH_SHORT).show();
                //Redirige a la página principal
                Intent intentAlMainPage = new Intent(Login.this, MainActivity.class);
                startActivity(intentAlMainPage);
            }else{
                Toast.makeText(Login.this, "Error: Usuario o contraseña introducidos incorrectos.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}