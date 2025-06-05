package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.aplicaciondeimpuestosdeltfg.Login;
import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.Registro1;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PerfilFragment extends Fragment {
    private TextInputEditText etNombreApellidoPerfil, etUsuarioPerfil;
    private Button btCambiarContrasenaPerfil, btCerrarSesion, detectarUsuario, btanadirDatos;
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
        btanadirDatos = view.findViewById(R.id.btanadirDatosPerfil);
        detectarUsuario = view.findViewById(R.id.btDetectarUsuarios);
        etNombreApellidoPerfil = view.findViewById(R.id.etNombreApPerf);
        etUsuarioPerfil = view.findViewById(R.id.etCorreoPerf);
        etUsuarioPerfil.setEnabled(false);
        btCambiarContrasenaPerfil = view.findViewById(R.id.btCambiarContrasenaPerf);
        btCerrarSesion = view.findViewById(R.id.btCerrarSesion);

        //Cargar datos del usuario si está autenticado
        if (usuarioActual != null) {
            cargarDatosUsuario();
        } else {
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        //Botón añadir datos
        btanadirDatos.setOnClickListener(v -> {
            addDatos();
        });

        //Botón cambiar contraseña
        btCambiarContrasenaPerfil.setOnClickListener(v -> {
           // cambiarContrasena();
        });
        detectarUsuario.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), afinarUsuario.class);
            startActivity(intent);
        });

        //Botón de cerrar sessión
        btCerrarSesion.setOnClickListener(v -> {
            cerrarSesion();
        });
        return view;
    }


    public void addDatos() {
        Intent intentReg1 = new Intent(getActivity(), Registro1.class);
        startActivity(intentReg1);
    }

    /*Cambiar Contraseña
    public void cambiarContrasena(){

    }*/
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