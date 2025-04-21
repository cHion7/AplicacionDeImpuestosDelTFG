package com.example.aplicaciondeimpuestosdeltfg.gestor;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.HashSet;

public class MultipleEventsDecorator implements DayViewDecorator {

    private final HashSet<CalendarDay> multipleEventsDays;
    private final int[] colores; // Los colores que quieres mostrar (ej: gasto y cobro)

    public MultipleEventsDecorator(HashSet<CalendarDay> multipleEventsDays, int[] colores) {
        this.multipleEventsDays = multipleEventsDays;
        this.colores = colores;
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return multipleEventsDays.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new MultiDotSpan(colores));
    }
}
