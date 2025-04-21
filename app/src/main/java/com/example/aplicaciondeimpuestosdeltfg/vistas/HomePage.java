package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.CalendarManager;
import com.example.aplicaciondeimpuestosdeltfg.gestor.EventDecorator;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.example.aplicaciondeimpuestosdeltfg.gestor.MultipleEventsDecorator;
import com.example.aplicaciondeimpuestosdeltfg.gestor.ViewPager2Adapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class HomePage extends Fragment {
    private MaterialCalendarView calendarView;
    private ViewPager2 viewPager2;
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

    public void calendarConfiguration(){
        // Clasificar eventos por tipo
        HashSet<CalendarDay> gastos = new HashSet<>();
        HashSet<CalendarDay> cobros = new HashSet<>();
        HashSet<CalendarDay> multiplesEventos = new HashSet<>();

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

            if (gastos.contains(dia) && cobros.contains(dia)) {
                multiplesEventos.add(dia);
            }
        }

        // Quitar días con múltiples eventos de los conjuntos individuales
        gastos.removeAll(multiplesEventos);
        cobros.removeAll(multiplesEventos);

        int[] colores = {Color.RED, Color.GREEN};

        // Decorar el calendario
        calendarView.addDecorator(new EventDecorator(Color.RED, gastos));  // Decorador para los GASTOS
        calendarView.addDecorator(new EventDecorator(Color.GREEN, cobros)); // Decorador para los COBROS
        calendarView.addDecorator(new MultipleEventsDecorator(multiplesEventos, colores));
    }

    public void viewPager2Configuration(){
        List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");

        ViewPager2Adapter adapter = new ViewPager2Adapter(meses);
        viewPager2.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        calendarView = view.findViewById(R.id.calendarHomePage);
        viewPager2 = view.findViewById(R.id.homePageMeses);

        listaEventos = new ArrayList<>();
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 18), Evento.Tipo.GASTO)); // 18 abril
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 5, 18), Evento.Tipo.COBRO)); // mismo día
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 20), Evento.Tipo.GASTO)); // otro día
        listaEventos.add(new Evento(calendarManager.getCalendar(2025, 3, 20), Evento.Tipo.COBRO)); // otro día

        calendarConfiguration();
        viewPager2Configuration();
        return view;
    }
}