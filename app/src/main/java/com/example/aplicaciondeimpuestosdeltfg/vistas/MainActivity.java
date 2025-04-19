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
   AjustesFragment settings = new AjustesFragment();
   CalculatorFragment calculatorFragment = new CalculatorFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Asegúrate que este layout tenga el BottomNavigationView y el FrameLayout con los IDs correctos
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, homeFragment)
                            .commit();
                    return true;
                } else if (id == R.id.ajustes) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, settings)
                            .commit();
                    return true;
                } else if (id == R.id.calculadora) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frame_container, calculatorFragment)
                            .commit();
                    return true;
                }

                return false;
            }

        });
    }
}
