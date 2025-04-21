package com.example.aplicaciondeimpuestosdeltfg.gestor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.aplicaciondeimpuestosdeltfg.R;

import java.util.List;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.MesViewHolder> {
    private List<String> listaMeses;

    public ViewPager2Adapter(List<String> listaMeses) {
        this.listaMeses = listaMeses;
    }


    @NonNull
    @Override
    public MesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_meses, parent, false);
        return new MesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MesViewHolder holder, int position) {
        holder.tituloMes.setText(listaMeses.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMeses.size();
    }

    class MesViewHolder extends RecyclerView.ViewHolder {
        TextView tituloMes;

        public MesViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloMes = itemView.findViewById(R.id.tituloMesHomePage);
        }
    }
}
