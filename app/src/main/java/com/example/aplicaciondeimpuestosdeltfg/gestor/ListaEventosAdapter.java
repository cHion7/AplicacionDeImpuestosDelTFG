package com.example.aplicaciondeimpuestosdeltfg.gestor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciondeimpuestosdeltfg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ListaEventosAdapter extends RecyclerView.Adapter<ListaEventosAdapter.EventoViewHolder> {

    private List<Evento> listaEventos;
    private FirebaseDatabase db;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    public ListaEventosAdapter(List<Evento> listaEventos) {
        this.listaEventos = listaEventos;
    }

    @NonNull
    @Override
    public EventoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.vistalistaeventos, parent, false);
        return new EventoViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull EventoViewHolder holder, int position) {
        Evento evento = listaEventos.get(position);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        holder.titulo.setText(evento.getTitulo());
        holder.descripcion.setText(evento.getDescripcion());
        holder.cantidad.setText(evento.getDinero() + " €");

        // Formatear fecha si es tipo Calendar
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.fecha.setText(formato.format(evento.getFechaMillis()));

        holder.eliminar.setOnClickListener(v -> {
            if (evento.getKey() != null) {
                db = FirebaseDatabase.getInstance("https://base-de-datos-del-tfg-1-default-rtdb.europe-west1.firebasedatabase.app/");
                DatabaseReference usuariosReferencia = db.getReference().child("Usuarios");
                String emailUser = user.getEmail();
                DatabaseReference eventosRef = usuariosReferencia.child(emailUser.replace("@", "_").replace(".", "_")).child("eventos");
                eventosRef.child(evento.getKey()).removeValue();

                // También quita el evento localmente y refresca la lista
                listaEventos.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, listaEventos.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public static class EventoViewHolder extends RecyclerView.ViewHolder {
        TextView titulo, descripcion, cantidad, fecha, eliminar;

        public EventoViewHolder(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tvTituloListaEventos);
            descripcion = itemView.findViewById(R.id.tvDecripcionEventoListaEventos);
            cantidad = itemView.findViewById(R.id.tvCantidadEventoListaEventos);
            fecha = itemView.findViewById(R.id.tvFechaEventoListaEventos);
            eliminar = itemView.findViewById(R.id.tvEliminarListaEventos);

        }
    }
}
