package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegistroRevision extends AppCompatActivity {
    private EditText etUsuarioRegRev, etContrasenaRegRev;

    private TextInputEditText etNombreRegistroRev, etDNIRegistroRev, etfechaRegistroRev, etEstadoRegistroRev, etDireccionRegistroRev, etTelefonoRegistroRev;
    private Button btModificar1RegistroRev;

    private TextInputEditText etEmpresaRegistroRev, etIngresosBrRegistroRev, etNominaRegistroRev, etDeclaracionRegistroRev;
    private Button btModificar2RegistroRev, btRegistroRegistroRev;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_revision);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesionRevision), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Iniciamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        // Recuperar los datos del sharedPreferences "registro_usuario"
        SharedPreferences sharedPreferences = getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        String correo = sharedPreferences.getString("correo", "");
        String password = sharedPreferences.getString("password", "");
        //Sesión 1
        String nombre = sharedPreferences.getString("nombre", "");
        String dni = sharedPreferences.getString("dni", "");
        String fechaNacimiento = sharedPreferences.getString("fechaNacimiento", "");
        String estadoCivil = sharedPreferences.getString("estadoCivil", "");
        String direccion = sharedPreferences.getString("direccion", "");
        String nacionalidad = sharedPreferences.getString("nacionalidad", "");
        String telefono = sharedPreferences.getString("telefono", "");
        //Sesión 2
        String empresa = sharedPreferences.getString("empresa", "");
        String ingresosBrutos = sharedPreferences.getString("ingresosBrutos", "");
        String nomina = sharedPreferences.getString("nomina", "");
        String declaraciones = sharedPreferences.getString("declaraciones", "");

        // Vincular elementos UI y establecer valores obtenido del sharedPreferences
        etUsuarioRegRev = findViewById(R.id.etUsuarioRegRevision);
        etUsuarioRegRev.setText(correo);
        etContrasenaRegRev = findViewById(R.id.etContrasenaRegRevision);
        etContrasenaRegRev.setText(password);

        etNombreRegistroRev = findViewById(R.id.etNombreRegRevision);
        etNombreRegistroRev.setText(nombre);
        etDNIRegistroRev = findViewById(R.id.etDNIRegRevision);
        etDNIRegistroRev.setText(dni);
        etfechaRegistroRev = findViewById(R.id.etfechaRegRevision);
        etfechaRegistroRev.setText(fechaNacimiento);
        etEstadoRegistroRev = findViewById(R.id.etEstadoRegRevision);
        etEstadoRegistroRev.setText(estadoCivil);
        etDireccionRegistroRev = findViewById(R.id.etDireccionRegRevision);
        etDireccionRegistroRev.setText(direccion);
        etTelefonoRegistroRev = findViewById(R.id.etTelefonoRegRevision);
        etTelefonoRegistroRev.setText(telefono);

        etEmpresaRegistroRev = findViewById(R.id.etEmpresaRegRevision);
        etEmpresaRegistroRev.setText(empresa);
        etIngresosBrRegistroRev = findViewById(R.id.etIngresosBrRegRevision);
        etIngresosBrRegistroRev.setText(ingresosBrutos);
        etNominaRegistroRev = findViewById(R.id.etNominaRegRevision);
        etNominaRegistroRev.setText(nomina);
        etDeclaracionRegistroRev = findViewById(R.id.etDeclaracionRegRevision);
        etDeclaracionRegistroRev.setText(declaraciones);

        btRegistroRegistroRev = findViewById(R.id.btRegistroRevision);

        //Botón de registro
        btRegistroRegistroRev.setOnClickListener(view -> {
            registrarNuevoUsuario(correo, password, nombre, dni, fechaNacimiento, estadoCivil, direccion, telefono, empresa, ingresosBrutos, nomina, declaraciones);
        });

    }

    private void registrarNuevoUsuario(String correo, String password, String nombre, String dni, String fechaNacimiento, String estadoCivil, String direccion, String telefono, String empresa, String ingresosBrutos, String nomina, String declaraciones){
    //Crear y registrar un nuevo usuario en Firebase Authentication
    firebaseAuth.createUserWithEmailAndPassword(correo, password)
            //Se ejecuta cuando el intento de registro termina
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    //Obtenemos el usuario registrado
                    FirebaseUser usuario = firebaseAuth.getCurrentUser();
                    if (usuario != null) { //registro fue exitoso y el usuario está disponible.
                        // Crear un HashMap para almacenar los datos del usuario
                        HashMap<String, Object> datosUsuario = new HashMap<>();
                        datosUsuario.put("correo", correo);
                        datosUsuario.put("nombre", nombre);
                        datosUsuario.put("dni", dni);
                        datosUsuario.put("fechaNacimiento", fechaNacimiento);
                        datosUsuario.put("estadoCivil", estadoCivil);
                        datosUsuario.put("direccion", direccion);
                        datosUsuario.put("telefono", telefono);
                        datosUsuario.put("password", password);

                        datosUsuario.put("empresa", empresa);
                        datosUsuario.put("ingresosBrutos", ingresosBrutos);
                        datosUsuario.put("nomina", nomina);
                        datosUsuario.put("declaraciones", declaraciones);

                        // Convertir el email en clave válida para Firebase (reemplaza caracteres especiales)
                        String emailKey = correo.replace(".", "_").replace("@", "_");

                        // Guardar los datos en la base de datos Firebase
                        nodoUsuario.child(emailKey).setValue(datosUsuario).addOnCompleteListener(dbTask ->{

                            if (dbTask.isSuccessful()) { //Escritura
                                Toast.makeText(this, "Datos guardados corectamente.", Toast.LENGTH_LONG).show();

                                //Redirigir al login
                                Intent intentAlLogin = new Intent(RegistroRevision.this, Login.class);
                                startActivity(intentAlLogin);
                                finish();
                            } else {
                                Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "Error: el correo introducido ya existe", Toast.LENGTH_SHORT).show();
                }
            });
    }
}