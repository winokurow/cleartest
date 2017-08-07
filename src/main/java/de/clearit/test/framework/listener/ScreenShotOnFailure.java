package de.clearit.test.framework.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.testng.ITestResult;
import org.testng.Reporter;

import de.clearit.test.common.DateiUtils;
import de.clearit.test.framework.WebDriverHolder;
import de.clearit.test.framework.webdriver.WebDriverWrapper;

public class ScreenShotOnFailure extends BaseProjectTestListenerAdapter
{

   private static final Logger logger = Logger.getLogger(ScreenShotOnFailure.class.getName());

   @Override
   public void onTestStart(ITestResult tr)
   {
      createDestinationDirIfNeeded();
   }

   @Override
   public void onTestSkipped(ITestResult tr)
   {
      createDestinationDirIfNeeded();
   }

   @Override
   public void onTestFailure(final ITestResult tr)
   {
      createDestinationDirIfNeeded();
      try
      {
         takeScreenshotFromWebdriver(tr);
      }
      catch (Exception e)
      {
         logger.error("Fehler beim erzeugen des Screenshots im Fehlerfall", e);
         Throwable testThrowable = tr.getThrowable();
         if (testThrowable != null)
         {
            logger.error("Fehler beim erzeugen des Screenshots, Test Exception war folgende", testThrowable);
         }
      }
   }

   private void takeScreenshotFromWebdriver(final ITestResult tr)
   {
      final WebDriverWrapper driver = ((WebDriverHolder) tr.getInstance()).getWebDriver();
      if (driver != null)
      {
         takeScreenshot(tr, driver);
      }
      else
      {
         logger.info("Cannot take Screenshot");
      }
   }

   private void takeScreenshot(final ITestResult tr, final WebDriverWrapper driver)
   {
      final File scrFile = driver.getScreenshotAs(OutputType.FILE);
      final String destDir = findDestitnationDir();
      final String destFile = ArtifactTestFiles.filenameBasis(tr) + ".png";

      try
      {
         FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
      }
      catch (final IOException e)
      {
         logger.error(e.getMessage());
      }
      // Reporter.setEscapeHtml(false);
      logger.info("Screenshot " + destFile + " erstellt in " + destDir + ".");
      String log = "Saved <a target='_blank' href=../../surefire-reports/artifact-test-files/screenshots/" + destFile
            + ">Screenshot</a>";
      Reporter.log(log);
   }

   private void createDestinationDirIfNeeded()
   {
      final String destDir = findDestitnationDir();
      try
      {
         DateiUtils.createFolder(destDir);
      }
      catch (IOException e)
      {
         logger.error("Ausnahme bei Ordner anlegen.", e);
      }
   }

   private String findDestitnationDir()
   {
      return ArtifactTestFiles.findDestinationDir("screenshots");
   }

}
