package com.example.aplicaciondeimpuestosdeltfg.gestor;

import java.util.Calendar;

public class CalendarManager {
    public CalendarManager() {
    }
    public Calendar getCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day); // aquí restas 1 para corregir
        // Opcional: poner hora, minutos y segundos a 0 para evitar problemas
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal;
    }
}
