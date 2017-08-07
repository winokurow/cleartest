package de.clearit.test.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * Test Utilities.
 * 
 * @author Ilja Winokurow
 */
public final class TestUtils
{
   /** Gesamte Schlafzeit */
   private static final AtomicLong TOTAL_SLEEP = new AtomicLong();

   /** Logger */
   public static final Logger logger = Logger.getLogger("TestUtils");

   /** Tasklist Befehl */
   private static final String TASKLIST = "tasklist";

   /** Kill Befehl */
   private static final String KILL = "taskkill /F /IM ";

   /**
    * Constructor.
    * 
    * Utility classes should not have public constructors
    */
   private TestUtils()
   {
   }

   /**
    * Überprüfen ob Prozess läuft.
    * 
    * @param serviceName
    *           - der Prozessname.
    * 
    * @return ob Prozess läuft.
    * 
    * @throws IOException
    *            wenn beim lesen der Tasks etwas schief geht
    */
   public static boolean isProcessRunning(final String serviceName) throws IOException
   {
      boolean isRunning = false;
      final Process p = Runtime.getRuntime().exec(TASKLIST);
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));)
      {

         String line;
         while ((line = reader.readLine()) != null)
         {
            if (line.contains(serviceName))
            {
               isRunning = true;
               break;
            }
         }
      }
      catch (IOException e)
      {
         logger.error(e);
      }
      return isRunning;
   }

   /**
    * Überprüfen ob Prozess läuft (ohne Ausnahme).
    * 
    * @param serviceName
    *           - der Prozessname.
    *
    * @return ob Prozess läuft.
    */
   public static boolean isProcessRunningQuiet(final String serviceName)
   {
      try
      {
         return isProcessRunning(serviceName);
      }
      catch (Exception e)
      {
         logger.error(e);
         return false;
      }
   }

   /**
    * Den Prozess schließen.
    * 
    * @param serviceName
    *           - Der Prozess zu schließen.
    */
   public static void killProcess(final String serviceName)
   {
      String command = KILL + serviceName;
      try
      {
         Runtime.getRuntime().exec(command);
      }
      catch (IOException e)
      {
         logger.error("Fehler beim töten des mit Kommando " + command, e);
      }
   }

   /**
    * Warten bestimmte Zeit.
    * 
    * @param time
    *           - Die Schlafzeit (Millisekunden).
    */
   public static void sleep(final int time)
   {
      TOTAL_SLEEP.addAndGet(time);
      try
      {
         if (time > 0)
         {
            loggeWarteZeit(time);
            Thread.sleep(time);
         }
      }
      catch (final InterruptedException e)
      {
         logger.error(e);
      }
   }

   /**
    * Schlafzeit ausgeben.
    * 
    * @param time
    *           - Die Schlafzeit (Millisekunden).
    */
   private static void loggeWarteZeit(final int time)
   {
      String message = "Warte " + time + "ms";
      if (time > 50)
      {
         logger.info(message);
      }
      else
      {
         logger.debug(message);
      }
   }

   /**
    * Liefert generierte Zeichenfolge zurück
    * 
    * @param length
    *           - die Lange der Zeichenfolge
    * 
    * @return - die generierte Zeichenfolge
    */
   public static String generateString(int length)
   {
      return RandomStringUtils.randomAlphabetic(length);
   }

   /**
    * Liefert generierte Integer zurück
    * 
    * @param max
    *           - max Wert
    * 
    * @return - die generierte Zahl (als String)
    */
   public static String generateInteger(int max)
   {
      Random rand = new Random();
      return Integer.toString(rand.nextInt(max));
   }

   /**
    * Zeit des Schlafens ausgeben
    */
   public static void logTotalSleep()
   {
      long totalSleepMillis = TOTAL_SLEEP.get();
      long sekunden = totalSleepMillis / 1000;
      if (sekunden > 0)
      {
         logger.info(String.format("Insgesamt %s geschlafen ZzZZzzzzzz",
               DauerStringUtils.formatierteDauer(totalSleepMillis)));
      }
   }

   /**
    * Liefert den Name der Methode zurück
    * 
    * @return - der Name der Methode
    */
   public static String getMethodName()
   {
      return getStackMethod(2);
   }
   
   /**
    * getSubMethodName
    * <p>
    * 
    * Liefert den Name der SubMethode zurück
    * 
    * @return - der Name der SubMethode
    */
   public static String getSubMethodName()
   {
      return getStackMethod(2);
   }

   /**
    * Liefert einen Name der Methode aus StackTrace
    * 
    * @param position
    *           - die Position in Stack Trace
    * @return - der Name der SubMethode
    */
   private static String getStackMethod(int position)
   {
      return new Exception().getStackTrace()[position].getMethodName();
   }
}
