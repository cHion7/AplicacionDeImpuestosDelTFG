package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.example.aplicaciondeimpuestosdeltfg.gestor.ViewPager2Adapter;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link setImpuestosTime#newInstance} factory method to
 * create an instance of this fragment.
 */
public class setImpuestosTime extends BottomSheetDialogFragment {
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String eleccion;
    private LinearLayout llIRPFAutonomo, llIVAAutonomo, llIRPFAsalariado, llIRPFEstudiante, llIRPFJubilado, llISEmpresario, llIRPFEmpresario, llRetenEmpresario;
    private TextView tvIRPFAutonomo, tvIRPFAutonomoDate, tvIVAAutonomo, tvIVAAutonomoDate, tvIRPFAsalariado, tvIRPFAsalariadoDate, tvIRPFEstudiante, tvIRPFEstudianteDate, tvIRPFJubilado, tvIRPFJubiladoDate, tvISEmpresario, tvISEmpresarioDate, tvIRPFEmpresario, tvIRPFEmpresarioDate, tvRetenEmpresario, tvRetenEmpresarioDate, tvCerrar;
    private CardView cvIRPFAutonomo, cvIVAAutonomo, cvIRPFAsalariado, cvIRPFEstudiante, cvIRPFJubilado, cvISEmpresario, cvIRPFEmpresario, cvRetencionesEmpresario;
    private DatabaseReference ref;
    //Variables que vamos a utilizar en diferentes perfiles
    private Double ingresoBruto;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public setImpuestosTime() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment setImpuestosTime.
     */
    // TODO: Rename and change types and number of parameters
    public static setImpuestosTime newInstance(String param1, String param2) {
        setImpuestosTime fragment = new setImpuestosTime();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static setImpuestosTime newInstance() {
        return new setImpuestosTime();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    private void establecerVisibilidad() {

        cvIRPFAutonomo.setVisibility(View.GONE);
        cvIVAAutonomo.setVisibility(View.GONE);
        cvIRPFJubilado.setVisibility(View.GONE);
        cvISEmpresario.setVisibility(View.GONE);
        cvIRPFEmpresario.setVisibility(View.GONE);
        cvRetencionesEmpresario.setVisibility(View.GONE);
        // Ocultar todos los LinearLayouts
        llIRPFAutonomo.setVisibility(View.GONE);
        llIVAAutonomo.setVisibility(View.GONE);
        llIRPFAsalariado.setVisibility(View.GONE);
        llIRPFEstudiante.setVisibility(View.GONE);
        llIRPFJubilado.setVisibility(View.GONE);
        llISEmpresario.setVisibility(View.GONE);
        llIRPFEmpresario.setVisibility(View.GONE);
        llRetenEmpresario.setVisibility(View.GONE);

        // Ocultar todos los TextView por si los reutilizas
        tvIRPFAutonomo.setVisibility(View.GONE);
        tvIRPFAutonomoDate.setVisibility(View.GONE);
        tvIVAAutonomo.setVisibility(View.GONE);
        tvIVAAutonomoDate.setVisibility(View.GONE);
        tvIRPFAsalariado.setVisibility(View.GONE);
        tvIRPFAsalariadoDate.setVisibility(View.GONE);
        tvIRPFEstudiante.setVisibility(View.GONE);
        tvIRPFEstudianteDate.setVisibility(View.GONE);
        tvIRPFJubilado.setVisibility(View.GONE);
        tvIRPFJubiladoDate.setVisibility(View.GONE);
        tvISEmpresario.setVisibility(View.GONE);
        tvISEmpresarioDate.setVisibility(View.GONE);
        tvIRPFEmpresario.setVisibility(View.GONE);
        tvIRPFEmpresarioDate.setVisibility(View.GONE);
        tvRetenEmpresario.setVisibility(View.GONE);
        tvRetenEmpresarioDate.setVisibility(View.GONE);

        if (eleccion == null) return;

        switch (eleccion) {
            case "Autónomo":
                cvIRPFAutonomo.setVisibility(View.VISIBLE);
                cvIVAAutonomo.setVisibility(View.VISIBLE);
                llIRPFAutonomo.setVisibility(View.VISIBLE);
                llIVAAutonomo.setVisibility(View.VISIBLE);

                //Calculo del IRPF Anual estimado
                tvIRPFAutonomo.setVisibility(View.VISIBLE);
                tvIRPFAutonomo.setText("IRPF Anual (modelo 130)");
                tvIRPFAutonomoDate.setVisibility(View.VISIBLE);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String ingresoBrutoString = snapshot.child("ingresoBruto").getValue(String.class);
                            Log.d("Firebase", "Valor de 'ingresoBruto': " + ingresoBruto);
                            ingresoBruto = Double.parseDouble(ingresoBrutoString);
                            Double irpfAnualEstimado = ingresoBruto * 0.15;
                            tvIRPFAutonomoDate.setText("Estimación: " + irpfAnualEstimado);
                            Double ivaAnualEstimado = ingresoBruto * 0.21;
                            tvIVAAutonomoDate.setText("Estimación: " + ivaAnualEstimado);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error al leer datos: " + error.getMessage());
                    }
                });


                tvIVAAutonomo.setVisibility(View.VISIBLE);
                tvIVAAutonomo.setText("IVA Anual (modelo 303, 390)");
                tvIVAAutonomoDate.setVisibility(View.VISIBLE);
                break;

            case "asalariado":
                llIRPFAsalariado.setVisibility(View.VISIBLE);

                tvIRPFAsalariado.setVisibility(View.VISIBLE);
                tvIRPFAsalariado.setText("IRPF en nómina");
                tvIRPFAsalariadoDate.setVisibility(View.VISIBLE);
                break;

            case "estudiante":
                llIRPFEstudiante.setVisibility(View.VISIBLE);

                tvIRPFEstudiante.setVisibility(View.VISIBLE);
                tvIRPFEstudiante.setText("IRPF reducido o exento");
                tvIRPFEstudianteDate.setVisibility(View.VISIBLE);
                break;

            case "jubilado":
                llIRPFJubilado.setVisibility(View.VISIBLE);

                tvIRPFJubilado.setVisibility(View.VISIBLE);
                tvIRPFJubilado.setText("IRPF por pensión");
                tvIRPFJubiladoDate.setVisibility(View.VISIBLE);
                break;

            case "empresario":

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String facturacionEmpresaString = snapshot.child("facturacionEmpresa").getValue(String.class);
                            String ingresoBrutoString = snapshot.child("ingresoBruto").getValue(String.class);
                            String empleadosString = snapshot.child("empleados").getValue(String.class);

                            Log.d("Firebase", "Valor de 'ingresoBruto': " + ingresoBruto);
                            Double facturacionEmpresa = Double.parseDouble(facturacionEmpresaString);
                            ingresoBruto = Double.parseDouble(ingresoBrutoString);
                            int empleados = Integer.parseInt(empleadosString);

                            double impuestoSociedades = ingresoBruto * 0.25;
                            double retencionesTrabajadores = facturacionEmpresa * 0.05; // estimación mensual de IRPF retenido
                            double segurosSociales = empleados * 300 * 12;

                            tvISEmpresario.setText("Estimación: " + impuestoSociedades);
                            tvIRPFEmpresario.setText("Estimación: " + segurosSociales);
                            tvRetenEmpresario.setText("Estimación: " + retencionesTrabajadores);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Firebase", "Error al leer datos: " + error.getMessage());
                    }
                });

                llISEmpresario.setVisibility(View.VISIBLE);
                llIRPFEmpresario.setVisibility(View.VISIBLE);
                llRetenEmpresario.setVisibility(View.VISIBLE);

                tvISEmpresario.setVisibility(View.VISIBLE);
                tvISEmpresario.setText("Impuesto de Sociedades (modelo 200)");
                tvISEmpresarioDate.setVisibility(View.VISIBLE);

                tvIRPFEmpresario.setVisibility(View.VISIBLE);
                tvIRPFEmpresario.setText("Seguridad social de los empleados");
                tvIRPFEmpresarioDate.setVisibility(View.VISIBLE);
                tvRetenEmpresario.setVisibility(View.VISIBLE);
                tvRetenEmpresario.setText("Retenciones IRPF a empleados (modelo 111)");
                tvRetenEmpresarioDate.setVisibility(View.VISIBLE);
                break;
        }
    }

    /*
            datePicker.setOnClickListener(v -> {
            // Obtener la fecha actual como predeterminada
            Calendar calendarioActual = Calendar.getInstance();
            int anio = calendarioActual.get(Calendar.YEAR);
            int mes = calendarioActual.get(Calendar.MONTH);
            int dia = calendarioActual.get(Calendar.DAY_OF_MONTH);

            // Mostrar el DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view1, year, month, dayOfMonth) -> {
                        anioSeleccionado = year;
                        mesSeleccionado = month;
                        diaSeleccionado = dayOfMonth;

                        // Actualizar el texto del TextView
                        datePicker.setText(diaSeleccionado + " de " + mesNombre.get(mesSeleccionado + 1) + " de " + anioSeleccionado);
                    },
                    anio, mes, dia
            );
            datePickerDialog.show();
        });
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_impuestos_time, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        tvCerrar = view.findViewById(R.id.tvCerrarSetImpuestos);

        //Cardviews necesarios para la visibilidad de la tarjeta
        cvIRPFJubilado = view.findViewById(R.id.cvIRPFJubilado);
        cvISEmpresario = view.findViewById(R.id.cvISEmpresario);
        cvIRPFEmpresario = view.findViewById(R.id.cvIRPFEmpresario);
        cvRetencionesEmpresario = view.findViewById(R.id.cvRetencionesEmpresario);
        cvIRPFAutonomo = view.findViewById(R.id.cvIRPFAutonomo);
        cvIVAAutonomo = view.findViewById(R.id.cvIVAAutonomo);
        //Layouts necesarios para definir la visibilidad
        llIRPFAutonomo = view.findViewById(R.id.llIRPFAutonomo);
        llIVAAutonomo = view.findViewById(R.id.llIVAAutonomo);
        llIRPFAsalariado = view.findViewById(R.id.llIRPFAsalariado);
        llIRPFEstudiante = view.findViewById(R.id.llIRPDFEstudiante);
        llIRPFJubilado = view.findViewById(R.id.llIRPFJubilado);
        llISEmpresario = view.findViewById(R.id.llISEmpresario);
        llIRPFEmpresario = view.findViewById(R.id.llIRPFEmpresario);
        llRetenEmpresario = view.findViewById(R.id.llRetenEmpresario);


        //TextViews y Dates Necesarios para definir la fecha
        tvIRPFAutonomo = view.findViewById(R.id.tvIRPFAutonomo);
        tvIRPFAutonomoDate = view.findViewById(R.id.tvIRPFAutonomoDate);
        tvIVAAutonomo = view.findViewById(R.id.tvIVAAutonomo);
        tvIVAAutonomoDate = view.findViewById(R.id.tvIVAAutonomoDate);

        tvIRPFAsalariado = view.findViewById(R.id.tvIRPFAsalariado);
        tvIRPFAsalariadoDate = view.findViewById(R.id.tvIRPFAsalariadoDate);

        tvIRPFEstudiante = view.findViewById(R.id.tvIRPFEstudiante);
        tvIRPFEstudianteDate = view.findViewById(R.id.tvIRPFEstudianteDate);

        tvIRPFJubilado = view.findViewById(R.id.tvIRPFJubilado);
        tvIRPFJubiladoDate = view.findViewById(R.id.tvIRPFJubiladoDate);

        tvISEmpresario = view.findViewById(R.id.tvISEmpresario);
        tvISEmpresarioDate = view.findViewById(R.id.tvISEmpresarioDate);
        tvIRPFEmpresario = view.findViewById(R.id.tvIRPFEmpresario);
        tvIRPFEmpresarioDate = view.findViewById(R.id.tvIRPFEmpresarioDate);
        tvRetenEmpresario = view.findViewById(R.id.tvRetenEmpresario);
        tvRetenEmpresarioDate = view.findViewById(R.id.tvRetenEmpresarioDate);


        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        String userPath = emailUser.replace("@", "_").replace(".", "_");
        ref = usuariosReferencia.child(userPath).child("datosPersonales");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    eleccion = snapshot.child("eleccion").getValue(String.class);
                    Log.d("Firebase", "Valor de 'eleccion': " + eleccion);
                    establecerVisibilidad();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al leer datos: " + error.getMessage());
            }
        });

        tvCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Modificar la altura del BottomSheet al iniciar el fragmento
        BottomSheetDialog vista = (BottomSheetDialog) getDialog();
        if (vista != null) {
            View view = vista.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (view != null) {
                BottomSheetBehavior<View> comportamiento = BottomSheetBehavior.from(view);

                // Obtener la altura de la pantalla y calcular la altura deseada del BottomSheet
                int alturaPantalla = getResources().getDisplayMetrics().heightPixels;
                int alturaDeseada = (int) (alturaPantalla * 0.50);

                // Establecer la altura y el comportamiento del BottomSheet
                view.getLayoutParams().height = alturaDeseada;
                view.requestLayout();
                comportamiento.setPeekHeight(alturaDeseada);
                comportamiento.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}