package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoRegistro1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoRegistro1 extends Fragment {
    private TextInputEditText etNombreRegistro, etDNIRegistro, etfechaRegistro, etEstadoRegistro, etDireccionRegistro, etNacionalidadRegistro, etCorreoRegistro, etTelefonoRegistro;
    private Button btSiguiente1Registro;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoPadre;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoRegistro1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoRegistro1.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoRegistro1 newInstance(String param1, String param2) {
        FragmentoRegistro1 fragment = new FragmentoRegistro1();
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
        View view = inflater.inflate(R.layout.fragment_registro1, container, false);
        //Inicialización de UI
        etNombreRegistro = view.findViewById(R.id.etNombreReg);
        etDNIRegistro = view.findViewById(R.id.etDNIReg);
        etfechaRegistro = view.findViewById(R.id.etfechaReg);
        etEstadoRegistro = view.findViewById(R.id.etEstadoReg);
        etDireccionRegistro = view.findViewById(R.id.etDireccionReg);
        etNacionalidadRegistro = view.findViewById(R.id.etNacionalidadReg);
        etTelefonoRegistro = view.findViewById(R.id.etTelefonoReg);
        btSiguiente1Registro = view.findViewById(R.id.btSiguienteReg1);

        btSiguiente1Registro.setOnClickListener(v -> {
        // Obtenemos los datos introducidos por el usuario
            String nombre = etNombreRegistro.getText().toString();
            String dni = etDNIRegistro.getText().toString();
            String fechaNacimiento = etfechaRegistro.getText().toString();
            String estadoCivil = etEstadoRegistro.getText().toString();
            String direccion = etDireccionRegistro.getText().toString();
            String nacionalidad = etNacionalidadRegistro.getText().toString();
            String telefono = etTelefonoRegistro.getText().toString();

            // Guardamos los datos en SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("nombre", nombre);
            editor.putString("dni", dni);
            editor.putString("fechaNacimiento", fechaNacimiento);
            editor.putString("estadoCivil", estadoCivil);
            editor.putString("direccion", direccion);
            editor.putString("nacionalidad", nacionalidad);
            editor.putString("telefono", telefono);
            editor.apply(); // Guardo los cambios

            // Pasar al siguiente fragmento
            Fragment siguienteFragmento = new FragmentoRegistro2();
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutRegistro1, siguienteFragmento)
                    .addToBackStack(null)
                    .commit();
            requireActivity().finish();
        });
        return view;
    }
}