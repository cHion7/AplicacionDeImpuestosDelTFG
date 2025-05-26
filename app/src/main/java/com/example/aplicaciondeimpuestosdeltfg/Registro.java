package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.vistas.MainActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Registro extends AppCompatActivity {
    private TextInputEditText etNombreReg, etUsuarioReg, etContrasenaReg;
    private Button btRegistrarseReg;
    private TextView btVolverLoginReg;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Iniciamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        // Referenciamos los elementos del layout
        etNombreReg = findViewById(R.id.etNombreReg);
        etUsuarioReg = findViewById(R.id.etUsuarioReg);
        etContrasenaReg = findViewById(R.id.etContrasenaReg);
        btRegistrarseReg = findViewById(R.id.btRegistrarseReg);
        btVolverLoginReg = findViewById(R.id.btVolverLoginRegistro);

        //Botón siguiente
        btRegistrarseReg.setOnClickListener(v -> {
            registrarse();
        });

        //Botón volver al Login
        btVolverLoginReg.setOnClickListener(v -> {
            Intent intentAlLogin = new Intent(Registro.this, Login.class);
            startActivity(intentAlLogin);
        });
    }

    public void registrarse(){
        // Obtenemos los datos introducidos por el usuario
        String nombre = etNombreReg.getText().toString().trim();
        String correo = etUsuarioReg.getText().toString().trim();
        String contrasena = etContrasenaReg.getText().toString().trim();

        //Validaciones de campo
        if(nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(Registro.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validación del email
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(Registro.this, "Por favor, ingresa un email válido.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Validación de la contraseña
        if(contrasena.length() < 6){
            Toast.makeText(Registro.this, "La contraseña debe tener al menos 6 carácteres.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear y registrar un nuevo usuario en Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(correo, contrasena)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    //Obtenemos el usuario registrado
                    FirebaseUser usuario = firebaseAuth.getCurrentUser();
                    if (usuario != null){ //registro fue exitoso y el usuario está disponible.
                        // Crear un HashMap para almacenar los datos del usuario
                        HashMap<String, Object> datosUsuario = new HashMap<>();
                        datosUsuario.put("correo", correo);
                        datosUsuario.put("nombre", nombre);

                        //Convertir el email en clave válida para el Firebase (reemplazar carácteres especiales)
                        String emailKey = correo.replace(".", "_").replace("@", "_");

                        //Guardar los datos en la base de datos de Firebase
                        nodoUsuario.child(emailKey).setValue(datosUsuario).addOnCompleteListener(dbTask -> {
                            if(dbTask.isSuccessful()){
                                Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_SHORT).show();

                                // Pasar a la página principal
                                Intent intentLogin = new Intent(Registro.this, Login.class);
                                startActivity(intentLogin);
                                finish();
                            } else {
                                Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                        });
                    }
                }else{
                    Toast.makeText(this, "El correo introducido ya existe.", Toast.LENGTH_SHORT).show();
                }
            });


        /*// Guardamos los datos en SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("correo", correo);
        editor.putString("password", contrasena);
        editor.apply(); // Guardo los cambios*/
    }
}