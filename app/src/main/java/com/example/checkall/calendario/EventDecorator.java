package com.example.checkall.calendario;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.HashSet;
import java.util.Set;
import org.threeten.bp.LocalDate;

public class EventDecorator implements DayViewDecorator {

    private final int color;
    private final HashSet<LocalDate> dates;

    public EventDecorator(int color, Set<LocalDate> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day.getDate());
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(7, color)); // Tamaño y color del punto
    }
}

