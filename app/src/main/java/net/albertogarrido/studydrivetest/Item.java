package net.albertogarrido.studydrivetest;

import org.threeten.bp.Instant;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

public final class Item {

    private String id;
    private String date;
    private int color;

    public Item() {
        Instant instant = Instant.now();
        ZoneId zoneId = ZoneId.of("Europe/Berlin");
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
        id = String.valueOf(instant.getEpochSecond());
        date = localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
        color = Utils.getRandomColor();
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getColor() {
        return color;
    }

}
