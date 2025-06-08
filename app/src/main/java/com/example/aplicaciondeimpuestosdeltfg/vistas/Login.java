package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.database.ValueEventListener;


public class Login extends AppCompatActivity {
    private TextInputEditText etUsuarioLogin, etContrasenaLogin;
    private Button btIniciarSesionlogin;
    private TextView tvRegistrarseLogin, tvRecuperarContrasenaLogin;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private SignInButton googleSignInButton; // Botón para iniciar sesión con Google
    private ActivityResultLauncher<Intent> signInResultLauncher; // Lanzador de actividad para Google Sign-In

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        // Ajuste de la ventana para que se adapte a las barras del sistema (por ejemplo, la barra de estado)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.sesion1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Inicialización de las vistas
        etUsuarioLogin = findViewById(R.id.etUsuarioLogin);
        etContrasenaLogin = findViewById(R.id.etContrasenaLogin);
        btIniciarSesionlogin = findViewById(R.id.btIniciarSesionLogin);
        tvRegistrarseLogin = findViewById(R.id.btRegistroLogin);
        tvRecuperarContrasenaLogin = findViewById(R.id.tvRecuperarContrasena);
        googleSignInButton = findViewById(R.id.btGoogle);
        googleSignInButton.setSize(SignInButton.SIZE_STANDARD);

        //Inicialización de la autentificación
        firebaseAuth = FirebaseAuth.getInstance();

        // Redirección a la pantalla de registro
        tvRegistrarseLogin.setOnClickListener(view -> {
            Intent intentRegistro = new Intent(Login.this, Registro.class);
            startActivity(intentRegistro);
        });

        // Redirección a la pantalla de recuperar contraseña
        tvRecuperarContrasenaLogin.setOnClickListener(view -> {
            Intent intentRecuperarContrasena = new Intent(Login.this, RecuperarContrasena.class);
            startActivity(intentRecuperarContrasena);
        });

        //Configuración de los listeners para los botones
        btIniciarSesionlogin.setOnClickListener(view ->
                signInWithEmail());

        googleSignInButton.setOnClickListener(view -> {
            signInWithGoogle();
        });

        //Resultado de la actividad de Google Sign-In
        signInResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent datos = result.getData();
                    if(datos != null){
                        GoogleSignIn.getSignedInAccountFromIntent(datos).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Google sign in exitoso.", Toast.LENGTH_SHORT).show();
                                firebaseAuthWithGoogle(task.getResult()); // Autenticación con Google
                            } else {
                                Toast.makeText(this, "Error en Google sign in.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    Toast.makeText(this, "Error en iniciar sesión con Google por el dato", Toast.LENGTH_SHORT).show();
                }
            }
        );
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
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
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

    // Método para iniciar sesión con Google
    private void signInWithGoogle(){
        // Configuración de Google Sign-In (está deprecado porque la mayoría de los móviles no soportan la nueva versión): pide el ID de cliente web y el email.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)) // Se especifica el ID de cliente web
                .requestEmail() // Solicita el email
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);
        mGoogleSignInClient.signOut(); //Si es necesario - Desconecta previamente al usuario de Google
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        signInResultLauncher.launch(signInIntent); // Lanza el intent de Google Sign-In
    }

    // Método para autentificar al usuario con Google en Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount cuenta){
        //Crea una credencial de autentificación con el ID de token de Google
        AuthCredential credenciales = GoogleAuthProvider.getCredential(cuenta.getIdToken(), null);
        firebaseAuth.signInWithCredential(credenciales)
                .addOnCompleteListener(this, tarea -> {
                   //Obtiene el usuario autneticado con Google en Firebase
                   if (tarea.isSuccessful()){
                       FirebaseUser usuario = firebaseAuth.getCurrentUser();
                       if (usuario != null) {
                           String email = usuario.getEmail();
                           String emailkey = email.replace("@", "_").replace(".", "_");
                           DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios").child(emailkey);
                           databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                               // Asigna los primeros datos al iniciar sesión con Google Firebase
                               @Override
                               public void onDataChange(@NonNull DataSnapshot snapshot) {
                                   if (!snapshot.exists()) {
                                       databaseReference.child("nombre").setValue(usuario.getDisplayName());
                                       databaseReference.child("correo").setValue(usuario.getEmail());
                                   }
                                   // Guarda los datos del usuario en SharedPreferences
                                   SharedPreferences preferences = getSharedPreferences("UserProfile", MODE_PRIVATE);
                                   SharedPreferences.Editor editor = preferences.edit();
                                   editor.putString("correo", email);
                                   editor.putString("nombre", usuario.getDisplayName());
                                   editor.apply();
                               }
                               @Override
                               public void onCancelled(@NonNull DatabaseError error) {
                                   Log.e("Database", "Error al acceder a la base de datos", error.toException());
                               }
                           });
                           //Inicia la actividad principal y cierra la pantalla de login
                           Intent GoogleirAlMain = new Intent(Login.this, MainActivity.class);
                           startActivity(GoogleirAlMain);
                           finish();
                           Toast.makeText(this, "Inicio de sesión exitoso con Google", Toast.LENGTH_SHORT).show();
                       }
                   } else{
                       Toast.makeText(this, "Error al autentificar con Firebase.", Toast.LENGTH_SHORT).show();
                   }
                });
    }
}