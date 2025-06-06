package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.aplicaciondeimpuestosdeltfg.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class verImpuestos extends AppCompatActivity {

    // Preguntas Comunes
    EditText etIngresoPregComunes, etEdadPregComunes, etPersonasACargoPregComunes;
    RadioButton radioViviendaSi, radioViviendaNo; // Changed to RadioButton for direct access
    Spinner spinner_situacion;
    Button btEnviarCom;

    // Layouts situaciones específicas
    LinearLayout layoutAsalariadoPerfilar, layoutAutonomoPerfilar,
            layoutEmpresarioPerfilar, layoutEstudiantePerfilar, layoutJubiladoPerfilar;

    // Asalariado
    Spinner spinnerContratoAsalariado;
    RadioGroup rgFamiliaNumerosaAsalariado;
    RadioButton radioFamiliaSiAsalariado, radioFamiliaNoAsalariado;
    EditText etEdadHijosAsalariado, etGastosEscolaresAsalariado;

    // Autónomo
    EditText etFechaAltaSSAutonomo, etActividadEconAutonomo, etGastosDeduciblesAutonomo,
            etIvaSoportadoAutonomo, etIvaRepercutidoAutonomo;
    RadioGroup rgCocheAutonomo;
    RadioButton radioCocheSiAutonomo, radioCocheNoAutonomo;

    // Empresario
    Spinner spinnerTipoEmpresaEmpresario;
    EditText etFacturacionAnualEmpresario, etSueldoAdminEmpresario,
            etNumeroEmpleadosEmpresario, etGastosDeduciblesEmpresario;

    // Estudiante
    Spinner spinnerEstudiosEstudiante;
    RadioGroup rgTrabajaParcialEstudiante;
    RadioButton radioTrabajaParcialSiEstudiante, radioTrabajaParcialNoEstudiante;
    EditText etCantidadBecaEstudiante;

    // Jubilado
    EditText etPensionMensualJubilado;
    RadioGroup rgAyudaAdicionalJubilado;
    RadioButton radioAyudaSiJubilado, radioAyudaNoJubilado;
    LinearLayout tilTipoAyudaJubilado; // This LinearLayout is the parent of etTipoAyudaJubilado
    EditText etTipoAyudaJubilado;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_comunes); // Make sure this matches your XML file name

        //Inicializamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        //Recuperamos los datos del shresPreference "impuestos"
        SharedPreferences sharedPreferences = getSharedPreferences("impuestos", Context.MODE_PRIVATE);
        //Preguntas comunes
        String eleccion = sharedPreferences.getString("situacion", "");
        String ingresoBruto = sharedPreferences.getString("ingresoBruto", "");
        String edad = sharedPreferences.getString("edad", "");
        String personasACargo = sharedPreferences.getString("personasACargo", "");
        Boolean vivienda = sharedPreferences.getBoolean("vivienda", false);



        // Initialize Common UI elements
        etIngresoPregComunes = findViewById(R.id.etIngresosCom);
        etEdadPregComunes = findViewById(R.id.etEdadCom);
        etPersonasACargoPregComunes = findViewById(R.id.etPersonasACargoCom);
        radioViviendaSi = findViewById(R.id.radio_vivienda_si);
        radioViviendaNo = findViewById(R.id.radio_vivienda_no);
        spinner_situacion = findViewById(R.id.spinner_situacion);
        btEnviarCom = findViewById(R.id.btEnviarCom);

        // Inicializar layout específicos
        layoutAsalariadoPerfilar = findViewById(R.id.layout_asalariado_perfilar_especif);
        layoutAutonomoPerfilar = findViewById(R.id.layout_autonomo_perfilar_especif);
        layoutEmpresarioPerfilar = findViewById(R.id.layout_empresario_perfilar_especif);
        layoutEstudiantePerfilar = findViewById(R.id.layout_estudiante_perfilar_especif);
        layoutJubiladoPerfilar = findViewById(R.id.layout_jubilado_perfilar_especif);


        //  Asalariado
        spinnerContratoAsalariado = findViewById(R.id.spinner_contrato);
        rgFamiliaNumerosaAsalariado = findViewById(R.id.rgFamiliaNumerosa_especif);
        radioFamiliaSiAsalariado = findViewById(R.id.radio_familia_si_especif);
        radioFamiliaNoAsalariado = findViewById(R.id.radio_familia_no_especif);
        etEdadHijosAsalariado = findViewById(R.id.etEdadHijos_especif);
        etGastosEscolaresAsalariado = findViewById(R.id.etGastosEscolares_especif);

        // Autónomo
        etFechaAltaSSAutonomo = findViewById(R.id.etDadoAltaSSAutono_especif);
        etActividadEconAutonomo = findViewById(R.id.etActividadEconoAutono_especif);
        etGastosDeduciblesAutonomo = findViewById(R.id.etGastosDeduciblesAutono_especif);
        etIvaSoportadoAutonomo = findViewById(R.id.etIvaSoportadoAutono_especif);
        etIvaRepercutidoAutonomo = findViewById(R.id.etIvaRepercutidoAutono_especif);
        rgCocheAutonomo = findViewById(R.id.rgCocheAutonomo_especif);
        radioCocheSiAutonomo = findViewById(R.id.radio_coche_si_especif);
        radioCocheNoAutonomo = findViewById(R.id.radio_coche_no_especif);

        // Empresario
        spinnerTipoEmpresaEmpresario = findViewById(R.id.spinnerTipoEmpresa_especif);
        etFacturacionAnualEmpresario = findViewById(R.id.etFacturaAnual_especif);
        etSueldoAdminEmpresario = findViewById(R.id.etSueldoAdmin_especif);
        etNumeroEmpleadosEmpresario = findViewById(R.id.etNumeroEmpleados_especif);
        etGastosDeduciblesEmpresario = findViewById(R.id.etGastosDeduciblesEmpresario);

        // Estudiante
        spinnerEstudiosEstudiante = findViewById(R.id.spinner_estudios);
        rgTrabajaParcialEstudiante = findViewById(R.id.rgTrabajaParcialEstudiante_especif);
        radioTrabajaParcialSiEstudiante = findViewById(R.id.radio_trabajaParcial_si_especif);
        radioTrabajaParcialNoEstudiante = findViewById(R.id.radio_trabajaParcial_no_especif);
        etCantidadBecaEstudiante = findViewById(R.id.etRecibirBecaEstudiante_especif);

        // Jubilado
        etPensionMensualJubilado = findViewById(R.id.etPensionMensualJubilado_especif);
        rgAyudaAdicionalJubilado = findViewById(R.id.rgAyudaAdicionalJubilado_especif);
        radioAyudaSiJubilado = findViewById(R.id.radio_ayuda_si_jubilado_especif);
        radioAyudaNoJubilado = findViewById(R.id.radio_ayuda_no_jubilado_especif);

        tilTipoAyudaJubilado = findViewById(R.id.tilTipoAyudaJubilado_especif);
        etTipoAyudaJubilado = findViewById(R.id.etTipoAyudaJubilado_especif);


        // Spinners
        List<String> valorSpinnerSituacion = List.of("Asalariado", "Autónomo", "Empresario", "Estudiante", "Jubilado");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valorSpinnerSituacion);
        spinner_situacion.setAdapter(arraySituacion);
        // Set a default selection. This will trigger the onItemSelected and show the corresponding layout.
        spinner_situacion.setSelection(0); // Default to Asalariado


        // Asalariado Spinner setup
        List<String> spinnerValuesContrato = List.of("Parcial", "Indefinido", "Estacional");
        ArrayAdapter<String> adapterContrato = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesContrato);
        spinnerContratoAsalariado.setAdapter(adapterContrato);

        // Empresario Spinner setup
        List<String> spinnerValuesTipoEmpresa = List.of("SL", "SA", "Autónomo con empleados"); // Example types
        ArrayAdapter<String> adapterTipoEmpresa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesTipoEmpresa);
        spinnerTipoEmpresaEmpresario.setAdapter(adapterTipoEmpresa);

        // Estudiante Spinner setup
        List<String> spinnerValuesEstudios = List.of("Universidad", "Formación Profesional", "Bachillerato", "Otro"); // Example types
        ArrayAdapter<String> adapterEstudios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesEstudios);
        spinnerEstudiosEstudiante.setAdapter(adapterEstudios);


        // Spinner listener to show/hide specific layouts
        spinner_situacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSituation = parent.getItemAtPosition(position).toString();
                // Call the method to update visibility based on selection
                updateSpecificLayoutVisibility(selectedSituation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally hide all layouts if nothing is selected (though a spinner usually has a selection)
                hideAllSpecificLayouts();
            }
        });

        // Listener for the "Sí" radio button in Jubilado section
        rgAyudaAdicionalJubilado.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_ayuda_si_jubilado) {
                tilTipoAyudaJubilado.setVisibility(View.VISIBLE);
            } else {
                tilTipoAyudaJubilado.setVisibility(View.GONE);
                etTipoAyudaJubilado.setText(""); // Clear text when hidden
            }
        });

        // Button click listener
        btEnviarCom.setOnClickListener(v -> {
            registrarDatosComunesYEspecificos();
        });
    }

    // Helper method to hide all specific layouts
    private void hideAllSpecificLayouts() {
        layoutAsalariadoPerfilar.setVisibility(View.GONE);
        layoutAutonomoPerfilar.setVisibility(View.GONE);
        layoutEmpresarioPerfilar.setVisibility(View.GONE);
        layoutEstudiantePerfilar.setVisibility(View.GONE);
        layoutJubiladoPerfilar.setVisibility(View.GONE);
    }

    // Method to update visibility based on selected situation
    private void updateSpecificLayoutVisibility(String selectedSituation) {
        // Hide all specific layouts first
        hideAllSpecificLayouts();

        // Show the relevant layout based on selection
        switch (selectedSituation) {
            case "Asalariado":
                layoutAsalariadoPerfilar.setVisibility(View.VISIBLE);
                break;
            case "Autónomo":
                layoutAutonomoPerfilar.setVisibility(View.VISIBLE);
                break;
            case "Empresario":
                layoutEmpresarioPerfilar.setVisibility(View.VISIBLE);
                break;
            case "Estudiante":
                layoutEstudiantePerfilar.setVisibility(View.VISIBLE);
                break;
            case "Jubilado":
                layoutJubiladoPerfilar.setVisibility(View.VISIBLE);
                // Handle initial visibility of the "Tipo de Ayuda" field for Jubilado
                // Only show if "Sí" is checked, otherwise hide it.
                if (radioAyudaSiJubilado.isChecked()) {
                    tilTipoAyudaJubilado.setVisibility(View.VISIBLE);
                } else {
                    tilTipoAyudaJubilado.setVisibility(View.GONE);
                }
                break;
        }
    }

    public void registrarDatosComunesYEspecificos() {
        // Get common data
        String eleccion = spinner_situacion.getSelectedItem().toString();
        String ingresoBruto = etIngresoPregComunes.getText().toString();
        String edad = etEdadPregComunes.getText().toString();
        String personasACargo = etPersonasACargoPregComunes.getText().toString();
        boolean vivienda = radioViviendaSi.isChecked(); // Get value from radio button

        // Basic validation for common fields
        if (ingresoBruto.isEmpty() || edad.isEmpty() || personasACargo.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos comunes.", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
        if (usuarioActual == null) {
            Toast.makeText(this, "No hay usuario autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a HashMap for all user data
        HashMap<String, Object> datosUsuario = new HashMap<>();
        datosUsuario.put("eleccion", eleccion);
        datosUsuario.put("ingresoBruto", ingresoBruto);
        datosUsuario.put("edad", edad);
        datosUsuario.put("personasACargo", personasACargo);
        datosUsuario.put("vivienda", vivienda);

        // Add specific data based on selection
        switch (eleccion) {
            case "Asalariado":
                String tipoContrato = spinnerContratoAsalariado.getSelectedItem().toString();
                boolean familiaNumerosa = radioFamiliaSiAsalariado.isChecked();
                String edadesHijos = etEdadHijosAsalariado.getText().toString();
                String gastosEscolares = etGastosEscolaresAsalariado.getText().toString();

                // Validation for Asalariado specific fields
                if (tipoContrato.isEmpty() || edadesHijos.isEmpty() || gastosEscolares.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete todos los campos de Asalariado.", Toast.LENGTH_SHORT).show();
                    return;
                }
                datosUsuario.put("tipoContrato", tipoContrato);
                datosUsuario.put("familiaNumerosa", familiaNumerosa);
                datosUsuario.put("edadesHijos", edadesHijos);
                datosUsuario.put("gastosEscolares", gastosEscolares);
                break;
            case "Autónomo":
                String fechaAltaSS = etFechaAltaSSAutonomo.getText().toString();
                String actividadEcon = etActividadEconAutonomo.getText().toString();
                String gastosDeducibles = etGastosDeduciblesAutonomo.getText().toString();
                String ivaSoportado = etIvaSoportadoAutonomo.getText().toString();
                String ivaRepercutido = etIvaRepercutidoAutonomo.getText().toString();
                boolean vehiculoEmpresa = radioCocheSiAutonomo.isChecked();

                // Validation for Autónomo specific fields
                if (fechaAltaSS.isEmpty() || actividadEcon.isEmpty() || gastosDeducibles.isEmpty() ||
                        ivaSoportado.isEmpty() || ivaRepercutido.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete todos los campos de Autónomo.", Toast.LENGTH_SHORT).show();
                    return;
                }
                datosUsuario.put("fechaAltaSS", fechaAltaSS);
                datosUsuario.put("actividadEconomica", actividadEcon);
                datosUsuario.put("gastosDeducibles", gastosDeducibles);
                datosUsuario.put("ivaSoportado", ivaSoportado);
                datosUsuario.put("ivaRepercutido", ivaRepercutido);
                datosUsuario.put("vehiculoEmpresa", vehiculoEmpresa);
                break;
            case "Empresario":
                String tipoEmpresa = spinnerTipoEmpresaEmpresario.getSelectedItem().toString();
                String facturacionAnual = etFacturacionAnualEmpresario.getText().toString();
                String sueldoAdministrador = etSueldoAdminEmpresario.getText().toString();
                String numeroEmpleados = etNumeroEmpleadosEmpresario.getText().toString();
                String gastosDeduciblesEmpresa = etGastosDeduciblesEmpresario.getText().toString();

                // Validation for Empresario specific fields
                if (tipoEmpresa.isEmpty() || facturacionAnual.isEmpty() || sueldoAdministrador.isEmpty() ||
                        numeroEmpleados.isEmpty() || gastosDeduciblesEmpresa.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete todos los campos de Empresario.", Toast.LENGTH_SHORT).show();
                    return;
                }
                datosUsuario.put("tipoEmpresa", tipoEmpresa);
                datosUsuario.put("facturacionAnual", facturacionAnual);
                datosUsuario.put("sueldoAdministrador", sueldoAdministrador);
                datosUsuario.put("numeroEmpleados", numeroEmpleados);
                datosUsuario.put("gastosDeduciblesEmpresa", gastosDeduciblesEmpresa);
                break;
            case "Estudiante":
                String tipoEstudios = spinnerEstudiosEstudiante.getSelectedItem().toString();
                boolean trabajaTiempoParcial = radioTrabajaParcialSiEstudiante.isChecked();
                String cantidadBeca = etCantidadBecaEstudiante.getText().toString();

                // Validation for Estudiante specific fields
                if (tipoEstudios.isEmpty() || cantidadBeca.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete todos los campos de Estudiante.", Toast.LENGTH_SHORT).show();
                    return;
                }
                datosUsuario.put("tipoEstudios", tipoEstudios);
                datosUsuario.put("trabajaTiempoParcial", trabajaTiempoParcial);
                datosUsuario.put("cantidadBeca", cantidadBeca);
                break;
            case "Jubilado":
                String pensionMensual = etPensionMensualJubilado.getText().toString();
                boolean recibeAyudaAdicional = radioAyudaSiJubilado.isChecked();
                String tipoAyudaAdicional = etTipoAyudaJubilado.getText().toString();

                // Validation for Jubilado specific fields
                if (pensionMensual.isEmpty()) {
                    Toast.makeText(this, "Por favor, complete la pensión mensual de Jubilado.", Toast.LENGTH_SHORT).show();
                    return;
                }
                datosUsuario.put("pensionMensual", pensionMensual);
                datosUsuario.put("recibeAyudaAdicional", recibeAyudaAdicional);
                // Only add tipoAyudaAdicional if "Sí" was selected for additional help AND the field is not empty
                if (recibeAyudaAdicional) {
                    if (tipoAyudaAdicional.isEmpty()){
                        Toast.makeText(this, "Por favor, describe la ayuda adicional para Jubilado.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    datosUsuario.put("tipoAyudaAdicional", tipoAyudaAdicional);
                }
                break;
        }

        // Save data to Firebase
        String emailUsuario = Objects.requireNonNull(usuarioActual.getEmail());
        String emailKey = emailUsuario.replace(".", "_").replace("@", "_");

        nodoUsuario.child(emailKey).child("datosPersonales").setValue(datosUsuario)
                .addOnCompleteListener(dbTask -> {
                    if (dbTask.isSuccessful()) {
                        Toast.makeText(this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}