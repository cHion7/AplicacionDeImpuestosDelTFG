package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.app.DatePickerDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.CalendarManager;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddEvent extends BottomSheetDialogFragment {
    private int anioSeleccionado;
    private int mesSeleccionado;
    private int diaSeleccionado;
    private TextView datePicker;
    private TextView guardar;
    private TextView cancelar;
    private RadioGroup radioGroup;
    private Spinner spinner;
    private TextView titulo;
    private TextView descripcion;
    private TextView dinero;
    private Switch repetir;
    private String cobroOGasto = "GASTO"; // valor por defecto
    private Map<Integer, String> mesNombre = new HashMap<Integer, String>() {{
        put(1, "Enero");
        put(2, "Febrero");
        put(3, "Marzo");
        put(4, "Abril");
        put(5, "Mayo");
        put(6, "Junio");
        put(7, "Julio");
        put(8, "Agosto");
        put(9, "Septiembre");
        put(10, "Octubre");
        put(11, "Noviembre");
        put(12, "Diciembre");
    }};
    private List<String> categoriasGastos = Arrays.asList(
            "Vivienda", "Transporte", "Alimentacion", "Salud", "Educacion", "Ocio", "Ropa y Calzado", "Seguros", "Impuestos y Tasas", "Otros"
    );
    private List<String> categoriasCobros = Arrays.asList(
            "Salario", "Ingresos Extras", "Inversiones", "Ventas", "Rentas", "Prestaciones y Subsidios", "Devoluciones", "Premios - Lotería", "Regalos - Donaciones"
    );
    private CalendarManager calendarManager = new CalendarManager();

    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String mParam1;
    private String mParam2;

    public AddEvent() {
    }


    public static AddEvent newInstance(String param1, String param2) {
        AddEvent fragment = new AddEvent();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static AddEvent newInstance() {
        return new AddEvent();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    public void subirEventoNube(Evento evento) {
        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        DatabaseReference eventosRef = usuariosReferencia.child(emailUser.replace("@", "_").replace(".", "_")).child("eventos");

        // Generar referencia con clave única
        DatabaseReference nuevaRef = eventosRef.push();
        String clave = nuevaRef.getKey(); // Obtener clave generada

        // Asignar clave al objeto Evento
        evento.setKey(clave);

        // Subir evento con la clave incluida
        nuevaRef.setValue(evento);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        datePicker = view.findViewById(R.id.tvFechaAddEvents);
        radioGroup = view.findViewById(R.id.rgGastosCobros);
        guardar = view.findViewById(R.id.tvGuardarAddEvento);
        cancelar = view.findViewById(R.id.tvCancelarAddEvents);
        spinner = view.findViewById(R.id.spCategoriasAddEventos);
        titulo = view.findViewById(R.id.tvTituloEvento);
        descripcion = view.findViewById(R.id.tvMultilineDescripcionAddEvents);
        dinero = view.findViewById(R.id.tvCantDineroAddEvento);
        repetir = view.findViewById(R.id.repetir);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            List<String> categoriasSeleccionadas;

            if (checkedId == R.id.rbGastoAddEvento) {
                cobroOGasto = "GASTO";
                categoriasSeleccionadas = categoriasGastos;
            } else if (checkedId == R.id.rbCobroAddEvento) {
                cobroOGasto = "COBRO";
                categoriasSeleccionadas = categoriasCobros;
            } else {
                categoriasSeleccionadas = new ArrayList<>();
            }

            // Actualiza el Spinner
            ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoriasSeleccionadas);
            adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterCategorias);
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Evento evento = new Evento(
                        dinero.getText().toString(),
                        cobroOGasto,
                        spinner.getSelectedItem().toString(),
                        titulo.getText().toString(),
                        descripcion.getText().toString(),
                        calendarManager.getCalendar(anioSeleccionado, mesSeleccionado, diaSeleccionado));
                Log.d("AddEvento", "Tipo: " + cobroOGasto);
                if (repetir.isChecked()) {
                    Calendar fechaOriginal = calendarManager.getCalendar(anioSeleccionado, mesSeleccionado, diaSeleccionado);
                    int dia = fechaOriginal.get(Calendar.DAY_OF_MONTH);
                    int anio = fechaOriginal.get(Calendar.YEAR);

                    for (int mes = 1; mes <= 12; mes++) {
                        Calendar fechaRepetida = Calendar.getInstance();
                        fechaRepetida.set(anio, mes - 1, dia);

                        Evento eventoRepetido = new Evento(
                                dinero.getText().toString(),
                                cobroOGasto,
                                spinner.getSelectedItem().toString(),
                                titulo.getText().toString(),
                                descripcion.getText().toString(),
                                fechaRepetida
                        );
                        subirEventoNube(eventoRepetido);
                    }
                } else {
                    subirEventoNube(evento);
                }
                dismiss();
            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
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
                int alturaDeseada = (int) (alturaPantalla * 0.95);

                // Establecer la altura y el comportamiento del BottomSheet
                view.getLayoutParams().height = alturaDeseada;
                view.requestLayout();
                comportamiento.setPeekHeight(alturaDeseada);
                comportamiento.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }
}