package de.clearit.test.framework.webdriver;

import java.net.URL;

import org.apache.commons.lang3.Validate;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.clearit.test.framework.PropertyManager;

/**
 * Webdriver Instance steuern.
 */
public final class WebDriverManager
{

   private static String DEFAULT_VALUE = "false";

   /** Logger */
   private static final Logger logger = Logger.getLogger("WebDriverManager");

   /**
    * Constructor.
    * 
    * Private constructor to hide the implicit public one
    */
   private WebDriverManager()
   {
   }

   /**
    * Start WebDriver.
    * 
    * @param grid
    *           - Selenium Grid URL
    * 
    * @param gridHint
    *           - Selenium Grid Bezeichnung
    * 
    * @param isIE
    *           - ob Internet Explorer
    * @param url
    *           - URL zu laden.
    * @return den gestarteten WebDriver
    * 
    */
   public static WebDriverWrapper createDriver(String grid, String gridHint, final boolean isIE, String url)
   {
      WebDriverWrapper driver = erzeugeWebDriverMitRetry(isIE, url, grid);
      logger.info("Browser (" + (useNoProxy() ? "no Proxy" : "via Proxy") + ") "
            + (isLocal() ? "Lokaler" : "Remote/" + gridHint) + " Rechner: '" + driver.getIpOfNode() + "'");
      return driver;
   }

   /**
    * Stoppt den Webdriver und schliesst den Browser
    * 
    * @param webDriver
    */
   public static void stopDriver(WebDriver webDriver)
   {
      try
      {
         stopDriverOrFail(webDriver);
      }
      catch (Exception e)
      {
         logger.error("Fehler beim stoppen des Webdrivers: " + e.getMessage(), e);
      }
   }

   /**
    * Rausfinden ob Browser geschlossen war
    * 
    * @param webDriver
    *           - driver
    * 
    * @return ob Browser geschlossen war
    */
   public static boolean isClosedOrQuit(WebDriver webDriver)
   {
      boolean result = false;
      if (webDriver instanceof WebDriverWrapper)
      {
         result = ((WebDriverWrapper) webDriver).isClosedOrQuit();
      }
      else
      {
         if (webDriver == null || webDriver.toString().contains("(null)"))
         {
            result = true;
         }
      }
      return result;
   }

   /**
    * Rausfinden ob in IE ausgeführt wird
    * 
    * @param driver
    *           - driver
    * 
    * @return ob in IE ausgeführt wird
    */
   public static boolean isIE(WebDriver driver)
   {
      if (driver instanceof WebDriverWrapper)
      {
         return ((WebDriverWrapper) driver).isIE();
      }
      return false;
   }

   private static WebDriverWrapper erzeugeWebDriverMitRetry(final boolean isIE, String url, String grid)
   {
      WebDriverWrapper driver = null;
      driver = WebDriverManager.startDriver(url, isLocal(), grid, isIE);
      return driver;
   }

   private static WebDriverWrapper startDriver(String url, final boolean local, final String seleniumGridUrl,
         boolean isIE)
   {
      Validate.notEmpty(url, "Url ist nicht gesetzt.");

      RemoteWebDriver driver = null;
      String browser = "";
      String remoteOderLokal = local ? "Lokaler" : "Remote";
      boolean useNoProxy = useNoProxy();
      String proxyOderDirekt = useNoProxy ? "(kein Proxy)" : "(via Proxy)";
      if (isIE)
      {
         PropertyManager properties = PropertyManager.getInstance();
         String iePath = properties.getProperty("webdriver.iedriver.path");
         System.setProperty("webdriver.ie.driver", iePath);

         browser = "InternetExplorer";
         driver = IEWebDriverCreator.createIEWebDriver(url, local, useNoProxy, seleniumGridUrl);
      }
      else
      {
         browser = "Firefox";
         driver = FireFoxWebDriverCreator.createFirefoxWebDriver(url, useNoProxy, local, seleniumGridUrl);
      }
      URL urlObject = UrlCreator.createURL(url);
      String browserInfo = String.format("%s %s%s auf %s:%s und %s", remoteOderLokal, browser, proxyOderDirekt,
            urlObject.getHost(), urlObject.getPort(), urlObject.getQuery());
      return new WebDriverWrapper(driver, browserInfo, local, isIE);
   }

   private static boolean useNoProxy()
   {
      String key = "noproxy";
      String useNoProxy = PropertyManager.getInstance().getProperty(key, System.getProperty(key, DEFAULT_VALUE));
      return "true".equals(useNoProxy);
   }

   private static void stopDriverOrFail(WebDriver webDriver)
   {
      if (!isClosedOrQuit(webDriver))
      {
         // webDriver.close();
         webDriver.quit();
         loggeBrowserSchliessenWennMoeglich(webDriver);
      }
   }

   private static void loggeBrowserSchliessenWennMoeglich(WebDriver webDriver)
   {
      if (webDriver instanceof WebDriverWrapper)
      {
         logger.info(((WebDriverWrapper) webDriver).getBrowserInfo() + " geschlossen.");
      }
   }

   private static boolean isLocal()
   {
      final String remote = System.getProperty("remote", DEFAULT_VALUE);
      final boolean isLocal = remote.equals(DEFAULT_VALUE);
      return isLocal;
   }

}
