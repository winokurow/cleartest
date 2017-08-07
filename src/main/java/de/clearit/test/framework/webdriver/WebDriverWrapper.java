package de.clearit.test.framework.webdriver;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Keyboard;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.FindsByClassName;
import org.openqa.selenium.internal.FindsByCssSelector;
import org.openqa.selenium.internal.FindsById;
import org.openqa.selenium.internal.FindsByLinkText;
import org.openqa.selenium.internal.FindsByName;
import org.openqa.selenium.internal.FindsByTagName;
import org.openqa.selenium.internal.FindsByXPath;
import org.openqa.selenium.remote.RemoteWebDriver;

import de.clearit.test.common.WebdriverUtils;

public class WebDriverWrapper implements WebDriver, JavascriptExecutor, FindsById, FindsByClassName, FindsByLinkText,
      FindsByName, FindsByCssSelector, FindsByTagName, FindsByXPath, HasInputDevices, HasCapabilities, TakesScreenshot
{

   /* webDriver instance */
   private final RemoteWebDriver webDriver;

   /* ob close was already called */
   private boolean closeCalled;

   /* ob quit was already called */
   private boolean quitCalled;

   /* ob local */
   private boolean local;

   /* browserInfo */
   private final String browserInfo;

   /* ob IE */
   private boolean isIE = false;

   /**
    * Constructor.
    * 
    * @param webDriver
    *           - driver
    * @param local
    *           - der Text, der wird in Log angezeigt
    * @param local
    *           - ob auf lokale Rechner ausgeführt wird
    * @param isIE
    *           - ob IE
    */
   public WebDriverWrapper(final RemoteWebDriver webDriver, String browserInfo, boolean local, boolean isIE)
   {
      this.local = local;
      this.webDriver = webDriver;
      this.browserInfo = browserInfo;
      this.isIE = isIE;
   }

   @Override
   public void get(String url)
   {
      webDriver.get(url);
   }

   @Override
   public String getCurrentUrl()
   {
      return webDriver.getCurrentUrl();
   }

   @Override
   public String getTitle()
   {
      return webDriver.getTitle();
   }

   @Override
   public List<WebElement> findElements(By by)
   {
      return webDriver.findElements(by);
   }

   @Override
   public WebElement findElement(By by)
   {
      return webDriver.findElement(by);
   }

   @Override
   public String getPageSource()
   {
      return webDriver.getPageSource();
   }

   @Override
   public void close()
   {
      webDriver.close();
   }

   @Override
   public void quit()
   {
      if (!quitCalled) // spart ein synchronize
      {
         quitIfNeeded();
      }
   }

   private synchronized void quitIfNeeded()
   {
      if (!quitCalled)
      {
         webDriver.quit();
         quitCalled = true;
      }
   }

   @Override
   public Set<String> getWindowHandles()
   {
      return webDriver.getWindowHandles();
   }

   @Override
   public String getWindowHandle()
   {
      return webDriver.getWindowHandle();
   }

   @Override
   public TargetLocator switchTo()
   {
      return webDriver.switchTo();
   }

   @Override
   public Navigation navigate()
   {
      return webDriver.navigate();
   }

   @Override
   public Options manage()
   {
      return webDriver.manage();
   }

   /**
    * Overrided toString Method.
    * 
    * @return a string representation of the object.
    */
   @Override
   public String toString()
   {
      return super.toString();
   }

   @Override
   public <T> T getScreenshotAs(OutputType<T> outputType)
   {
      return ((TakesScreenshot) webDriver).getScreenshotAs(outputType);
   }

   public String getIpOfNode()
   {
      if (local)
      {
         return "localhost";
      }
      else
      {
         return WebdriverUtils.getIPOfNode(webDriver);
      }
   }

   @Override
   public Capabilities getCapabilities()
   {
      return webDriver.getCapabilities();
   }

   @Override
   public Keyboard getKeyboard()
   {
      return webDriver.getKeyboard();
   }

   @Override
   public Mouse getMouse()
   {
      return webDriver.getMouse();
   }

   @Override
   public WebElement findElementByXPath(String using)
   {
      return webDriver.findElementByXPath(using);
   }

   @Override
   public List<WebElement> findElementsByXPath(String using)
   {
      return webDriver.findElementsByXPath(using);
   }

   @Override
   public WebElement findElementByTagName(String using)
   {
      return webDriver.findElementByTagName(using);
   }

   @Override
   public List<WebElement> findElementsByTagName(String using)
   {
      return webDriver.findElementsByTagName(using);
   }

   @Override
   public WebElement findElementByCssSelector(String using)
   {
      return webDriver.findElementByCssSelector(using);
   }

   @Override
   public List<WebElement> findElementsByCssSelector(String using)
   {
      return webDriver.findElementsByCssSelector(using);
   }

   @Override
   public WebElement findElementByName(String using)
   {
      return webDriver.findElementByName(using);
   }

   @Override
   public List<WebElement> findElementsByName(String using)
   {
      return webDriver.findElementsByName(using);
   }

   @Override
   public WebElement findElementByLinkText(String using)
   {
      return webDriver.findElementByLinkText(using);
   }

   @Override
   public List<WebElement> findElementsByLinkText(String using)
   {
      return webDriver.findElementsByLinkText(using);
   }

   @Override
   public WebElement findElementByPartialLinkText(String using)
   {
      return webDriver.findElementByPartialLinkText(using);
   }

   @Override
   public List<WebElement> findElementsByPartialLinkText(String using)
   {
      return webDriver.findElementsByPartialLinkText(using);
   }

   @Override
   public WebElement findElementByClassName(String using)
   {
      return webDriver.findElementByClassName(using);
   }

   @Override
   public List<WebElement> findElementsByClassName(String using)
   {
      return webDriver.findElementsByClassName(using);
   }

   @Override
   public WebElement findElementById(String using)
   {
      return webDriver.findElementById(using);
   }

   @Override
   public List<WebElement> findElementsById(String using)
   {
      return webDriver.findElementsById(using);
   }

   @Override
   public Object executeScript(String script, Object... args)
   {
      return webDriver.executeScript(script, args);
   }

   @Override
   public Object executeAsyncScript(String script, Object... args)
   {
      return webDriver.executeAsyncScript(script, args);
   }

   public synchronized void initClosedAndQuit()
   {
      closeCalled = false;
      quitCalled = false;
   }

   public synchronized boolean isClosedOrQuit()
   {
      return closeCalled || quitCalled;
   }

   public String getBrowserInfo()
   {
      return browserInfo;
   }

   public boolean isIE()
   {
      return isIE;
   }
}
