package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class informacionAdicional extends AppCompatActivity {
    TextView tvNombreUsuarioInforme, tvCorreoUsuarioInforme;

    TextView tvIngresoPregComunes, tvEdadPregComunes, tvPersonasACargoPregComunes, spinner_situacion;
    TextView tvViviendaPropiedadAd;

    CardView layoutAsalariadoPerfilar, layoutAutonomoPerfilar,
            layoutEmpresarioPerfilar, layoutEstudiantePerfilar, layoutJubiladoPerfilar;

    TextView tvDate, tvactividadEconomica, tvgastosDeducibles, tvivaSoportado, tvivaRepercutido;
    TextView tvCocheAutonomoAd;

    TextView tvFamiliaNumerosaAd;
    TextView tvEdadHijosAsala, tvgastosEscolaresAsala, spinner_contrato;

    TextView tvingreso, tvsueldoAdmin, tvempleadosNum, tvgastosDeduciblesEmp, spinnerTipoempresa;

    TextView tvTrabajaParcialEstudianteAd;
    TextView tvestudiosBeca, spiner_estudios;

    TextView tvcobroPension, tvgastosMedicos;
    TextView tvSegundaViviendaAd;
    ImageButton btVolverAtrasInformacion;
    Button btEnviarCom, btGenerarImpuestos;

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
        // Inicializar TextViews para Nombre y Correo
        tvNombreUsuarioInforme = findViewById(R.id.tvNombreDocumento);
        tvCorreoUsuarioInforme = findViewById(R.id.tvCorreoDocumento);
        btVolverAtrasInformacion = findViewById(R.id.ibVolverInformacion);
        btGenerarImpuestos = findViewById(R.id.btGenerarImpuestos);



        //Preguntas comunes
        tvIngresoPregComunes = findViewById(R.id.etIngresosComAd);
        tvEdadPregComunes = findViewById(R.id.etEdadComAd);
        tvPersonasACargoPregComunes = findViewById(R.id.etPersonasACargoComAd);
        spinner_situacion = findViewById(R.id.spinner_situacionAd);

        //LinearLayout
        layoutAsalariadoPerfilar = findViewById(R.id.layout_asalariado_perfilarAd);
        layoutAutonomoPerfilar = findViewById(R.id.layout_autonomo_perfilarAd);
        layoutEmpresarioPerfilar = findViewById(R.id.layout_empresario_perfilarAd);
        layoutEstudiantePerfilar = findViewById(R.id.layout_estudiante_perfilarAd);
        layoutJubiladoPerfilar = findViewById(R.id.layout_jubilado_perfilarAd);

        //Asalariado
        spinner_contrato = findViewById(R.id.spinner_contratoAd);
        tvFamiliaNumerosaAd = findViewById(R.id.tvFamiliaNumerosaAd);
        tvEdadHijosAsala = findViewById(R.id.etEdadHijosAd);
        tvgastosEscolaresAsala = findViewById(R.id.etGastosEscolaresAd);
        tvViviendaPropiedadAd = findViewById(R.id.tvViviendaPropiedadAd);

        //Autónomo
        tvDate = findViewById(R.id.etDadoAltaSSAutonoAd);
        tvactividadEconomica = findViewById(R.id.etActividadEconoAutonoAd);
        tvgastosDeducibles = findViewById(R.id.etGastosDeduciblesAutonoAd);
        tvivaSoportado = findViewById(R.id.etIvaSoportadoAutonoAd);
        tvivaRepercutido = findViewById(R.id.etIvaRepercutidoAutonoAd);
        tvCocheAutonomoAd = findViewById(R.id.tvCocheAutonomoAd);

        //Empresario
        spinnerTipoempresa = findViewById(R.id.spinnerTipoEmpresaAd);
        tvingreso = findViewById(R.id.etFacturaAnualAd);
        tvsueldoAdmin = findViewById(R.id.etSueldoAdminAd);
        tvempleadosNum = findViewById(R.id.etNumeroEmpleadosAd);
        tvgastosDeduciblesEmp = findViewById(R.id.etGastosDeduciblesEmpresarioAd);

        //Estudiante
        spiner_estudios = findViewById(R.id.spinner_estudiosAd);
        tvestudiosBeca = findViewById(R.id.etRecibirBecaEstudianteAd);
        tvTrabajaParcialEstudianteAd = findViewById(R.id.tvTrabajaParcialEstudianteAd);

        //Jubilados
        tvcobroPension = findViewById(R.id.etPensionAnualJubiladoAd);
        tvgastosMedicos = findViewById(R.id.etgastosMedicosJubiladoAd);
        tvSegundaViviendaAd = findViewById(R.id.tvSegViviendalJubiladoAd);

        //Iniciamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        nodoUsuario = database.getReference("Usuarios");

        //Botón de volver atrás
        btVolverAtrasInformacion.setOnClickListener(v ->{
            Intent intentAlPerfil = new Intent(informacionAdicional.this, MainActivity.class);
            intentAlPerfil.putExtra("selected_tab", "perfil");
            startActivity(intentAlPerfil);
            finish();
        });
        btGenerarImpuestos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setImpuestosTime fragmento = setImpuestosTime.newInstance();
                fragmento.show(getSupportFragmentManager(), "setImpuestosTime");
            }
        });


        FirebaseUser usuarioActual = firebaseAuth.getCurrentUser();
        if (usuarioActual != null) {
            obtenerDatos(usuarioActual);
        } else {
            Toast.makeText(this, "No hay usuario autentificado.", Toast.LENGTH_SHORT).show();
        }
    }

    public void obtenerDatos(FirebaseUser usuarioActual) {
        // Convertir el email en clave válida para Firebase (reemplaza caracteres especiales)
        String emailKey = usuarioActual.getEmail().replace(".", "_").replace("@", "_");

        // Cpger los datos en la base de datos Firebase
        nodoUsuario.child(emailKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot capturaSnapshot) {
                // dataSnapshot aquí contiene TODO el contenido del nodo del email en el momento de la lectura. -> Foto
                if (capturaSnapshot.exists()) {
                    //Correo
                    String correo = capturaSnapshot.child("correo").getValue(String.class);
                    if (correo != null) {
                        tvCorreoUsuarioInforme.setText(correo);
                    } else {
                        tvCorreoUsuarioInforme.setText("Correo no disponible");
                    }
                    //Nombre
                    String nombre = capturaSnapshot.child("nombre").getValue(String.class);
                    if (nombre != null) {
                        tvNombreUsuarioInforme.setText(nombre);
                    } else {
                        tvNombreUsuarioInforme.setText("Nombre no disponible");
                    }
                    //Obtener los datos personales
                    DataSnapshot capturaDatosPersonalesSnapshot = capturaSnapshot.child("datosPersonales");
                    if (capturaDatosPersonalesSnapshot.exists()) {
                        // Rellenar los campos comunes
                        String eleccion = capturaDatosPersonalesSnapshot.child("eleccion").getValue(String.class);
                        String ingresoBruto = capturaDatosPersonalesSnapshot.child("ingresoBruto").getValue(String.class);
                        String edad = capturaDatosPersonalesSnapshot.child("edad").getValue(String.class);
                        String personasACargo = capturaDatosPersonalesSnapshot.child("personasACargo").getValue(String.class);
                        Boolean vivienda = capturaDatosPersonalesSnapshot.child("vivienda").getValue(Boolean.class);

                        // Para tvIngresoPregComunes
                        if (ingresoBruto != null) {
                            tvIngresoPregComunes.setText(ingresoBruto);
                        } else {
                            tvIngresoPregComunes.setText("");
                        }

                        //Lo mismo que antes pero con operadores ternarios
                        tvEdadPregComunes.setText(edad != null ? edad : "");
                        tvPersonasACargoPregComunes.setText(personasACargo != null ? personasACargo : "");
                        if (vivienda != null) {
                            // Si 'vivienda' es true
                            tvViviendaPropiedadAd.setText(vivienda ? "Sí" : "No");
                        } else {
                            // Si 'vivienda' es false
                            tvViviendaPropiedadAd.setText("");
                        }
                        //Situación actual
                        if (eleccion != null) {
                            spinner_situacion.setText(eleccion);
                            vistaEspecializada(eleccion, capturaDatosPersonalesSnapshot);
                        } else {
                            spinner_situacion.setText("Situación no definida.");
                            vistaEspecializada(null, null);
                        }
                    } else {
                        Toast.makeText(informacionAdicional.this, "No se encontraron datos personales ", Toast.LENGTH_SHORT).show();
                        vistaEspecializada(null, null);
                    }
                } else {
                    Toast.makeText(informacionAdicional.this, "Usuario no encontrado en la base de datos.", Toast.LENGTH_SHORT).show();
                    tvNombreUsuarioInforme.setText("Usuario no encontrado");
                    tvCorreoUsuarioInforme.setText("Correo no encontrado");
                    // Ocultar todos los layouts si no hay datos
                    vistaEspecializada(null, null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(informacionAdicional.this, "Error al cargar los datos.", Toast.LENGTH_SHORT).show();
                tvNombreUsuarioInforme.setText("Error de carga");
                tvCorreoUsuarioInforme.setText("Error de carga");
                vistaEspecializada(null, null);
            }
        });
    }

    private void vistaEspecializada(String situacionSelecionada, DataSnapshot capturaDatosPersonalesSnapshot) {
        // Escondemos vistas especializadas
        layoutAsalariadoPerfilar.setVisibility(View.GONE);
        layoutAutonomoPerfilar.setVisibility(View.GONE);
        layoutEmpresarioPerfilar.setVisibility(View.GONE);
        layoutEstudiantePerfilar.setVisibility(View.GONE);
        layoutJubiladoPerfilar.setVisibility(View.GONE);

        if (situacionSelecionada == null || capturaDatosPersonalesSnapshot == null) {
            // Si no hay situación seleccionada o datos, ocultamos todo
            return;
        }

        // Enseñar layout en función de la seleción y rellenar los datos
        switch (situacionSelecionada) {
            case "Asalariado":
                layoutAsalariadoPerfilar.setVisibility(View.VISIBLE);
                spinner_contrato.setText(capturaDatosPersonalesSnapshot.child("tipoContrato").getValue(String.class));
                Boolean familiaNumerosa = capturaDatosPersonalesSnapshot.child("familiaNumerosa").getValue(Boolean.class);
                if (familiaNumerosa != null) {
                    tvFamiliaNumerosaAd.setText(familiaNumerosa ? "Sí" : "No");
                } else {
                    tvFamiliaNumerosaAd.setText("");
                }
                tvEdadHijosAsala.setText(capturaDatosPersonalesSnapshot.child("edadesHijos").getValue(String.class));
                tvgastosEscolaresAsala.setText(capturaDatosPersonalesSnapshot.child("gastosEscolares").getValue(String.class));
                break;
            case "Autónomo":
                layoutAutonomoPerfilar.setVisibility(View.VISIBLE);
                tvDate.setText(capturaDatosPersonalesSnapshot.child("fechaAlta").getValue(String.class));
                tvactividadEconomica.setText(capturaDatosPersonalesSnapshot.child("actividad").getValue(String.class));
                tvgastosDeducibles.setText(capturaDatosPersonalesSnapshot.child("gastosDeducibles").getValue(String.class));
                tvivaSoportado.setText(capturaDatosPersonalesSnapshot.child("ivaSoportado").getValue(String.class));
                tvivaRepercutido.setText(capturaDatosPersonalesSnapshot.child("ivaRepercutido").getValue(String.class));
                Boolean vehiculo = capturaDatosPersonalesSnapshot.child("vehiculo").getValue(Boolean.class);
                if (vehiculo != null) {
                    tvCocheAutonomoAd.setText(vehiculo ? "Sí" : "No");
                } else {
                    tvCocheAutonomoAd.setText("");
                }
                break;
            case "Empresario":
                layoutEmpresarioPerfilar.setVisibility(View.VISIBLE);
                spinnerTipoempresa.setText(capturaDatosPersonalesSnapshot.child("tipoContrato").getValue(String.class));
                tvingreso.setText(capturaDatosPersonalesSnapshot.child("facturacionEmpresa").getValue(String.class));
                tvsueldoAdmin.setText(capturaDatosPersonalesSnapshot.child("sueldoAdministrador").getValue(String.class));
                tvempleadosNum.setText(capturaDatosPersonalesSnapshot.child("empleados").getValue(String.class));
                tvgastosDeduciblesEmp.setText(capturaDatosPersonalesSnapshot.child("gastosDeduciblesEmpresa").getValue(String.class));
                break;
            case "Estudiante":
                layoutEstudiantePerfilar.setVisibility(View.VISIBLE);
                spiner_estudios.setText(capturaDatosPersonalesSnapshot.child("tipoEstudios").getValue(String.class));
                Boolean trabajaParcial = capturaDatosPersonalesSnapshot.child("trabaja").getValue(Boolean.class);
                tvestudiosBeca.setText(capturaDatosPersonalesSnapshot.child("becaCantidad").getValue(String.class));
                if (trabajaParcial != null) {
                    tvTrabajaParcialEstudianteAd.setText(trabajaParcial ? "Sí" : "No");
                } else {
                    tvTrabajaParcialEstudianteAd.setText("");
                }

                break;
            case "Jubilado":
                layoutJubiladoPerfilar.setVisibility(View.VISIBLE);
                tvcobroPension.setText(capturaDatosPersonalesSnapshot.child("pensionAnual").getValue(String.class));
                tvgastosMedicos.setText(capturaDatosPersonalesSnapshot.child("gastosMedicos").getValue(String.class));
                Boolean rgsegundaVivienda = capturaDatosPersonalesSnapshot.child("segundaVivienda").getValue(Boolean.class);
                if (rgsegundaVivienda != null) {
                    tvSegundaViviendaAd.setText(rgsegundaVivienda ? "Sí" : "No");
                } else {
                    tvSegundaViviendaAd.setText("");
                }
                break;
            default:
                // No hacer nada o mostrar un mensaje si la situación no se reconoce
                break;
        }
    }
}