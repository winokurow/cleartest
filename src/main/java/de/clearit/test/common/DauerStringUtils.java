package de.clearit.test.common;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Die Test Dauer.
 */
public class DauerStringUtils
{

   /**
    * Gibt z.B. 3min, 20sec oder 120ms zurück, abhängig von der Größe
    * 
    * @param ms
    *           Millisekunden
    * @return Dauer als formatierten String, abhängig von der Größe
    */
   public static String formatierteDauer(double ms)
   {
      String result = "";
      if (ms > (60000))
      {
         result = divide(ms, 60000) + "min";
      }
      else if (ms > 1000)
      {
         result = divide(ms, 1000) + "sec";
      }
      else
      {
         result = BigDecimal.valueOf(ms).intValue() + "ms";
      }
      return result;
   }

   /**
    * Die Devision
    * 
    * @param ms
    *           - der Dividend (double)
    * @param divisor
    *           - der Teiler (int)
    * 
    * @return Quotient
    */
   private static int divide(double zahl, int divisor)
   {
      return BigDecimal.valueOf(zahl).divide(BigDecimal.valueOf(divisor), RoundingMode.HALF_UP)
            .setScale(0, RoundingMode.HALF_UP).intValue();
   }
}
