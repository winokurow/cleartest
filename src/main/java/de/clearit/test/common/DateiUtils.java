package de.clearit.test.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

/**
 * FileUtils
 * 
 * Funktionen für die Arbeit mit Dateien.
 * 
 * @author Ilja Winokurow
 */
public class DateiUtils
{

   /* Logger */
   static Logger logger = Logger.getLogger("FileUtils");

   /**
    * Zeile in Datei schreiben.
    * 
    * @param fileName
    *           - der Dateiname.
    * @param text
    *           - die Zeile zu schreiben.
    * @throws IOException
    *            - die Ausnahme während Dateischreiben
    */
   public static void writeLine(final String fileName, final String text) throws IOException
   {
      try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(fileName, true), "ISO-8859-1")))
      {
         writer.write(text);
         writer.newLine();
      }
   }

   /**
    * Empty and delete a folder (and subfolders).
    * 
    * @param folder
    *           folder to empty
    * 
    * @throws IOException
    *            - die Ausnahme bei Löschen
    */
   public static void rmdir(final File folder) throws IOException
   {
      FileUtils.deleteDirectory(folder);
   }

   /**
    * Empty a folder (without folder delete).
    * 
    * @param folder
    *           folder to clean
    * 
    * @throws IOException
    *            - die Ausnahme bei Löschen
    */
   public static void cleanDirectory(final File folder) throws IOException
   {
      FileUtils.cleanDirectory(folder);
   }

   /**
    * Einen Ordner from Resources kopieren.
    * 
    * @param srcFolder
    *           - Ordner Pfad
    * @param destFolder
    *           - Der Pfad wohin wird kopiert
    * 
    * @throws IOException
    *            - die Ausnahme bei Kopieren
    */
   public static void copyResourceFolder(String srcFolder, String destFolder) throws IOException
   {
      File file = new File(DateiUtils.class.getResource(srcFolder).getFile());
      File dest = new File(destFolder);
      copyFolder(file, dest);
   }

   /**
    * Einen Ordner kopieren.
    * 
    * @param src
    *           - Ordner Pfad
    * @param dest
    *           - Der Pfad wohin wird kopiert
    * 
    * @throws IOException
    *            - die Ausnahme bei Kopieren
    */
   public static void copyFolder(File src, File dest) throws IOException
   {

      FileUtils.copyDirectory(src, dest);
   }

   /**
    * Einen Ordner erstellen.
    * 
    * @param pathString
    *           - Ordner Pfad
    * 
    * @throws IOException
    *            - die Ausnahme bei Ornder Anlegen
    */
   public static void createFolder(String pathString) throws IOException
   {
      Path path = Paths.get(pathString);
      if (!Files.exists(path))
      {
         Files.createDirectories(path);
      }
   }
}
