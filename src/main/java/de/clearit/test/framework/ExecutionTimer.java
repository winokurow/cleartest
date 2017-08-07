package de.clearit.test.framework;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import de.clearit.test.common.DateiUtils;

/**
 * Messt Zeit, die für Maskenwechseln notendig ist
 */
public class ExecutionTimer
{

   /* Logger */
   static Logger logger = Logger.getLogger("ExecutionTimer");

   /* Zeit des Anfanges */
   private long startTime = 0;

   /* Zeit des Endes */
   private long endTime = 0;

   /* Die Seite des Anfanges */
   private String startPage = "";

   /* Aktion */
   private String aktion = "";

   /* Die Seite des Ende */
   private String endPage = "";

   /* Ergebnisse Dateiname */
   private Path file;

   /* Ob Zeitmessung wird ausgeführt */
   private boolean isProfilerOn;

   /**
    * Die Werte initializieren
    * 
    * @param text
    *           - Profiler Datei Prefix (Meistens der Name des Testfalls)
    */
   public void init(String text)
   {
      String fileName = text + "_Profiler.csv";
      isProfilerOn = Boolean.valueOf(System.getProperty("profiler", "false"));

      if (isProfilerOn)
      {
         DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
         Date date = new Date();
         final String umgebung = System.getProperty("umgebung", "local");
         Path folder = Paths.get("target", "Profiler", dateFormat.format(date) + "_" + umgebung);
         logger.info("Create folder " + folder.toString());

         try
         {
            Files.createDirectories(folder);
            DateiUtils.cleanDirectory(folder.toFile());
            file = folder.resolve(fileName);
            DateiUtils.writeLine(file.toString(), "Datum;Start Seite;Aktion;End Seite;Ladedauer (ms)");
         }
         catch (IOException e1)
         {
            logger.error("Exception in Profiler: " + e1);
         }

      }
   }

   /**
    * Timer starten
    * 
    * @param text
    *           - Der Name der Anfangsmaske
    */
   public void start(String text)
   {
      if ((isProfilerOn))
      {
         startTime = System.currentTimeMillis();
         startPage = endPage;
         this.aktion = text;
      }
   }

   /**
    * Timer stoppen
    * 
    * @param text
    *           - Der Name der Endmaske
    */
   public void end(String text)
   {
      long duration = 0;
      if ((isProfilerOn) && (!(aktion.isEmpty())))
      {
         endTime = System.currentTimeMillis();
         endPage = text;
         duration = endTime - startTime;
         DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
         Date date = new Date();

         try
         {
            DateiUtils.writeLine(file.toString(),
                  dateFormat.format(date) + ";" + startPage + ";" + aktion + ";" + endPage + ";" + duration);
         }
         catch (IOException e)
         {
            logger.error("Schreiben in Datei throw Exception: " + e.getMessage());
         }
         aktion = "";
      }
   }

   /**
    * Sprung dauer
    * 
    * @return die Dauer
    */
   public long duration()
   {
      return endTime - startTime;
   }

}
