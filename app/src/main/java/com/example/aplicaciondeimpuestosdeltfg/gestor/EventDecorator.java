package com.example.aplicaciondeimpuestosdeltfg.gestor;

import android.util.Log;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<CalendarDay> dates;

    public EventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        boolean result = dates.contains(day);
        if (result) {
            Log.d("DECORADOR", "Decorando día: " + day);
        }
        return result;
    }
    /*
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }
    */
    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(10, color));
    }
}

