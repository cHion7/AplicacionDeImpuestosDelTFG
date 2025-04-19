package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentoRegistro#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentoRegistro extends Fragment {
    private TextInputEditText etUsuarioReg, etContrasenaReg;
    private Button btSiguienteReg;
    private FirebaseAuth firebaseAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentoRegistro() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentoRegistro.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentoRegistro newInstance(String param1, String param2) {
        FragmentoRegistro fragment = new FragmentoRegistro();
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
        View view = inflater.inflate(R.layout.fragment_registro, container, false);
        // Inicializamos FirebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Referenciamos los elementos del layout
        etUsuarioReg = view.findViewById(R.id.etUsuarioReg);
        etContrasenaReg = view.findViewById(R.id.etContrasenaReg);
        btSiguienteReg = view.findViewById(R.id.btSiguienteReg);

        btSiguienteReg.setOnClickListener(v -> {
            // Obtenemos los datos introducidos por el usuario
            String correo = etUsuarioReg.getText().toString().trim();
            String contrasena = etContrasenaReg.getText().toString().trim();

            // Guardamos los datos en SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("registro_usuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("correo", correo);
            editor.putString("password", contrasena);
            editor.apply(); // Guardo los cambios

            // Pasar al siguiente fragmento
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.frameLayoutRegistro, new FragmentoRegistro1())
                    .addToBackStack(null)
                    .commit();
            //requireActivity().finish();
        });
        return view;
    }
}

