package de.clearit.test.framework;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * PropertyManager.
 * 
 * <P>
 * Property manager - notwendig um alle test Eigenschaften zu steuern.
 * 
 * @author Ilja Winokurow
 */
public class PropertyManager
{

   /* Logger */
   private static final Logger logger = Logger.getLogger("PropertyManger");

   /** Property manager instance. */
   private static PropertyManager instance = null;

   /** Lock Objekt. Notwendig für sync Ausführung. */
   private static final Object lock = new Object();

   /** Test Eigenschaften. */
   private final Properties defaultProps = new Properties();

   /**
    * Constructor.
    * 
    * Wird nie ausgeführt. Um PropertyManager zu initialisieren rufen Method getInstance an.
    */
   private PropertyManager()
   {
   }

   /**
    * getInstance.
    * 
    * PropertyManager Objekt initialisieren.
    * 
    * @return PropertyManager Objekt.
    */
   public static PropertyManager getInstance()
   {
      // if (instance == null)
      // {
      synchronized (lock)
      {
         if (instance == null)
         {
            instance = new PropertyManager();
            instance.loadProperties();
         }
      }
      // }
      return instance;
   }

   /**
    * loadProperties.
    * 
    * Eigenschaften laden.
    * 
    */
   private void loadProperties()
   {
      // create and load default properties
      loadPropertyFile("/defaultprop.properties");
      loadPropertyFile("/localprop.properties");
      final String umgebung = System.getProperty("umgebung", "local");

      logger.info("Umgebung: " + umgebung);
      loadPropertyFile("/umgebungen" + File.separator + umgebung + ".properties");

      final String dataset = System.getProperty("dataset", "dataset1");

      String folderString2 = "/testdata/default";
      File folder2 = new File(getClass().getResource(folderString2).getFile());

      String propertiesExtension = "properties";
      File[] files2 = folder2.listFiles();
      if (files2 != null)
      {
         for (final File fileEntry : files2)
         {

            if (fileEntry.getName().contains(propertiesExtension))
            {
               loadPropertyFile(fileEntry);
            }

         }
      }

      String folderString1 = "/testdata/" + dataset;
      File folder1 = new File(getClass().getResource(folderString1).getFile());
      if (folder1 != null)
      {
         File[] files1 = folder1.listFiles();
         if (files1 != null)
         {
            for (final File fileEntry : files1)
            {
               if (fileEntry.getName().contains(propertiesExtension))
               {
                  loadPropertyFile(fileEntry);
               }
            }
         }
      }
   }

   /**
    * loadPropertyFile.
    * 
    * Eigenschaften Datei laden.
    * 
    * @param fileName
    *           - der Name der Datei
    * 
    */
   private void loadPropertyFile(final String fileName)
   {
      final InputStream in = getClass().getResourceAsStream(fileName);
      if (in == null)
      {
         throw new RuntimeException(fileName + " not found");
      }
      try
      {
         defaultProps.load(in);
         in.close();
      }
      catch (final IOException e)
      {
         logger.error("IOException passiert " + e.getMessage());
      }
   }

   /**
    * loadPropertyFile.
    * 
    * Eigenschaften Datei laden.
    * 
    * @param fileName
    *           - der Name der Datei
    * 
    * @throws FileNotFoundException
    * 
    */
   private void loadPropertyFile(final File file)
   {

      try
      {
         final InputStream in = new FileInputStream(file);
         defaultProps.load(in);
         in.close();
      }
      catch (final IOException e)
      {
         logger.error("IOException passiert " + e.getMessage());
      }
   }

   /**
    * getProperty.
    * 
    * Get Wert für bestimmte Eigenschaft.
    * 
    * @param key
    *           - Eigenschaft Name.
    * @param defaultString
    *           - was zurückgegeben werden soll, wenn der key nicht gefunden wurde
    */
   public String getProperty(final String key, final String defaultString)
   {
      String val = null;
      if (key != null)
      {
         val = defaultProps.getProperty(key, defaultString);
      }
      return (val);
   }

   /**
    * getProperty.
    * 
    * Get Wert für bestimmte Eigenschaft.
    * 
    * @param key
    *           - Eigenschaft Name.
    */
   public String getProperty(final String key)
   {
      return getProperty(key, null);
   }

}
