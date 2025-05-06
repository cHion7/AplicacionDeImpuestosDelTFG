package com.example.aplicaciondeimpuestosdeltfg.gestor;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciondeimpuestosdeltfg.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.MesViewHolder> {
    private List<String> listaMeses;
    private List<Evento> listaEventos;
    private List<Evento> eventosDelMes;
    private Map<String, Integer> MES_NUMEROS = new HashMap<String, Integer>() {{
        put("Enero", 0);
        put("Febrero", 1);
        put("Marzo", 2);
        put("Abril", 3);
        put("Mayo", 4);
        put("Junio", 5);
        put("Julio", 6);
        put("Agosto", 7);
        put("Septiembre", 8);
        put("Octubre", 9);
        put("Noviembre", 10);
        put("Diciembre", 11);
    }};

    private Map<String, List<Evento>> gastosPorCategoria;
    private Map<String, List<Evento>> cobrosPorCategoria;

    public ViewPager2Adapter(List<String> listaMeses, List<Evento> listaEventos) {
        this.listaMeses = listaMeses;
        this.listaEventos = listaEventos;
    }


    @NonNull
    @Override
    public MesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_meses, parent, false);
        return new MesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MesViewHolder holder, int position) {
        Log.d("DEBUG", "Eventos totales: " + listaEventos.size());
        String mesActual = listaMeses.get(position);
        holder.tituloMes.setText(listaMeses.get(position));

        int numeroMes = MES_NUMEROS.get(mesActual);

        eventosDelMes = new ArrayList<>();
        for (Evento evento : listaEventos) {
            Calendar fechaEvento = evento.getFecha(); // <- esto es un Calendar
            int mesEvento = fechaEvento.get(Calendar.MONTH); // 0-11
            if (mesEvento == numeroMes) {
                eventosDelMes.add(evento);
            }
        }

        gastosPorCategoria = new HashMap<>();
        cobrosPorCategoria = new HashMap<>();


        for (Evento evento : eventosDelMes) {
            if (evento.getTipo() == Evento.Tipo.GASTO) {
                if (!gastosPorCategoria.containsKey(evento.getCategoria())) {
                    gastosPorCategoria.put(evento.getCategoria(), new ArrayList<>());
                }
                gastosPorCategoria.get(evento.getCategoria()).add(evento);
            } else if (evento.getTipo() == Evento.Tipo.COBRO) {
                if (!cobrosPorCategoria.containsKey(evento.getCategoria())) {
                    cobrosPorCategoria.put(evento.getCategoria(), new ArrayList<>());
                }
                cobrosPorCategoria.get(evento.getCategoria()).add(evento);
            }
        }

        StringBuilder desgloseGastos = new StringBuilder();
        for (Map.Entry<String, List<Evento>> entry : gastosPorCategoria.entrySet()) {
            desgloseGastos.append(entry.getKey()).append(":\n"); // La categoría (Transporte, etc.)

            for (Evento evento : entry.getValue()) {
                desgloseGastos
                        .append(" - ")
                        .append(evento.getTitulo())
                        .append(": ")
                        .append(evento.getDinero())
                        .append("€\n");
            }
            desgloseGastos.append("\n"); // Salto entre categorías
        }

        holder.gastos.setText(desgloseGastos.toString());

        StringBuilder desgloseCobros = new StringBuilder();
        for (Map.Entry<String, List<Evento>> entry : cobrosPorCategoria.entrySet()) {
            desgloseCobros.append(entry.getKey()).append(":\n");

            for (Evento evento : entry.getValue()) {
                desgloseCobros
                        .append(" - ")
                        .append(evento.getTitulo())
                        .append(": ")
                        .append(evento.getDinero())
                        .append("€\n");
            }
            desgloseCobros.append("\n");
        }

        holder.ingresos.setText(desgloseCobros.toString());
    }
    @Override
    public int getItemCount() {
        return listaMeses.size();
    }

    class MesViewHolder extends RecyclerView.ViewHolder {
        TextView tituloMes;
        TextView tituloGastos;
        TextView tituloIngresos;
        CardView cvGastos;
        CardView cvIngresos;

        TextView gastos;
        TextView ingresos;


        public MesViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloMes = itemView.findViewById(R.id.tituloMesHomePage);
            cvGastos = itemView.findViewById(R.id.cvGastos);
            cvIngresos = itemView.findViewById(R.id.cvIngresos);
            tituloGastos = itemView.findViewById(R.id.tvTituloGastos);
            tituloIngresos = itemView.findViewById(R.id.tvTituloIngresos);
            gastos = itemView.findViewById(R.id.tvGastosViewPager2);
            ingresos = itemView.findViewById(R.id.tvCobrosViewPager2);
        }
    }
}
