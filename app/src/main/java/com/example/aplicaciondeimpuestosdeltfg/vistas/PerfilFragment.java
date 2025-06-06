package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.aplicaciondeimpuestosdeltfg.Login;
import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.VerImpuestos;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PerfilFragment extends Fragment {
    private TextInputEditText etNombreApellidoPerfil, etUsuarioPerfil, etSaldoInicialPerfil;
    private Button btCambiarContrasenaPerfil, btCerrarSesion, btAnadriImpuestosPerfil;
    private ImageButton btAjustesPerfil;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;
    private FirebaseUser usuarioActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");
        usuarioActual = firebaseAuth.getCurrentUser();

        //Referenciamos los layouts
        etNombreApellidoPerfil = view.findViewById(R.id.etNombreApPerf);
        etUsuarioPerfil = view.findViewById(R.id.etCorreoPerf);
        etUsuarioPerfil.setEnabled(false);
        //etSaldoInicialPerfil = view.findViewById(R.id.etSaldoInicialReg);
        btAjustesPerfil = view.findViewById(R.id.btnAjustes);
        btCambiarContrasenaPerfil = view.findViewById(R.id.btCambiarContrasenaPerf);
        btAnadriImpuestosPerfil = view.findViewById(R.id.anadirImpuestos);
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);

        //Cargar datos del usuario si está autenticado
        if (usuarioActual != null) {
            cargarDatosUsuario();
        } else {
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        //Imagen botón
        btAjustesPerfil.setOnClickListener(v ->{
            Intent irADatosImpuestos = new Intent(getActivity(), VerImpuestos.class);
            startActivity(irADatosImpuestos);
        });

        //Botón cambiar contraseña
        btCambiarContrasenaPerfil.setOnClickListener(v -> {
            cambiarContrasena();
        });
        //Botón Preguntas Impuestos
        btAnadriImpuestosPerfil.setOnClickListener(v -> {
            Intent intentUsuarios = new Intent(getActivity(), preguntasComunes.class);
            startActivity(intentUsuarios);
        });

        //Botón de cerrar sessión
        btCerrarSesion.setOnClickListener(v -> {
            cerrarSesion();
        });
        return view;
    }

    //Cambiar Contraseña
    public void cambiarContrasena(){
        if(usuarioActual == null) {
            Toast.makeText(getActivity(), "No hay usuario autenticado para cambiar la contraseña.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder alerta = new AlertDialog.Builder(getActivity());
        alerta.setTitle("Cambiar Contraseña");
        alerta.setMessage("Por favor, introduce tus contraseñas");

        // Creamos un EditText (dentro de LinearLayout porque sino no contiene todos) para que el usuario escriba la nueva contraseña
        LinearLayout layoutContenedor = new LinearLayout(getActivity());
        layoutContenedor.setOrientation(LinearLayout.VERTICAL);

        EditText inputContrasenaActual = new EditText(getActivity());
        EditText inputNuevaContrasena = new EditText(getActivity());
        EditText inputConfirmarContrasena = new EditText(getActivity());

        inputContrasenaActual.setHint("Contraseña actual");
        inputNuevaContrasena.setHint("Nueva contraseña");
        inputConfirmarContrasena.setHint("Nueva contraseña");
        inputContrasenaActual.setInputType(InputType.TYPE_CLASS_TEXT| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        inputNuevaContrasena.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_NUMBER_VARIATION_PASSWORD);
        inputConfirmarContrasena.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        //Añadir datos a la alerta
        layoutContenedor.addView(inputContrasenaActual);
        layoutContenedor.addView(inputNuevaContrasena);
        layoutContenedor.addView(inputConfirmarContrasena);
        alerta.setView(layoutContenedor);

        alerta.setPositiveButton("Guardar", (dialog, which) -> {
            String contrasenaActual = inputContrasenaActual.getText().toString().trim();
            String nuevaContrasena = inputNuevaContrasena.getText().toString().trim();
            String confirmarContrasena = inputConfirmarContrasena.getText().toString();

            if (contrasenaActual.isEmpty() || nuevaContrasena.isEmpty() || confirmarContrasena.isEmpty()) {
                Toast.makeText(getActivity(), "La campos de contraseñas no puede estar vacía.", Toast.LENGTH_SHORT).show();
                return; // Salimos del método si la contraseña está vacía
            }

            if (nuevaContrasena.length() <= 6) {
                Toast.makeText(getActivity(), "La contraeña debe tener al menos 6 carácteres.", Toast.LENGTH_SHORT).show();
                return; // Salimos del método si la contraseña es demasiado corta
            }
            if (!nuevaContrasena.equals(confirmarContrasena)) {
                Toast.makeText(getActivity(), "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Reautenticar al usuario
            AuthCredential credencial = EmailAuthProvider.getCredential(usuarioActual.getEmail(), contrasenaActual);

            //Auntenticamos la contraseña en Firebase
            usuarioActual.reauthenticate(credencial)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getActivity(), "Contraseña actual verificada.", Toast.LENGTH_SHORT).show();
                        //Intentamos actualizar la contraseña en Firebase
                        usuarioActual.updatePassword(nuevaContrasena)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Toast.makeText(getActivity(), "Contraseña actualizada correctamente.", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(getActivity(), "Error al cambiar la contraseña.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }else{
                        Toast.makeText(getActivity(), "Error al verificar la contraseña actual.", Toast.LENGTH_SHORT).show();
                    }
                });
        });

        //Botón de cancelar
        alerta.setNegativeButton("Cancelar", (dialog, which) -> {
           dialog.cancel();
        });
        alerta.show();
    }

    //Cerrar Sesión
    public void cerrarSesion() {
        Toast.makeText(getActivity(), "Cerrando sesión...", Toast.LENGTH_SHORT).show();
        Intent intentAlLogin = new Intent(getActivity(), Login.class);
        startActivity(intentAlLogin);
        firebaseAuth.signOut();
        Toast.makeText(getActivity(), "Sesión Cerrada", Toast.LENGTH_SHORT).show();
        getActivity().finish();
    }

    // Cargar los datos del usuario desde Firebase y los muestra en la interfaz
    private void cargarDatosUsuario(){
        String emailUsuario = usuarioActual.getEmail();
        //Si el email no está vacío
        if (emailUsuario != null) {
            String usuarioClave = emailUsuario.replace("@", "_").replace(".", "_");
            DatabaseReference usuarioReferenciado = nodoUsuario.child(usuarioClave);

            //Solo queremos obtener los datos una vez (sin escuchar cambios en tiempo real)
            usuarioReferenciado.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //Si existe: obtenemos valores del firebae
                    if (snapshot.exists()) {
                        String nombre = snapshot.child("nombre").getValue(String.class);
                        String email = snapshot.child("correo").getValue(String.class);

                        //Si el valor es null, se muestra un mensaje predeterminado
                        etNombreApellidoPerfil.setText(nombre != null ? nombre : "Nombre no disponible");
                        etUsuarioPerfil.setText(email != null ? email : "Email no disponible");
                    } else {
                        Toast.makeText(getActivity(), "No se encontraron datos", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(), "Error al obtener los datos", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}