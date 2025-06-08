package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class Registro extends AppCompatActivity {
    private TextInputEditText etNombreReg, etUsuarioReg, etContrasenaReg, etSaldoInicial;
    private TextView btVolverLoginReg, tvNivelSeguridad;
    private Button btRegistrarseReg;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesionRegistro), (v, insets) -> {
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
        tvNivelSeguridad = findViewById(R.id.txtNivelSeguridad);
        etContrasenaReg = findViewById(R.id.etContrasenaReg);
        etSaldoInicial = findViewById(R.id.etSaldoInicialReg);
        btRegistrarseReg = findViewById(R.id.btRegistrarseReg);
        btVolverLoginReg = findViewById(R.id.btVolverLoginRegistro);

        // 3 Método (se generan "solo") TextWatcher al campo de la contraseña para verificar su fuerza en tiempo real
        etContrasenaReg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                comprobarNivelSeguridadContrasena(s.toString());
            }
        });
        //Botón registrarse
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
        String saldoInicialStr = etSaldoInicial.getText().toString().trim();
        Double saldoInicial;
        long fechaInicioDelDia;

        //Validaciones de campo
        if(nombre.isEmpty() || correo.isEmpty() || contrasena.isEmpty()){
            Toast.makeText(Registro.this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (saldoInicialStr.isEmpty()) {
            fechaInicioDelDia = 0;
            saldoInicial= 0.0;
        }else {
            saldoInicial = Double.parseDouble(saldoInicialStr);
            long currentTimeMillis = System.currentTimeMillis();
            long fechaOriginal = currentTimeMillis;

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(fechaOriginal);

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            fechaInicioDelDia = calendar.getTimeInMillis();
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
                        datosUsuario.put("saldoInicial", saldoInicial);
                        datosUsuario.put("tiempoSaldoInicial", fechaInicioDelDia);

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
    }

    //Nivel de seguridad
    public void comprobarNivelSeguridadContrasena(String contrasena){
        //Valores Predeterminado
        int puntos = 0;
        String textoMostrado = "";
        int color = Color.GRAY;

        //Si la contraseña es demasiado corta, se considera débil
        if(contrasena.length()<6){
            textoMostrado = "Débil (mínimo 6 carácteres)";
            color = getResources().getColor(R.color.grafica_rojo, null);
        }else{
            // Verifica la presencia de diferentes tipos de caracteres para aumentar la fuerza
            if (contrasena.matches(".*[a-z].*")) puntos++; // Letras minúsculas
            if (contrasena.matches(".*[A-Z].*")) puntos++; // Letras mayúsculas
            if (contrasena.matches(".*[0-9].*")) puntos++; // Dígitos
            if (contrasena.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?].*")) puntos++; // Caracteres especiales

            // Asigna el texto y color según la fuerza acumulada
            if (puntos < 2) {
                textoMostrado = "Nivel de seguridad de la contraseña débil. Por favor, añade carácteres especiales, mayúsculas o números.";
                color = getResources().getColor(R.color.rojo, null);
            } else if (puntos < 4) {
                textoMostrado = "Nivel de seguridad de la contraseña media. Por favor, añade carácteres especiales, mayúsculas o números.";
                color = getResources().getColor(R.color.verde, null);
            }
        }
        // Actualiza el texto y el color del TextView que muestra el nivel de seguridad
        tvNivelSeguridad.setText(textoMostrado);
        tvNivelSeguridad.setTextColor(color);
    }
}