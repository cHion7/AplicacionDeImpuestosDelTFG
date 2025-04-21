package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.aplicaciondeimpuestosdeltfg.R;

public class CalculatorFragment extends Fragment {

    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        // Referencia al DrawerLayout (desde el layout del fragmento)
        drawerLayout = view.findViewById(R.id.drawer_layout);

        // Configurar el botón (ImageView) para abrir el menú lateral
        ImageView botonAbrirMenu = view.findViewById(R.id.btn_open_menu);
        botonAbrirMenu.setOnClickListener(v -> {
            if (drawerLayout != null && !drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        return view;
    }
}
