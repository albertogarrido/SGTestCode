package net.albertogarrido.studydrivetest

import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class Item(
        val id: String,
        val date: String,
        val color: Int
) {
    companion object {
        @JvmStatic
        fun create(): Item {
            val instant = Instant.now()
            val zoneId = ZoneId.of("Europe/Berlin")
            val localDateTime = LocalDateTime.ofInstant(instant, zoneId)
            return Item(
                    id = instant.epochSecond.toString(),
                    date = localDateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                    color = randomColor()
            )
        }
    }

}
