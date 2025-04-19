package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoRegistro2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoRegistro2 extends Fragment {
    private TextInputEditText etEmpresaRegistro, etIngresosBrRegistro, etNominaRegistro, etIngresosAdRegistro, etDeclaracionRegistro;
    private Button btRegistroRegistro;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoRegistro2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoRegistro2.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoRegistro2 newInstance(String param1, String param2) {
        FragmentoRegistro2 fragment = new FragmentoRegistro2();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro2, container, false);
        // Inicializamos las Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app");
        nodoUsuario = database.getReference("Usuarios");

        etEmpresaRegistro = view.findViewById(R.id.etEmpresaReg);
        etIngresosBrRegistro = view.findViewById(R.id.etIngresosBrReg);
        etNominaRegistro = view.findViewById(R.id.etNominaReg);
        etIngresosAdRegistro = view.findViewById(R.id.etIngresosAdReg);
        etDeclaracionRegistro = view.findViewById(R.id.etDeclaracionReg);
        btRegistroRegistro = view.findViewById(R.id.btRegistroReg);

        //Registrarse
        btRegistroRegistro.setOnClickListener(v -> {
            registrarNuevoUsuario();
        });
        return view;
    }

    private void registrarNuevoUsuario() {
        //Recuperar los datos del sharedPreferences "registro_usuario"
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
        String nombre = sharedPreferences.getString("nombre", "");
        String dni = sharedPreferences.getString("dni", "");
        String fechaNacimiento = sharedPreferences.getString("fechaNacimiento", "");
        String estadoCivil = sharedPreferences.getString("estadoCivil", "");
        String direccion = sharedPreferences.getString("direccion", "");
        String nacionalidad = sharedPreferences.getString("nacionalidad", "");
        String telefono = sharedPreferences.getString("telefono", "");
        String correo = sharedPreferences.getString("correo", "");
        String password = sharedPreferences.getString("password", "");


        // Obtener datos del Fragmento 2
        String empresa = etEmpresaRegistro.getText().toString();
        String ingresosBrutos = etIngresosBrRegistro.getText().toString();
        String nomina = etNominaRegistro.getText().toString();
        String ingresosAdicionales = etIngresosAdRegistro.getText().toString();
        String declaraciones = etDeclaracionRegistro.getText().toString();

        // Crear y registra un usuario en Firebase Authentication
        firebaseAuth.createUserWithEmailAndPassword(correo, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Obtener usuario registrado
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                            if (user != null) { //registro fue exitoso y el usuario está disponible.
                                // Crear un HashMap para almacenar los datos del usuario - Agrupar todos los datos en un HashMap
                                HashMap<String, Object> datosUsuario = new HashMap<>();
                                datosUsuario.put("nombre", nombre);
                                datosUsuario.put("dni", dni);
                                datosUsuario.put("fechaNacimiento", fechaNacimiento);
                                datosUsuario.put("estadoCivil", estadoCivil);
                                datosUsuario.put("direccion", direccion);
                                datosUsuario.put("nacionalidad", nacionalidad);
                                datosUsuario.put("telefono", telefono);
                                datosUsuario.put("correo", correo);
                                datosUsuario.put("password", password);

                                datosUsuario.put("empresa", empresa);
                                datosUsuario.put("ingresosBrutos", ingresosBrutos);
                                datosUsuario.put("nomina", nomina);
                                datosUsuario.put("ingresosAdicionales", ingresosAdicionales);
                                datosUsuario.put("declaraciones", declaraciones);

                                // Convertir el email en clave válida para Firebase (reemplaza caracteres especiales)
                                String emailKey = correo.replace(".", "_").replace("@", "_");
                                // Guardar los datos en la base de datos Firebase
                                nodoUsuario.child(emailKey).setValue(datosUsuario).addOnCompleteListener(dbTask -> {
                                    if (dbTask.isSuccessful()) {
                                        user.sendEmailVerification();
                                        Toast.makeText(requireContext(), "Registro exitoso. Verifica tu email.", Toast.LENGTH_LONG).show();

                                        // Opcional: Redirigir al login o cerrar el registro
                                        startActivity(new Intent(getActivity(), Login.class));
                                        requireActivity().finish();

                                    } else {
                                        Toast.makeText(requireContext(), "Error al guardar datos en la base de datos", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Toast.makeText(requireContext(), "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
                            }
                        Toast.makeText(getContext(), "Registro exitoso", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
        };

    }