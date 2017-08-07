package de.clearit.test.common;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

/**
 * Datum Utilites.
 * 
 * @author Ilja Winokurow
 */
public class DatumUtils
{

   /* Logger */
   public static Logger logger = Logger.getLogger("DatumUtils");

   /**
    * Das aktuelle Datum rausfinden
    * 
    * @param format
    *           - Das Format des Datums
    * 
    * @return - das aktuelle Datum
    */
   public static String getCurrentDatum(String format)
   {
      ZonedDateTime zonedDateTime = ZonedDateTime.now();
      DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(format);
      return dateTimeFormatter.format(zonedDateTime);
   }

   /**
    * 
    * Das Datum parsen
    *
    * @param datum
    *           - das Datum zu parsen
    * @param format
    *           - das Format des Datums
    * 
    * @return - das aktuelle Datum
    */
   public static LocalDateTime datumParsen(String datum, String format)
   {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      LocalDateTime dateTime = LocalDateTime.parse(datum, formatter);
      return dateTime;
   }

   /**
    * getCurrentDatumPlusOffset
    * <p>
    * 
    * Das Datum in der Zukunft rausfinden, das eine bestimmte Anzahl von Tagen nach dem aktuellen Datum liegt
    * 
    * @param format
    *           - Das Format des Datums
    * @param days
    *           - der Offset an Tagen, der zum aktuellen Datum draufgezählt werden soll
    * 
    * @return - das gewünschte Datum als String
    */
   public static String getCurrentDatumPlusOffset(String format, int days)
   {
      ZonedDateTime zonedDateTime = ZonedDateTime.now();
      zonedDateTime = zonedDateTime.plusHours(days * 24);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      return zonedDateTime.format(formatter);
   }

   /**
    * Prüfen ob Datum nicht früher als bestimmte Datum (now() - inter) ist
    * 
    * @param dt
    *           - geprüfte Datum
    * @param inter
    *           - Interval (Sekunden)
    * 
    * @return - ob Datum nicht früher als bestimmte Datum ist
    */
   public static boolean isDatumEarlierAsAwaited(ZonedDateTime dateTime, int interval)
   {
      ZonedDateTime now = ZonedDateTime.now();
      Duration duration = Duration.between(dateTime, now);
      return duration.getSeconds() < interval;
   }

   /**
    * Datum als String rausgeben
    * 
    * @param date
    *           - das Datum zu benutzen
    * @param format
    *           - Das Format des Datums
    * 
    * @return - Datum in String Format
    */
   public static String getDatumString(ZonedDateTime date, String format)
   {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
      return formatter.format(date);
   }
}
