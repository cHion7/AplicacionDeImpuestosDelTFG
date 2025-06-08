package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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
    private List<String> meses;
    private CalendarManager calendarManager = new CalendarManager();
    private TextView tvSaldo;
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

    public void calendarConfiguration() {
        HashSet<CalendarDay> gastos = new HashSet<>();
        HashSet<CalendarDay> cobros = new HashSet<>();
        HashSet<CalendarDay> multiplesEventos = new HashSet<>();

        for (Evento evento : listaEventos) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(evento.getFechaMillis());

            CalendarDay dia = CalendarDay.from(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            if ("GASTO".equalsIgnoreCase(evento.getCobroOGasto())) {
                gastos.add(dia);
            } else if ("COBRO".equalsIgnoreCase(evento.getCobroOGasto())) {
                cobros.add(dia);
            }
        }

        for (CalendarDay dia : new HashSet<>(gastos)) {
            if (cobros.contains(dia)) {
                multiplesEventos.add(dia);
            }
        }

        gastos.removeAll(multiplesEventos);
        cobros.removeAll(multiplesEventos);

        int[] colores = {Color.RED, Color.GREEN};

        calendarView.addDecorator(new EventDecorator(Color.RED, gastos));
        calendarView.addDecorator(new EventDecorator(Color.GREEN, cobros));
        calendarView.addDecorator(new MultipleEventsDecorator(multiplesEventos, colores));
        calendarView.invalidateDecorators();
    }
    public void cargarEventos(){
        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        String userPath = emailUser.replace("@", "_").replace(".", "_");
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

                            long fecha = evento.getFechaMillis();
                            Log.d("FECHAS", "Evento: " + evento.getCobroOGasto() + " | fechaMillis: " + fecha);
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(fecha);
                            Log.d("FECHAS", "Fecha convertida: " + calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                                    (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
                        }
                    }

                }

                if (!isAdded()) {
                    return;
                }
                // Aquí, SOLO después de cargar datos, crea y asigna el adapter:
                adapter = new ViewPager2Adapter(meses, listaEventos, getParentFragmentManager());
                viewPager2.setAdapter(adapter);
                viewPager2.setCurrentItem(5, false);

                // Refresca el calendario y demás UI
                calendarView.removeDecorators();
                calendarConfiguration();

                calcularSaldoActual();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error cargando eventos: " + error.getMessage());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        meses = Arrays.asList("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");

        calendarView = view.findViewById(R.id.calendarHomePage);
        viewPager2 = view.findViewById(R.id.homePageMeses);
        addEventos = view.findViewById(R.id.ibBackListaEventos);
        tvSaldo = view.findViewById(R.id.tvNumeroSaldoHomePage);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        listaEventos = new ArrayList<>();

        // NO crees el adapter ni asignes aquí, lo harás en cargarEventos después de recibir datos.

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
    private void calcularSaldoActual() {
        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        String userPath = emailUser.replace("@", "_").replace(".", "_");
        DatabaseReference ref = usuariosReferencia.child(userPath);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.e("SaldoDebug", "Snapshot del usuario no existe.");
                    return;
                }

                try {
                    Double saldo = snapshot.child("saldoInicial").getValue(Double.class);
                    Long fechaInicio = snapshot.child("tiempoSaldoInicial").getValue(Long.class);
                    if (fechaInicio == null){
                        tvSaldo.setText(String.format("%.2f €", 0));
                        tvSaldo.setTextColor(ContextCompat.getColor(getContext(), R.color.naranja));
                    }

                    if (saldo == null || fechaInicio == null) {
                        Log.e("SaldoDebug", "Datos faltantes: saldoInicial o tiempoSaldoInicial es null.");
                        return;
                    }

                    long ahora = System.currentTimeMillis();
                    Log.d("SaldoDebug", "Saldo Inicial: " + saldo);
                    Log.d("SaldoDebug", "Fecha Inicial: " + fechaInicio);
                    Log.d("SaldoDebug", "Fecha Actual: " + ahora);

                    double saldoInicial = saldo;
                    double saldoActual = saldoInicial;

                    for (Evento evento : listaEventos) {
                        long fechaEvento = evento.getFechaMillis();
                        String tipo = evento.getCobroOGasto();
                        String cantidadStr = evento.getDinero();

                        Log.d("SaldoDebug", "Evento: " + cantidadStr + " - " + tipo + " - Fecha: " + fechaEvento);

                        if (fechaEvento >= fechaInicio && fechaEvento <= ahora) {
                            try {
                                double cantidad = Double.parseDouble(cantidadStr);
                                if ("COBRO".equalsIgnoreCase(tipo)) {
                                    saldoActual += cantidad;
                                } else if ("GASTO".equalsIgnoreCase(tipo)) {
                                    saldoActual -= cantidad;
                                }
                                Log.d("SaldoDebug", "Saldo tras evento: " + saldoActual);
                            } catch (NumberFormatException e) {
                                Log.e("SaldoDebug", "Cantidad inválida en evento: " + cantidadStr);
                            }
                        } else {
                            Log.d("SaldoDebug", "Evento fuera de rango: " + fechaEvento);
                        }
                    }

                    if (!isAdded() || getContext() == null || tvSaldo == null) {
                        Log.w("SaldoDebug", "Fragmento no montado o TextView nulo.");
                        return;
                    }

<<<<<<< HEAD
=======
                   /* if(saldoActual = null){

                    }*/
>>>>>>> origin/Hui9
                    tvSaldo.setText(String.format("%.2f €", saldoActual));

                    if (saldoActual < 0) {
                        tvSaldo.setTextColor(ContextCompat.getColor(getContext(), R.color.rojo)); // negativo
                    } else {
                        tvSaldo.setTextColor(ContextCompat.getColor(getContext(), R.color.verde)); // positivo
                    }

                    Log.d("Saldo", "Saldo final calculado: " + saldoActual);
                } catch (Exception e) {
                    Log.e("SaldoDebug", "Error general al calcular saldo", e);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error al obtener datos del usuario", error.toException());
            }
        });
    }
}