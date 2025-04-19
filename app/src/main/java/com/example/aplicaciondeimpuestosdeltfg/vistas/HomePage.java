package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.CalendarManager;
import com.example.aplicaciondeimpuestosdeltfg.gestor.EventDecorator;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class HomePage extends Fragment {
    private MaterialCalendarView calendarView;
    private List<Evento> listaEventos;
    private CalendarManager calendarManager = new CalendarManager();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomePage() {
    }
    public static HomePage newInstance(String param1, String param2) {
        HomePage fragment = new HomePage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        calendarView = view.findViewById(R.id.calendarHomePage);

        listaEventos = new ArrayList<>();
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 18), Evento.Tipo.GASTO)); // 18 abril
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 5, 18), Evento.Tipo.COBRO)); // mismo día
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 20), Evento.Tipo.GASTO)); // otro día
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 20), Evento.Tipo.COBRO)); // otro día


        // Clasificar eventos por tipo
        HashSet<CalendarDay> gastos = new HashSet<>();
        HashSet<CalendarDay> cobros = new HashSet<>();
        List<CalendarDay> multipleEventsDays = new ArrayList<>();

        for (Evento evento : listaEventos) {
            CalendarDay dia = CalendarDay.from(
                    evento.getFecha().get(Calendar.YEAR),
                    evento.getFecha().get(Calendar.MONTH),
                    evento.getFecha().get(Calendar.DAY_OF_MONTH)
            );

            if (evento.getTipo() == Evento.Tipo.GASTO) {
                gastos.add(dia); // Añadir al conjunto de gastos
            } else {
                cobros.add(dia); // Añadir al conjunto de cobros
            }

            // Si un día tiene tanto un cobro como un gasto, lo añadimos a la lista de múltiples eventos
            if (gastos.contains(dia) && cobros.contains(dia) && !multipleEventsDays.contains(dia)) {
                multipleEventsDays.add(dia);
            }
        }

        // Decorar el calendario
        calendarView.addDecorator(new EventDecorator(Color.RED, gastos, multipleEventsDays));  // Decorador para los GASTOS
        calendarView.addDecorator(new EventDecorator(Color.GREEN, cobros, multipleEventsDays)); // Decorador para los COBROS


        return view;
    }
}