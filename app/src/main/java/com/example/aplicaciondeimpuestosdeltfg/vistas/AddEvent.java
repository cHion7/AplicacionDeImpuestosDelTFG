package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddEvent extends BottomSheetDialogFragment {
    private int anioSeleccionado;
    private int mesSeleccionado;
    private int diaSeleccionado;
    private TextView datePicker;
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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public AddEvent() {
    }


    public static AddEvent newInstance(String param1, String param2) {
        AddEvent fragment = new AddEvent();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_event, container, false);
        datePicker = view.findViewById(R.id.tvFechaAddEvents);
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
                        mesSeleccionado = month + 1;
                        diaSeleccionado = dayOfMonth;

                        // Actualizar el texto del TextView
                        datePicker.setText(diaSeleccionado + " de " + mesNombre.get(mesSeleccionado) + " de " + diaSeleccionado);
                    },
                    anio, mes, dia
            );
            datePickerDialog.show();
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