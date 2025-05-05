package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.Registro;
import com.example.aplicaciondeimpuestosdeltfg.Registro1;
import com.example.aplicaciondeimpuestosdeltfg.Registro2;


public class PerfilFragment extends Fragment {
    private Button btanadirDatos;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        //Referenciamos los layouts
        btanadirDatos = view.findViewById(R.id.btanadirDatosPerfil);

        //Botón añadir fragmento
        btanadirDatos.setOnClickListener(v ->{
            addDatos();
        });
        return view;
    }
    public void addDatos(){
        Intent intentReg1 = new Intent(getActivity(), Registro1.class);
        startActivity(intentReg1);
    }
}