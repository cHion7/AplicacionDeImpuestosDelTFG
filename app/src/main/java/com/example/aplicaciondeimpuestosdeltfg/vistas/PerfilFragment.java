package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplicaciondeimpuestosdeltfg.Login;
import com.example.aplicaciondeimpuestosdeltfg.R;
//import com.example.aplicaciondeimpuestosdeltfg.VerImpuestos;
import com.example.aplicaciondeimpuestosdeltfg.informacionAdicional;
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

import java.io.ByteArrayOutputStream;


public class PerfilFragment extends Fragment {
    // Constantes para manejo de permisos y cámara
    private static final int REQUEST_PERMISSION = 1;
    private static final int CAMERA_REQUEST_CODE = 100;

    private TextInputEditText etNombreApellidoPerfil, etUsuarioPerfil, etSaldoInicialPerfil;
    private Button btCambiarContrasenaPerfil, btAnadriImpuestosPerfil, btModificarDatos;
    private TextView btVerTodoPerfil;
    private ImageView ivFotoPerfil;
    private ImageButton btCerrarSesionPerfil;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;
    private FirebaseUser usuarioActual;
    private ActivityResultLauncher<Intent> cameraLauncher;

    // Parámetros opcionales para el fragmento
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilFragmento.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //Configurar el launcher para la cámara
        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == getActivity().RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getExtras() != null) {
                            Bitmap photo = (Bitmap) data.getExtras().get("data");
                            if (photo != null) {
                                ivFotoPerfil.setImageBitmap(photo);
                                subirImagenAFirebase(photo);
                            } else {
                                Toast.makeText(getActivity(), "No se ha capturado ninguna imagen", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e("PerfilFragment", "Intent data es null");
                        }
                    } else {
                        Log.e("PerfilFragment", "Error: resultCode incorrecto");
                    }
                });
    }

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
        btVerTodoPerfil = view.findViewById(R.id.tvVerTodoDatosPerfil);
        etSaldoInicialPerfil = view.findViewById(R.id.etSaldoInicialPerf);
        ivFotoPerfil = view.findViewById(R.id.ivPerfil);
        ivFotoPerfil.setImageResource(R.drawable.perfilimpuestos3);
        btModificarDatos = view.findViewById(R.id.btModificarDatosPerfil);
        btCambiarContrasenaPerfil = view.findViewById(R.id.btCambiarContrasenaPerf);
        btAnadriImpuestosPerfil = view.findViewById(R.id.anadirImpuestos);
        btCerrarSesionPerfil = view.findViewById(R.id.ibCerrarSesionPerfil);

        //Cargar datos del usuario si está autenticado
        if (usuarioActual != null) {
            cargarDatosUsuario();
        } else {
            Toast.makeText(getActivity(), "Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }

        btModificarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificarDatosUsuario();
            }
        });

        //Imagen botón
        btVerTodoPerfil.setOnClickListener(v ->{
            Intent irADatosImpuestos = new Intent(getActivity(), informacionAdicional.class);
            startActivity(irADatosImpuestos);
        });

        // Manejar clic en la imagen de perfil para tomar foto
        ivFotoPerfil.setOnClickListener(v ->{
            //Intentamos abrir la cámara
            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                abrirCamera();
            } else {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSION);
                System.out.println("No se puede abrir la camara, comprueba los permisos.");
            }
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
        btCerrarSesionPerfil.setOnClickListener(v -> {
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
        String nombreUsuario = usuarioActual.getDisplayName();
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

                        // Verificar si hay una imagen en Base64 subida por el usuario
                        if (snapshot.child("photoBase64").exists()) {
                            String imagenBase64 = snapshot.child("photoBase64").getValue(String.class);
                            if (imagenBase64 != null && !imagenBase64.isEmpty()) {
                                Log.d("PerfilFragment", "Base64 recuperada: " + imagenBase64);
                                //Propio
                                Bitmap imagenDecodificada = convertirBase64AImagen(imagenBase64);
                                ivFotoPerfil.setImageBitmap(imagenDecodificada);
                            } else {
                                Log.e("PerfilFragment", "Imagen en Base64 está vacía o nula");
                                //Google
                                comprobarImagenGoogle();
                            }
                        } else {
                            comprobarImagenGoogle();
                        }
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

    //Modificar datos personales del usuario
    public void modificarDatosUsuario(){
        String emailUser = usuarioActual.getEmail();
        DatabaseReference refNombre = nodoUsuario.child(emailUser.replace("@", "_").replace(".", "_")).child("nombre");
        DatabaseReference refSaldoInicial = nodoUsuario.child(emailUser.replace("@", "_").replace(".", "_")).child("saldoInicial");
        DatabaseReference refTiempoSaldoInicial = nodoUsuario.child(emailUser.replace("@", "_").replace(".", "_")).child("tiempoSaldoInicial");
        refNombre.setValue(etNombreApellidoPerfil.getText().toString());
        refSaldoInicial.setValue(Double.parseDouble(etSaldoInicialPerfil.getText().toString()));
        long currentTimeMillis = System.currentTimeMillis();
        refTiempoSaldoInicial.setValue(currentTimeMillis);
    }

    //Si hay aplicación de cámara
    private void abrirCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            Log.d("PerfilFragment", "Cámara encontrada, abriendo...");
            cameraLauncher.launch(cameraIntent);
        } else {
            Log.e("PerfilFragment", "No se encontró una aplicación de cámara instalada.");
            Toast.makeText(getActivity(), "No se encontró una aplicación de cámara", Toast.LENGTH_SHORT).show();
        }
    }

    //Si hay imagen de google
    private void comprobarImagenGoogle() {
        Uri Urlfoto = usuarioActual.getPhotoUrl();
        if (Urlfoto != null) {
            Glide.with(getContext())
                    .load(Urlfoto)
                    .into(ivFotoPerfil);
        } else {
            // Si no hay imagen en Google ni en Base64, poner la predeterminada
            ivFotoPerfil.setImageResource(R.drawable.perfilimpuestos3);
        }
    }

    //Captura y guarda la imagen de perfil en Firebase en formato Base64.
    private void subirImagenAFirebase(Bitmap photo) {
        String imagenBase64 = convertirImagenBase64(photo);

        if (usuarioActual != null) {
            String emailUser = usuarioActual.getEmail();
            if (emailUser != null) {
                String usuarioClave = emailUser.replace("@", "_").replace(".", "_");
                DatabaseReference usuarioRef = database.getReference("Usuarios").child(usuarioClave);

                usuarioRef.child("photoBase64").setValue(imagenBase64)
                        .addOnSuccessListener(aVoid -> {
                            Log.d("PerfilFragment", "Imagen guardada correctamente");
                            Toast.makeText(getActivity(), "Imagen guardada", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Log.e("PerfilFragment", "Error al guardar la imagen: " + e.getMessage());
                            Toast.makeText(getActivity(), "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
                        });
            }
        }
    }

    //Para gardar
    private String convertirImagenBase64(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
    }

    //Para sacar
    private Bitmap convertirBase64AImagen(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}