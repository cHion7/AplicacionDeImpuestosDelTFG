package com.example.aplicaciondeimpuestosdeltfg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.vistas.AsalariadoPerfilar;
import com.example.aplicaciondeimpuestosdeltfg.vistas.preguntasComunes;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class informacionAdicional extends AppCompatActivity {
    EditText etIngresoPregComunes, etEdadPregComunes, etPersonasACargoPregComunes;
    RadioButton viviendaPregComunes;
    Spinner spinner_situacion;
    Button btEnviarCom;

    LinearLayout layoutAsalariadoPerfilar, layoutAutonomoPerfilar,
            layoutEmpresarioPerfilar, layoutEstudiantePerfilar, layoutJubiladoPerfilar;


    EditText editTextDate, actividadEconomica, gastosDeducibles, ivaSoportado, ivaRepercutido;
    RadioButton radio_coche_si;
    Button bt_enviarAutonomo;

    Spinner spinner_contrato;
    RadioButton familiaNumAsala;
    EditText etEdadHijosAsala, gastosEscolaresAsala;
    Button btEnviarAsala;

    Spinner spinnerTipoempresa;
    EditText edit_ingreso, sueldoAdmin, empleadosNum, gastosDeduciblesEmp;
    Button btn_enviarEmpresario;

    Spinner spiner_estudios;
    RadioButton radio_estudiante_si;
    EditText edit_estudiosBeca;
    Button btn_enviarEstudiante;

    RadioButton radio_jubilado_si;
    EditText cobroPension, gastosMedicos;
    Button btn_enviarJubilado;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference nodoUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informacion_adicional);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_infoAdicional), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Preguntas comunes
        etIngresoPregComunes = findViewById(R.id.etIngresosComAd);
        etEdadPregComunes = findViewById(R.id.etEdadComAd);
        etPersonasACargoPregComunes = findViewById(R.id.etPersonasACargoComAd);
        viviendaPregComunes = findViewById(R.id.radio_vivienda_siAd);
        spinner_situacion = findViewById(R.id.spinner_situacionAd);

        List<String> valorSpinnerSituacion = List.of("Autónomo", "Asalariado", "Empresario", "Estudiante", "Jubilado");
        ArrayAdapter<String> arraySituacion = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, valorSpinnerSituacion);
        spinner_situacion.setAdapter(arraySituacion);
        spinner_situacion.setSelection(3);

        //LinearLayout
        layoutAsalariadoPerfilar = findViewById(R.id.layout_asalariado_perfilarAd);
        layoutAutonomoPerfilar = findViewById(R.id.layout_autonomo_perfilarAd);
        layoutEmpresarioPerfilar = findViewById(R.id.layout_empresario_perfilarAd);
        layoutEstudiantePerfilar = findViewById(R.id.layout_estudiante_perfilarAd);
        layoutJubiladoPerfilar = findViewById(R.id.layout_jubilado_perfilarAd);

        //Asalariado
        spinner_contrato = findViewById(R.id.spinner_contratoAd);
        familiaNumAsala = findViewById(R.id.radio_familia_siAd);
        etEdadHijosAsala = findViewById(R.id.etEdadHijosAd);
        gastosEscolaresAsala = findViewById(R.id.etGastosEscolaresAd);

        List<String> spinnerValues = List.of("Parcial", "Indefinido", "Estacional");
        ArrayAdapter<String> arraySituacionContrato = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValues);
        spinner_contrato.setAdapter(arraySituacionContrato);
        spinner_contrato.setSelection(0);

        //Autónomo
        editTextDate = findViewById(R.id.etDadoAltaSSAutonoAd);
        actividadEconomica = findViewById(R.id.etActividadEconoAutonoAd);
        gastosDeducibles = findViewById(R.id.etGastosDeduciblesAutonoAd);
        ivaSoportado = findViewById(R.id.etIvaSoportadoAutonoAd);
        ivaRepercutido = findViewById(R.id.etIvaRepercutidoAutonoAd);
        radio_coche_si = findViewById(R.id.radio_coche_siAd);

        //Empresario
        spinnerTipoempresa = findViewById(R.id.spinnerTipoEmpresaAd);
        edit_ingreso = findViewById(R.id.etFacturaAnualAd);
        sueldoAdmin = findViewById(R.id.etSueldoAdminAd);
        empleadosNum = findViewById(R.id.etNumeroEmpleadosAd);
        gastosDeduciblesEmp = findViewById(R.id.etGastosDeduciblesEmpresarioAd);

        List<String> spinnerValuesEmpresa = List.of("S.A", "S.L", "Otro");
        ArrayAdapter<String> arraySituacionEmpresa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesEmpresa);
        spinnerTipoempresa.setAdapter(arraySituacionEmpresa);
        spinnerTipoempresa.setSelection(0);

        //Estudiante
        spiner_estudios = findViewById(R.id.spinner_estudiosAd);
        radio_estudiante_si = findViewById(R.id.radio_trabajaParcial_siAd);
        edit_estudiosBeca = findViewById(R.id.etRecibirBecaEstudianteAd);

        List<String> spinnerValuesEstudios = List.of("Universidad", "FP", "Bachiller");
        ArrayAdapter<String> arraySituacionEstudios = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerValuesEstudios);
        spiner_estudios.setAdapter(arraySituacionEstudios);
        spiner_estudios.setSelection(0);

        //Jubilados
        radio_jubilado_si = findViewById(R.id.radio_ayuda_si_jubiladoAd);
        cobroPension = findViewById(R.id.etPensionMensualJubiladoAd);
        gastosMedicos = findViewById(R.id.etTipoAyudaJubiladoAd);

        //Situación
        spinner_situacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String situacionSelecionada = parent.getItemAtPosition(position).toString();
                vistaEspecializada(situacionSelecionada);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        vistaEspecializada(spinner_situacion.getSelectedItem().toString());

        btEnviarCom.setOnClickListener(v -> {
            FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
            if (usuarioActual != null) {
                registrarDatosComunesYEspecificos(usuarioActual);
            }else{
                Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void vistaEspecializada(String situacionSelecionada) {
        // Escondemos vistas especializadas
        layoutAsalariadoPerfilar.setVisibility(View.GONE);
        layoutAutonomoPerfilar.setVisibility(View.GONE);
        layoutEmpresarioPerfilar.setVisibility(View.GONE);
        layoutEstudiantePerfilar.setVisibility(View.GONE);
        layoutJubiladoPerfilar.setVisibility(View.GONE);

        // Enseñar layout en función de la seleción
        switch (situacionSelecionada) {
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
                break;
        }
    }
    public void registrarDatosComunesYEspecificos(FirebaseUser usuarioActual){
        //Preguntas comunes
        String eleccion = spinner_situacion.getSelectedItem().toString();
        String ingresoBruto = etIngresoPregComunes.getText().toString();
        String edad = etEdadPregComunes.getText().toString();
        String personaACargo = etPersonasACargoPregComunes.getText().toString();
        boolean vivienda = viviendaPregComunes.isChecked();

        // Validación de los campos
        if (edad.isEmpty() || ingresoBruto.isEmpty() || eleccion.isEmpty() || personaACargo.isEmpty()) {
            Toast.makeText(informacionAdicional.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }
        //Asalariado
        String tipoContrato = spinner_contrato.getSelectedItem().toString();
        boolean familiaNumerosa = familiaNumAsala.isChecked();
        String edadesHijos = etEdadHijosAsala.getText().toString();
        String gastosEscolares = gastosEscolaresAsala.getText().toString();

        //Autónomo
        String fecha = editTextDate.getText().toString();
        String actividad = actividadEconomica.getText().toString();
        String gastos = gastosDeducibles.getText().toString();
        String ivaSop = ivaSoportado.getText().toString();
        String ivaRep = ivaRepercutido.getText().toString();
        boolean coche = radio_coche_si.isChecked();

        //Empresario
        String tipoEmpresa = spinnerTipoempresa.getSelectedItem().toString();
        String facturacionEmpresa = edit_ingreso.getText().toString();
        String sueldoJefe = sueldoAdmin.getText().toString();
        String numeroEmpleados = empleadosNum.getText().toString();
        String gastosDeduciblesEmpresa = gastosDeduciblesEmp.getText().toString();

        if (facturacionEmpresa.isEmpty() || sueldoJefe.isEmpty()|| numeroEmpleados.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Estudiante
        boolean parcial = radio_estudiante_si.isChecked();
        String cantidadBeca = edit_estudiosBeca.getText().toString();
        String estudio = spiner_estudios.getSelectedItem().toString();

        if (cantidadBeca.isEmpty() || estudio.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Jubilado
        boolean cobroPensionario = radio_jubilado_si.isChecked();
        String segundaVivienda = cobroPension.getText().toString();
        String gastoMedico = gastosMedicos.getText().toString();

        if (gastoMedico.isEmpty() || segundaVivienda.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        //Mandar datos al Firebase
        if (usuarioActual != null) { //registro fue exitoso y el usuario está disponible.
            // Crear un HashMap para almacenar los datos del usuario
            HashMap<String, Object> datosUsuario = new HashMap<>();
            datosUsuario.put("eleccion", eleccion);
            datosUsuario.put("ingresoBruto", ingresoBruto);
            datosUsuario.put("edad", edad);
            datosUsuario.put("personasACargo", personaACargo);
            datosUsuario.put("vivienda", vivienda);
            //Asalariado
            datosUsuario.put("tipoContrato", tipoContrato);
            datosUsuario.put("familiaNumerosa", familiaNumerosa);
            datosUsuario.put("edadesHijos", edadesHijos);
            datosUsuario.put("gastosEscolares", gastosEscolares);
            //Autonomo
            datosUsuario.put("fechaAlta", fecha);
            datosUsuario.put("actividad", actividad);
            datosUsuario.put("gastosDeducibles", gastos);
            datosUsuario.put("ivaSoportado", ivaSop);
            datosUsuario.put("ivaRepercutido", ivaRep);
            datosUsuario.put("vehiculo", coche);
            //Empresario
            datosUsuario.put("tipoContrato", tipoEmpresa);
            datosUsuario.put("sueldoAdministrador", sueldoJefe);
            datosUsuario.put("facturacionEmpresa", facturacionEmpresa);
            datosUsuario.put("empleados", numeroEmpleados);
            datosUsuario.put("gastosDeduciblesEmpresa", gastosDeduciblesEmpresa);
            //Estudiante
            datosUsuario.put("tipoEstudios", estudio);
            datosUsuario.put("trabaja", parcial);
            datosUsuario.put("becaCantidad", cantidadBeca);
           //Jubilado
            datosUsuario.put("segundaVivienda", segundaVivienda);
            datosUsuario.put("pensionAnual", cobroPensionario);
            datosUsuario.put("gastosMedicos", gastoMedico);

            //Obtener el email del usuario logueado
            String emailUsuario = usuarioActual.getEmail();
            if (emailUsuario == null || emailUsuario.isEmpty()) {
                Toast.makeText(this, "Error: El correo del usuario no está disponible.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fecha.isEmpty() || actividad.isEmpty() || gastos.isEmpty() || ivaSop.isEmpty() || ivaRep.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            if (emailUsuario == null || emailUsuario.isEmpty()) {
                Toast.makeText(this, "Error: El correo del usuario no está disponible.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (emailUsuario == null || emailUsuario.isEmpty()) {
                Toast.makeText(this, "Error: El correo del usuario no está disponible.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (emailUsuario == null || emailUsuario.isEmpty()) {
                Toast.makeText(this, "Error: El correo del usuario no está disponible.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Convertir el email en clave válida para Firebase (reemplaza caracteres especiales)
            String emailKey = emailUsuario.replace(".", "_").replace("@", "_");

            // Guardar los datos en la base de datos Firebase
            nodoUsuario.child(emailKey).child("datosPersonales").setValue(datosUsuario).addOnCompleteListener(dbTask ->{

                if (dbTask.isSuccessful()) { //Escritura
                    Toast.makeText(this, "Datos guardados corectamente.", Toast.LENGTH_LONG).show();
                    //Redirigir al main
                    Intent intentAlLogin = new Intent(informacionAdicional.this, Login.class);
                    startActivity(intentAlLogin);
                    finish();
                } else {
                    Toast.makeText(this, "Error al guardar datos en la base de datos.", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Error: Usuario no autenticado", Toast.LENGTH_SHORT).show();
        }
        Toast.makeText(this, "Usuario registrado exitosamente.", Toast.LENGTH_SHORT).show();
    }
}