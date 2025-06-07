package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

   BottomNavigationView bottomNavigationView;
   HomePage homeFragment = new HomePage();
   PerfilFragment perfilFragment = new PerfilFragment();
   CalculatorFragment calculatorFragment = new CalculatorFragment();
   String seleted_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asegúrate que este layout tenga el BottomNavigationView y el FrameLayout con los IDs correctos
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        // Cargar el fragmento inicial (Principal) directamente
        getSupportFragmentManager().beginTransaction().replace(R.id.mainPageFragmentContainer,homeFragment).commit();
        bottomNavigationView.setSelectedItemId(R.id.principal);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.perfil) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainPageFragmentContainer, perfilFragment)
                            .commit();
                    return true;
                } else if (id == R.id.principal) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainPageFragmentContainer, homeFragment)
                            .commit();
                    return true;
                } else if (id == R.id.calculadora) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainPageFragmentContainer, calculatorFragment)
                            .commit();
                    return true;
                }
                return false;
            }
        });

        //Navegar por fragments
        seleted_tab = getIntent().getStringExtra("selected_tab");
        if (seleted_tab != null && seleted_tab.equals("perfil")) {
            bottomNavigationView.setSelectedItemId(R.id.perfil);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainPageFragmentContainer, perfilFragment)
                    .commit();
        } else if (seleted_tab != null && seleted_tab.equals("principal")){
            bottomNavigationView.setSelectedItemId(R.id.principal);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainPageFragmentContainer, homeFragment)
                    .commit();
        }
    }
}
