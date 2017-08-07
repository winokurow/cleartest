package de.clearit.test.framework.listener;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.testng.ITestResult;

import de.clearit.test.common.DateiUtils;
import de.clearit.test.framework.WebDriverHolder;
import de.clearit.test.framework.webdriver.WebDriverWrapper;

public class HtmlToFileOnFailure extends BaseProjectTestListenerAdapter
{

   private static final Logger logger = Logger.getLogger(HtmlToFileOnFailure.class.getName());

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
         takeHtmlFromWebdriver(tr);
      }
      catch (Exception e)
      {
         logger.error("Fehler beim erzeugen des HTML im Fehlerfall", e);
         Throwable testThrowable = tr.getThrowable();
         if (testThrowable != null)
         {
            logger.error("Fehler beim erzeugen des HTML, Test Exception war folgende", testThrowable);
         }
      }
   }

   private void takeHtmlFromWebdriver(final ITestResult tr)
   {
      final WebDriverWrapper driver = ((WebDriverHolder) tr.getInstance()).getWebDriver();
      if (driver != null)
      {
         takeHtml(tr, driver);
      }
   }

   private void takeHtml(final ITestResult tr, final WebDriverWrapper driver)
   {
      final String html = driver.getPageSource();
      final String destDir = findDestinationDir();
      final String destFileBase = ArtifactTestFiles.filenameBasis(tr);
      final String destFile = destFileBase + ".txt";
      try
      {
         FileUtils.writeByteArrayToFile(new File(destDir + "/" + destFile), html.getBytes());
      }
      catch (final IOException e)
      {
         logger.error(e.getMessage());
      }
      logger.info("HTML " + destFile + " erstellt in " + destDir + ".");
   }

   private void createDestinationDirIfNeeded()
   {
      final String destDir = findDestinationDir();
      try
      {
         DateiUtils.createFolder(destDir);
      }
      catch (IOException e)
      {
         logger.error("Ausnahme bei Ordner anlegen.", e);
      }
   }

   private String findDestinationDir()
   {
      return ArtifactTestFiles.findDestinationDir("html_on_error");
   }
}
