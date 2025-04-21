package com.example.aplicaciondeimpuestosdeltfg.gestor;

import java.util.Calendar;

public class CalendarManager {
    public CalendarManager() {
    }
    public Calendar getCalendar(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal;
    }
}
