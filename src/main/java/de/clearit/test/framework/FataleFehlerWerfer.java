package de.clearit.test.framework;

import org.testng.Assert;

/**
 * FataleFehlerWerfer
 */
public final class FataleFehlerWerfer
{

   /**
    * private constructor to hide the implicit public one
    */
   private FataleFehlerWerfer()
   {

   }

   /**
    * Es werden hier fatale Fehler in der Anwendung erkannt und lassen den Test failen, wenn sie auftreten
    * 
    * @param errorTextAufMaske
    */
   public static void pruefeFehlerTextUndFailWennFatal(String errorTextAufMaske)
   {
      if (errorTextAufMaske.contains("Es ist ein technischer Fehler aufgetreten"))
      {
         Assert.fail("Die Anwendung hat einen technischen Fehler erlitten: " + errorTextAufMaske);
      }

      if (errorTextAufMaske.contains("Es ist ein Service-Fehler aufgetreten"))
      {
         Assert.fail("Die Anwendung erlitt einen Service-Fehler: " + errorTextAufMaske);
      }

      if (errorTextAufMaske.contains("Es ist ein unbekannter Fehler aufgetreten"))
      {
         Assert.fail("Die Anwendung erlitt einen unbekannten Fehler: " + errorTextAufMaske);
      }
   }

}
