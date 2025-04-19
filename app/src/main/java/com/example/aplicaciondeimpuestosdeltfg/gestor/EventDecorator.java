package com.example.aplicaciondeimpuestosdeltfg.gestor;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;
    private final HashSet<CalendarDay> multipleEventsDays;

    // Constructor que recibe los días de eventos y los días con múltiples eventos
    public EventDecorator(int color, HashSet<CalendarDay> dates, HashSet<CalendarDay> multipleEventsDays) {
        this.color = color;
        this.dates = dates;
        this.multipleEventsDays = multipleEventsDays;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // Decorar solo los días que están en la lista de eventos
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Usar `view.getDate()` para obtener el día actual a decorar
        CalendarDay currentDay = view.getDate();  // Obtiene la fecha que se está decorando

        // Verifica si el día tiene múltiples eventos (tanto GASTO como COBRO)
        if (multipleEventsDays.contains(currentDay)) {
            // Si el día tiene múltiples eventos, coloca los puntos desplazados
            view.addSpan(new DotSpan(10, color, -3));  // Punto a la izquierda
            view.addSpan(new DotSpan(10, color, 3));   // Punto a la derecha
        } else {
            // Si solo tiene un evento, coloca el punto en el centro
            view.addSpan(new DotSpan(10, color));
        }
    }
}
