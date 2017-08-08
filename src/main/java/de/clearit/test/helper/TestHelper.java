package de.clearit.test.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Listeners;

import com.sun.jna.platform.win32.Netapi32Util.User;

import de.clearit.test.common.BasisLogger;
import de.clearit.test.common.ScreenshotCreator;
import de.clearit.test.common.TestUtils;
import de.clearit.test.data.Browser;
import de.clearit.test.exceptions.AllgemeineTechnischeException;
import de.clearit.test.framework.AllTestListenerAdapters;
import de.clearit.test.framework.ExecutionTimer;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.framework.PropertyManager;
import de.clearit.test.framework.WebDriverCleanUp;
import de.clearit.test.framework.WebDriverHolder;
import de.clearit.test.framework.webdriver.WebDriverManager;
import de.clearit.test.framework.webdriver.WebDriverWrapper;


/**
 * <P>
 * Das abstrakte Elternklass für alle Tests.
 * 
 *
 * @implements WebDriverHolder - WebDriver Holder
 * 
 * @author Ilja Winokurow
 */
@Listeners(AllTestListenerAdapters.class)
public class TestHelper implements WebDriverHolder, WebDriverCleanUp
{

   /* Logger */
   protected final static Logger logger = BasisLogger.LOGGER;

   /** Execution Timer. */
   private ExecutionTimer executionTimer = null;

   /** Webdriver Instance. */
   protected WebDriverWrapper driver;

   /** Alle im test erzeugten Webdriver */
   protected List<WebDriverWrapper> driversBeingUsedInTest = null;

   /** Die Eigenschaften des Tests. */
   protected PropertyManager properties;

   /** Angemeldete User. */
   protected User user = null;

   /** Test gestartet */
   protected Boolean testStartet = null;

   /** Testschritt */
   protected Integer step;

   /** Einstellungen fuer die Performancevermessung der durchgefuehrten Tests mit Dynatrace **/
   private DynatraceMessung dynatraceMessung;

   /**
    * Webdriver anhalten
    */
   protected void stopWebDriver()
   {
      stopWebDriver(driver);
   }

   protected void stopWebDriver(WebDriverWrapper driver)
   {
      WebDriverManager.stopDriver(driver);
      driversBeingUsedInTest.remove(driver);
      this.driver = null;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public WebDriverWrapper getWebDriver()
   {
      return driver;
   }

   public void nextStep(String text)
   {
      Reporter.log(text);
      logStep(text);
   }


   private void logStep(final String text)
   {
      logger.info("Step " + (step++) + ": " + text);
   }


   /**
    * Zentrale Erzeugung eines neuen Drivers, die Driver instanz darf nur hier neu zugewiesen werden!
    *
    * @param isIE
    *           - ob IE
    * @param url
    *           - URL der Anwendung
    */
   protected void erzeugeNeuenDriver(String url, Browser browser)
   {

      // initialize Execution Timer
      if (ExecutionTimerManager.getExecutionTimer() == null)
      {
         executionTimer = new ExecutionTimer();
         executionTimer.init(TestUtils.getMethodName());
         ExecutionTimerManager.setExecutionTimer(executionTimer);
      }

      String grid = getProperties().getProperty("seleniumgrid.url");
      String gridHint = "Service Grid";

      driver = WebDriverManager.createDriver(grid, gridHint, browser, url);
      driversBeingUsedInTest.add(driver);
      aktiviereDynatraceMessung(driver);
   }

   public String getProperty(String key)
   {
      return getProperties().getProperty(key);
   }

   private PropertyManager getProperties()
   {
      if (properties == null)
      {
         throw new AllgemeineTechnischeException("Properties sind null. Start Test nicht aufgerufen?");
      }
      return properties;
   }

   /**
    * @param dateiname
    *           ohne Endung
    */
   protected void screenshotErzeugen(String dateiname)
   {
      new ScreenshotCreator().takeScreenshot(getWebDriver(), dateiname);
   }

  

   /**
    * Initialisieren Dynatrace Messung
    *
    * @param clazz
    *           - die Testklasse
    * @param method
    *           - die Testmethode
    */
   protected void initDynatraceMessung(Class clazz, Method method)
   {
      dynatraceMessung = new DynatraceMessung(clazz.getName(), method.getName());
   }

   protected void aktiviereDynatraceMessung(WebDriverWrapper driver)
   {
      if (dynatraceMessung != null)
      {
         dynatraceMessung.aktivieren(driver);
      }
   }

   protected void beendeDynatraceMessung()
   {
      if (dynatraceMessung != null)
      {
         dynatraceMessung.beenden();
      }
   }

@Override
public void stopWebDrivers() {
    if (driversBeingUsedInTest == null)
    {
       return;
    }
    List<WebDriverWrapper> driversToClose = new ArrayList<>(driversBeingUsedInTest);
    for (WebDriverWrapper webDriverWrapper : driversToClose)
    {
       stopWebDriver(webDriverWrapper);
    }
    driversBeingUsedInTest.clear();
	
}

}
