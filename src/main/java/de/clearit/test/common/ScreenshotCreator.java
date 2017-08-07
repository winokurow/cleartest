package de.clearit.test.common;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.testng.Reporter;

import de.clearit.test.framework.listener.ArtifactTestFiles;
import de.clearit.test.framework.webdriver.WebDriverWrapper;

/**
 * Methoden um ein Screenshot abzuspeichern
 * 
 * @author Ilja Winokurow
 */
public class ScreenshotCreator
{

   private static final Logger logger = Logger.getLogger(ScreenshotCreator.class.getName());

   /**
    * Erzeugt einen Screenshot im dafür vorgesehenen target Ordner.
    * 
    * @param driver
    *           der Webdriver
    * @param filenameBasis
    *           Der Dateiname ohne Endung für den Screenshot.
    */
   public void takeScreenshot(final WebDriverWrapper driver, final String filenameBasis)
   {
      final String destDir = findDestitnationDir();

      final String destFile = filenameBasis + ".png";
      final String destignation = String.format("%s/%s", destDir, destFile);
      final File destAbsoluteFile = new File(destignation);
      try
      {
         DateiUtils.createFolder(destDir);
         File screenshot = driver.getScreenshotAs(OutputType.FILE);
         FileUtils.copyFile(screenshot, destAbsoluteFile);
      }
      catch (final IOException e)
      {
         logger.error(e);
      }

      // Eine Zeile in Reporter Log hinzufügen
      logger.info("Screenshot " + destFile + " erstellt in " + destDir + ".");
      String log = "Saved <a target='_blank' href=../../surefire-reports/artifact-test-files/screenshots/" + destFile
            + ">Screenshot</a>";
      Reporter.log(log);
   }

   /**
    * Der Ordner für Screenshots finden.
    * 
    * @return Der Ordner für Screenshots
    */
   private String findDestitnationDir()
   {
      return ArtifactTestFiles.findDestinationDir("screenshots");
   }

}
