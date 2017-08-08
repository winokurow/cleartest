package de.clearit.test.common;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.base.Predicate;

import de.clearit.test.framework.PropertyManager;

/**
 * 
 * SeitenladePruefer
 */
public class SeitenladePruefer
{
   // Wie lange ist BlockUI weg gedauert.
   private static final AtomicLong GESAMTZEIT_MILLIS_BLOCKUI = new AtomicLong();

   // Ab wann wird die Warnung asgegeben.
   private static final int LADE_WARNUNG_AB_MILLIS = 3000;

   // Logger.
   private static final Logger LOGGER = Logger.getLogger(SeitenladePruefer.class);

   // ajaxStatusPanelSign.
   By ajaxStatusPanelSign = By.id("zapAnwendungFormId:ajaxStatusPanel_start");

   // BlockUI.
   By blockUI = By.cssSelector("div[class*='blockUI blockOverlay']");

   // Seiteladen Timeout.
   private long timeoutInSeconds;

   // WebDriver.
   final WebDriver driver;
   
   /* Logger */
   protected Logger logger = Logger.getLogger(getClass());
   /**
    * Überprüft ob die Seite gerade geladen wird, also Ajax indicator oder BlockUi und wartet bis das fertig ist
    * 
    * @param driver
    */
   public static SeitenladePruefer mitDriver(final WebDriver driver)
   {
      return new SeitenladePruefer(driver);
   }

   /**
    * Constructor.
    * 
    * @param driver
    *           - WebDriver
    */
   SeitenladePruefer(final WebDriver driver)
   {
      this.driver = driver;
      timeoutInSeconds = Long.parseLong(PropertyManager.getInstance().getProperty("seite.laden.timeout"));
   }

   /**
    * waitForPageIsReady
    * 
    * Warten bis die Seite geladen wird
    * 
    */
   public void waitForPageIsLoad()
   {
      waitForPageIsLoad(timeoutInSeconds);
   }

   /**
    * waitForPageIsLoad
    * 
    * Warten bis die Seite geladen wird <br>
    * (warten bis der Status der Seite=ready, Block UI ist weg, Ajax Indicator ist weg)
    * 
    * @param timeoutInSeconds
    *           - das Timeout
    */
   public void waitForPageIsLoad(long timeoutInSeconds)
   {
      checkDriver();
      long startTimeMillis = System.currentTimeMillis();
      try
      {
         //waitForPageIsReady();
         waitForAjaxIndicatorInvisibility(timeoutInSeconds); // ajax indicator zuerst prüfen, da er früher erscheint
         //waitForBlockUiReady();
         waitForAjaxIndicatorInvisibility(timeoutInSeconds); // ajax indicator nochmal prüfen, falls der erste
                                                             // "überrannt" wurde
      }
      finally
      {
         logTimeIfTookLongTime(startTimeMillis);
      }
   }

   /**
    * Warnung ausfeben wenn die Seiteladen sehr lang gedauert wird
    * 
    * @param startTimeMillis
    *           - die Zeit der Laden
    */
   private void logTimeIfTookLongTime(long startTimeMillis)
   {
      long tookMillies = System.currentTimeMillis() - startTimeMillis;
      GESAMTZEIT_MILLIS_BLOCKUI.addAndGet(tookMillies);
      if (tookMillies > LADE_WARNUNG_AB_MILLIS)
      {
         long tookSeconds = tookMillies / 1000;
         LOGGER.warn(String.format("Ladezeit %s Sekunden (BlockUI).", tookSeconds));
      }
   }

   /**
    * Gesamtzeit in Millis ausgeben
    * 
    * @return Gesamtzeit in Millis
    */
   public static long loggeGesamtzeitBlockUI()
   {
      long dauerInMillis = GESAMTZEIT_MILLIS_BLOCKUI.get();
      LOGGER.warn(String.format("Ladezeit %s (BlockUI).", DauerStringUtils.formatierteDauer(dauerInMillis)));
      return dauerInMillis;
   }

   /**
    * waitForPageIsReady
    * 
    * Warten bis die Seite den Status "Ready" bekommt
    * 
    */
   public void waitForPageIsReady()
   {
      final ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(final WebDriver driver)
         {
        	 String result = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
        	 logger.info(result);
            return result.equals("complete");
         }
      };
      
      final Wait<WebDriver> wait = new WebDriverWait(driver, timeoutInSeconds);
      wait.until(expectation);
   }

   /**
    * Warten bis AjaxIndicator verschwunden (in Statusleiste).
    * 
    * @param timeoutInSeconds
    *           - wie lange wird es gewartet bis Indicator weg (in Sekunden)
    */
   private void waitForAjaxIndicatorInvisibility(long timeoutInSeconds)
   {
      TestUtils.sleep(20); // dem AjaxIndicator eine Chance geben zu erscheinen. Sonst läuft der Check u.U. ins leere.
      try
      {
         if (driver.findElements(ajaxStatusPanelSign).size() > 0)
         {
            WebDriverWait waitAjax = new WebDriverWait(driver, timeoutInSeconds);
            waitAjax.until(ExpectedConditions.invisibilityOfElementLocated(ajaxStatusPanelSign));
         }

      }
      catch (WebDriverException e)
      {
         LOGGER.error(e);
      }
   }

   // TODO Is not compatible with Selenium 3.0
//   /**
//    * Warten bis BlockUI weg ist.
//    */
//   private void waitForBlockUiReady()
//   {
//      WebDriverWait wait = new WebDriverWait(driver, timeoutInSeconds);
//      if (driver.findElements(blockUI).size() > 0)
//      {
//
//         wait.until(new Predicate<WebDriver>()
//         {
//            @Override
//            public boolean apply(WebDriver driver)
//            {
//               return driver.findElements(blockUI).isEmpty();
//            }
//         });
//      }
//   }

   /**
    * Überprüfen ob WebDriver gesetzt ist
    */
   private void checkDriver()
   {
      if (driver == null)
      {
         throw new RuntimeException("Kein Webdriver gefunden. Bitte setzen.");
      }
   }
}
