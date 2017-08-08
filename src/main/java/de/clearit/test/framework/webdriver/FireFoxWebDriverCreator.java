package de.clearit.test.framework.webdriver;

import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.clearit.test.framework.PropertyManager;

/**
 * Creator für Firefox Webdriver (WebDriver initialisieren und Browser starten).
 */
public final class FireFoxWebDriverCreator
{

   /** Browser Start URL */
   private static final String ABOUT_BLANK = "about:blank";

   /**
    * Constructor.
    * 
    * Private constructor to hide the implicit public one
    */
   private FireFoxWebDriverCreator()
   {
   }

   /**
    * Create Firefox WebDriver.
    * 
    * @param url
    *           - base url
    * @param useNoProxy
    *           - ob Proxy benutzt wird
    * @param local
    *           - ob lokal Treiber benutzt wird
    * @param seleniumGridUrl
    *           - Selenium Grid url
    * 
    * @return das Treiber
    * @throws URISyntaxException
    */
   protected static RemoteWebDriver createFirefoxWebDriver(String url, final boolean useNoProxy, final boolean local,
         final String seleniumGridUrl)
   {
      RemoteWebDriver driver;
      PropertyManager properties = PropertyManager.getInstance();
      String geckoPath = properties.getProperty("webdriver.geckodriver.path");
      System.setProperty("webdriver.gecko.driver", geckoPath);
      String firefoxPath = properties.getProperty("firefox.path");
      System.setProperty("firefox.bin", firefoxPath);

      FirefoxProfile profile = createFirefoxBaseProfile();
      profile.setAcceptUntrustedCertificates(true);
      profile.setAssumeUntrustedCertificateIssuer(false);
      final DesiredCapabilities capabilities = DesiredCapabilities.firefox();

      if (useNoProxy)
      {
         capabilities.setCapability(CapabilityType.PROXY, new Proxy().getNoProxy());
      }
      else
      {
         profile.setPreference("network.proxy.type", ProxyType.AUTODETECT.ordinal());

      }

      if (local)
      {
         // capabilities.setCapability("marionette", true);
         capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
         driver = new FirefoxDriver(createFirefoxBinary(), profile, capabilities);
      }
      else
      {

         capabilities.setBrowserName("firefox");
         capabilities.setPlatform(Platform.WINDOWS);
         capabilities.setVersion("39");
         capabilities.setCapability(FirefoxDriver.PROFILE, profile);
         driver = new RemoteWebDriver(UrlCreator.createURL(seleniumGridUrl), capabilities);
      }

      driver.manage().deleteAllCookies();
      // TODO Workaround für Firefox < 44
      //driver.manage().window().maximize();
      
      driver.manage().timeouts().pageLoadTimeout(100, TimeUnit.SECONDS);
      driver.manage().timeouts().setScriptTimeout(50, TimeUnit.SECONDS);
      driver.get(url);
      return driver;
   }

   /**
    * Firefox binary Pfad auslesen. Wenn Pfad ist nicht eingegeben, wird vordefinierte Binary benutzt. Firefox Binary
    * erstellen.
    * <p>
    *
    * @return erstellte Firefox Binary
    *
    */
   private static FirefoxBinary createFirefoxBinary()
   {

      String firefoxBin = System.getProperty("firefox.bin");
      
      if (firefoxBin == null)
      {
         firefoxBin = PropertyManager.getInstance().getProperty("firefox.path", null);
      }
      
      final FirefoxBinary firefoxBinary;
      if (firefoxBin != null)
      {
    	  firefoxBinary = new FirefoxBinary(new File(firefoxBin));
      }
      else
      {
    	  firefoxBinary = new FirefoxBinary();
      }
      return firefoxBinary;
   }

   private static FirefoxProfile createFirefoxBaseProfile()
   {

      // FirefoxProfile profile = new FirefoxProfile(
      // (new File(ClassLoader.getSystemResource("profile/firefox.default").toURI())));
      FirefoxProfile profile = new FirefoxProfile();
      profile.setPreference("network.proxy.type", 0);
      profile.setPreference("browser.startup.homepage", ABOUT_BLANK);
      profile.setPreference("startup.homepage_welcome_url", ABOUT_BLANK);
      profile.setPreference("startup.homepage_welcome_url.additional", ABOUT_BLANK);
      return profile;
   }
}
