



package me.earth.phobos.features.gui.alts.ias.legacysupport;

import java.time.format.*;
import java.time.*;

public class NewJava implements ILegacyCompat
{
    public int[] getDate() {
        final int[] ret = { LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth(), LocalDateTime.now().getYear() };
        return ret;
    }
    
    public String getFormattedDate() {
        final DateTimeFormatter format = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
        final LocalDate date = LocalDateTime.now().withDayOfMonth(this.getDate()[1]).withMonth(this.getDate()[0]).withYear(this.getDate()[2]).toLocalDate();
        return date.format(format);
    }
}
