package com.example.aplicaciondeimpuestosdeltfg.vistas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.example.aplicaciondeimpuestosdeltfg.gestor.Evento;
import com.example.aplicaciondeimpuestosdeltfg.gestor.ListaEventosAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListaEventos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListaEventos extends Fragment {
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView recyclerView;
    private ImageButton atras;
    private List<Evento> listaEventos;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListaEventos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListaEventos.
     */
    // TODO: Rename and change types and number of parameters
    public static ListaEventos newInstance(String param1, String param2) {
        ListaEventos fragment = new ListaEventos();
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
    private void cogerDatos() {
        db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
        String emailUser = user.getEmail();
        String userPath = emailUser.replace("@", "_").replace(".", "_");
        DatabaseReference eventosRef = usuariosReferencia.child(userPath).child("eventos");

        String tipoSeleccionado = getArguments() != null ? getArguments().getString("tipo") : null;
        int mesSeleccionado = getArguments() != null ? getArguments().getInt("mes", -1) : -1;

        eventosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listaEventos.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot eventoSnapshot : snapshot.getChildren()) {
                        Evento evento = eventoSnapshot.getValue(Evento.class);
                        if (evento != null && evento.getCobroOGasto().equalsIgnoreCase(tipoSeleccionado)) {

                            if (mesSeleccionado != -1) {
                                Calendar cal = Calendar.getInstance();
                                cal.setTimeInMillis(evento.getFechaMillis());
                                int mesEvento = cal.get(Calendar.MONTH); // ← 1 a 12

                                if (mesEvento == mesSeleccionado) {
                                    listaEventos.add(evento);
                                }
                            } else {
                                listaEventos.add(evento); // por si no se pasa el mes
                            }
                        }
                    }
                    recyclerView.setAdapter(new ListaEventosAdapter(listaEventos));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error cargando eventos: " + error.getMessage());
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista_eventos, container, false);

        recyclerView = view.findViewById(R.id.rvListaEventos);
        atras = view.findViewById(R.id.ibBackListaEventos);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listaEventos = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        cogerDatos();

        atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.mainPageFragmentContainer, new HomePage()) // Asegúrate de que R.id.fragment_container es el ID correcto de tu contenedor de fragments
                        .addToBackStack(null) // Opcional: permite volver atrás con el botón del sistema
                        .commit();
            }
        });

        return view;
    }

}