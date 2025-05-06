package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.CalendarManager;
import com.example.aplicaciondeimpuestosdeltfg.gestor.EventDecorator;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.example.aplicaciondeimpuestosdeltfg.gestor.MultipleEventsDecorator;
import com.example.aplicaciondeimpuestosdeltfg.gestor.ViewPager2Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
    private ViewPager2Adapter adapter;
    private ImageButton addEventos;
    private List<Evento> listaEventos;
    private CalendarManager calendarManager = new CalendarManager();
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

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

    public void cargarEventos(){
        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        String userPath = emailUser.replace("@", "_").replace(".", "_");
        Log.d("RutaFirebase", "Ruta: Usuarios/" + userPath + "/eventos");
        DatabaseReference eventosRef = usuariosReferencia.child(userPath).child("eventos");
        eventosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEventos.clear();
                if(snapshot.exists()){
                    for (DataSnapshot productoSnapshot : snapshot.getChildren()) {
                        Evento evento = productoSnapshot.getValue(Evento.class);
                        if (evento != null) {
                            listaEventos.add(evento);
                        }
                    }
                }
                Log.d("Eventos", "Eventos totales: " + listaEventos.size());

                // Refresca el calendario y el adapter
                calendarConfiguration();
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        List<String> meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
        calendarView = view.findViewById(R.id.calendarHomePage);
        viewPager2 = view.findViewById(R.id.homePageMeses);
        addEventos = view.findViewById(R.id.ibMasEventosHomePage);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        listaEventos = new ArrayList<>();
        adapter = new ViewPager2Adapter(meses, listaEventos);
        viewPager2.setAdapter(adapter);
        cargarEventos();

        addEventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEvent fragmento = AddEvent.newInstance();
                fragmento.show(((FragmentActivity) getContext()).getSupportFragmentManager(), fragmento.getTag());
            }
        });
        return view;
    }
}