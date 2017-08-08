package de.clearit.test.framework.elemente;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.InvalidElementStateException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import de.clearit.test.common.SeitenladePruefer;
import de.clearit.test.common.TestUtils;
import de.clearit.test.common.WebdriverUtils;
import de.clearit.test.exceptions.WebdriverNotSetException;
import de.clearit.test.framework.PropertyManager;
import de.clearit.test.framework.WebDriverInjectable;
import de.clearit.test.framework.webdriver.WebDriverWrapper;

/**
 * Framework GUI Element.
 *
 * @author Ilja Winokurow
 */
public class WebBaseElement implements WebDriverInjectable
{

   private static final Logger logger = Logger.getLogger("GuiElement Klass");

   protected static final long TIMEOUT_IN_SECONDS_DEFAULT = 30;

   public static final int DEFAULT_VERZOEGERUNG_IN_MILLISEKUNDEN = 100;

   /* WebElement */
   protected WebElement element;

   /* WebDriver */
   protected WebDriver driver;

   /* Element locator */
   protected By by;

   /* Eine Beschreibung */
   protected String description;

   /* Element finding timeout */
   protected long timeOutInSeconds = TIMEOUT_IN_SECONDS_DEFAULT;

   /**
    * Constructor.
    *
    * @param by
    *           - Locator des Elements (By)
    *
    */
   public WebBaseElement(final By by)
   {
      this(by, null);
   }

   /**
    * Constructor.
    *
    * @param by
    *           - Element locator (By).
    * @param driver
    *           - WebDriver instance to use.
    */
   public WebBaseElement(final By by, final WebDriver driver)
   {
      this(by, driver, Long.parseLong(PropertyManager.getInstance().getProperty("html.element.timeout",
            String.valueOf(TIMEOUT_IN_SECONDS_DEFAULT))));
   }

   /**
    * Constructor.
    *
    * @param by
    *           - Element locator (By).
    * @param driver
    *           - WebDriver instance to use.
    * @param description
    *           - die Beschreibung.
    */
   public WebBaseElement(final By by, final WebDriver driver, String description)
   {
      this(by, driver, Long.parseLong(PropertyManager.getInstance().getProperty("html.element.timeout",
            String.valueOf(TIMEOUT_IN_SECONDS_DEFAULT))));
      this.description = description;

   }

   /**
    * Constructor.
    *
    * @param by
    *           - Element locator (By).
    * @param driver
    *           - WebDriver instance to use.
    * @param timeOutInSeconds
    *           - WebDriver timeout for this element.
    */
   public WebBaseElement(final By by, final WebDriver driver, final long timeOutInSeconds)
   {
      this();
      this.by = by;
      this.driver = driver;
      this.timeOutInSeconds = timeOutInSeconds;
   }

   /**
    * Constructor.
    *
    * Default constructor ohne Parameters ist verboten.
    *
    */
   private WebBaseElement()
   {
   }

   /**
    * Die Beschreibung zurückliefern.
    *
    * return die Beschreibung
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * isPresent
    * <p>
    *
    * Is element presents in DOM.
    *
    * @return is element present
    *
    */
   public boolean isPresent()
   {
      return !driver.findElements(by).isEmpty();
   }

   /**
    * waitForVisible
    *
    * Warten bis Element angezeigt ist.
    *
    */
   public void waitForVisible()
   {
      waitForVisible(timeOutInSeconds);
   }

   public void waitForVisible(Long timeOutInSeconds)
   {
      checkElement();
      final WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
      wait.until(ExpectedConditions.visibilityOfElementLocated(by));
   }

   /**
    * Wait for element is present.
    */
   public void waitForPresent()
   {
      checkElement();
   }

   /**
    * waitForEnable
    * <p>
    *
    * Wait for element is enable.
    */
   public void waitForEnable()
   {
      waitForPresent();
      Long timeout = timeOutInSeconds;
      final WebDriverWait wait = new WebDriverWait(driver, timeout);
      wait.until(ExpectedConditions.elementToBeClickable(by));
   }

   /**
    * waitForDisable
    *
    * Warten bis Element deaktiviert wird.
    *
    */
   public void waitForDisable()
   {
      waitForPresent();
      WebdriverUtils.waitForDisable(driver, element);
   }

   /**
    * Das Element betätigen.
    */
   public void click()
   {
      checkElement();
      waitForVisible();
      clickElementStaleRetry();
   }

   /**
    * Das Element betätigen.
    */
   public void clickAndWaitForPage()
   {
      click();
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
   }

   /**
    * Text hinzufügen in ein Textfeld.
    *
    * @param text
    *           - Text zu hinzufügen
    */
   public void sendkeys(final CharSequence... text)
   {
      if (text != null)
      {
         checkElement();
         waitForVisible();
         sendKeysStaleRetry(text);
      }
   }

   /**
    * Text hinzufügen in ein Textfeld.
    *
    * @param text
    *           - Text zu hinzufügen
    */
   public void typeWithoutClear(final String text)
   {
      checkElement();
      sendKeysStaleRetry(text);
   }

   /**
    * Text hinzufügen in ein Textfeld.
    *
    * @param text
    *           - Text zu hinzufügen
    */
   public void typeSlowWithoutClear(final String text)
   {
      typeSlowWithoutClear(text, 50);
   }

   /**
    * Text hinzufügen in ein Textfeld.
    *
    * @param text
    *           - Text zu hinzufügen
    */
   public void typeSlowWithoutClear(final String text, int sleepInMillis)
   {
      long time = System.currentTimeMillis();
      waitForEnable();
      for (String s : text.split(""))
      {
         element = driver.findElement(this.getBy());
         sendKeysStaleRetry(s);
         TestUtils.sleep(sleepInMillis);
      }
      logWennMehrAlsEineSekunde(time);
   }

   private void logWennMehrAlsEineSekunde(long time)
   {
      long sekunden = ((System.currentTimeMillis() - time)) / 1000;
      if (sekunden > 0)
      {
         logger.info(String.format("Langsame Eingabe dauerte %s Sekunden", sekunden + ""));
      }
   }

   /**
    * Inhalt eines Feldes löschen. <br>
    * Text langsam (eine Pause zwischen jeder Buchstabe) im Textfeld hinzufügen. <br>
    * Danach wird gewartet bis Seite geladen wird.
    *
    * @param text
    *           - Text zu hinzufügen
    * @param sleepInMillis
    *           - die Pause zwischen Inhalt löschen und Inhalt eingeben (Millisekunden)
    * @param waitForPageLoaded
    *           - ob nach Seite Laden gewartet soll
    * @param waitForPageLoadedSeconds
    *           - wie lange wird es gewartet, bis Seite geladen wird (Sekunden)
    */
   private void typeSlowWithClearAndWithWaitForPage(final String text, int sleepInMillis, boolean waitForPageLoaded,
         int waitForPageLoadedSeconds)
   {
      waitForEnable();
      clickElementStaleRetry();
      clearInput();
      TestUtils.sleep(sleepInMillis);
      for (String s : text.split(""))
      {
         element = driver.findElement(this.getBy());
         sendKeysStaleRetry(s);
      }

      if (waitForPageLoaded)
      {
         SeitenladePruefer.mitDriver(driver).waitForPageIsLoad(waitForPageLoadedSeconds);
      }
   }

   /**
    * Text in ein Textfeld langsam hinzufügen (0.5 sek. Pause zwischen Buchstaben) und die Taste 'Enter' betätigen.
    *
    * @param text
    *           - Text zu hinzufügen
    */
   public void typeSlowWithClearAndWithEnter(final String text)
   {
      typeSlowWithClearAndWithWaitForPage(text, 500, false, 0);
      click();
      sendKeysStaleRetry(Keys.ENTER);

   }

   /**
    * Eingabefeld leeren mit ganz vielen DELETE und BackSpace
    *
    */
   public void clearInput()
   {
      clearInput(50);
   }

   /**
    * Eingabefeld leeren mit ganz vielen DELETE und BackSpace
    *
    * @param anzahlBackspaceUndDelete
    *           Anzahl der Backspace und Delete Tastendrücke
    * @return this GuiElement für Builder Pattern
    */
   public WebBaseElement clearInput(int anzahlBackspaceUndDelete)
   {
      checkElement();
      sendKeysStaleRetry(createArray(Keys.BACK_SPACE, anzahlBackspaceUndDelete));
      sendKeysStaleRetry(createArray(Keys.DELETE, anzahlBackspaceUndDelete));
      return this;
   }

   /**
    * Zur Nutzung in sendKeys, deutlich schneller als die Keys einzeln zu senden!
    * 
    * @param key
    * @param keyAnzahl
    * @return Keys[]
    */
   private Keys[] createArray(Keys key, int keyAnzahl)
   {
      List<Keys> keys = new ArrayList<>();
      for (int i = 0; i < keyAnzahl; i++)
      {
         keys.add(key);
      }
      return keys.toArray(new Keys[0]);
   }

   /**
    * Get value.
    *
    * return den ausgelesenen Wert
    */
   public String getValue()
   {
      checkElement();
      waitForVisible();
      return elementGetAttributeStaleRetry("value");
   }

   /**
    * Get value for an invisible element.
    *
    * return den ausgelesenen Wert
    */
   public String getValueForInvisibleElement()
   {
      checkElement();
      return elementGetAttributeStaleRetry("value");
   }

   /**
    * openMenu.
    *
    * Menü öffnen
    */
   public void openMenu()
   {
      waitForPresent();
      JavascriptExecutor js = (JavascriptExecutor) driver;
      WebElement element = driver.findElement(this.getBy());
      js.executeScript("arguments[0].style.display='block';", element);
   }

   /**
    * Mouse over.
    */
   public void mouseOverClick()
   {
      checkElement();
      final Actions action = new Actions(driver);
      action.moveToElement(element).click().build().perform();
   }

   /**
    * Get Element Text.
    *
    * @return Elements Text
    */
   public String getText()
   {
      checkElement();
      String value = "";
      if (this.isVisible())
      {
         value = element.getText();
      }
      waitForPresent();
      //
      return value;
   }

   /**
    * Get Element Text.
    *
    * @return Elements Text or null if error happens
    */
   public String getTextOrNull()
   {
      try
      {
         return getText();
      }
      catch (Exception e)
      {
         logger.debug(e);
         return null;
      }
   }

   /**
    * If Element visible.
    *
    */
   public boolean isVisible()
   {
      checkElement();
      return isVisibleWithStaleRetry();
   }

   /**
    * If Element enabled.
    *
    */
   public boolean isEnabled()
   {
      checkElement();
      waitForVisible();
      return isEnabledWithStaleRetry();
   }

   /**
    * If Element read only und disabled.
    *
    */
   public boolean isReadOnlyAndDisable()
   {
      checkElement();

      boolean result = (this.getAttribute("disabled") != null) && (this.getAttribute("readonly") != null);

      return result;
   }

   /**
    * If element is disabled;
    */
   public boolean isDisabled()
   {
      return !isEnabled();
   }

   public boolean isDisabledAttribute()
   {
      checkElement();

      boolean result = this.getAttribute("disabled") != null;

      return result;
   }

   /**
    * Get Attribute Value.
    *
    * @param attributeName
    *           - attribute name
    * @return attribute value
    *
    */
   public String getAttribute(final String attributeName)
   {
      checkElement();
      return elementGetAttributeStaleRetry(attributeName);
   }

   /**
    * Zum Element Scrollen
    */
   public void scrollToElement()
   {
      checkElement();
      try
      {
         scrollToElementAction();
      }
      catch (StaleElementReferenceException e)
      {
         findAndSetElement();
         scrollToElementAction();
      }
   }

   private void scrollToElementAction()
   {
      new Actions(driver).moveToElement(element).build().perform();
   }

   /**
    * @return the driver
    */
   public WebDriver getDriver()
   {
      return driver;
   }

   /**
    * @param driver
    *           the driver to set
    */
   public void setDriver(final WebDriver driver)
   {
      this.driver = driver;
   }

   /**
    * @param driver
    *           the driver to set
    * @return die Instanz des GuiElements
    */
   public WebBaseElement withDriver(final WebDriver driver)
   {
      setDriver(driver);
      return this;
   }

   /**
    * @return Returns the by.
    */
   public By getBy()
   {
      return by;
   }

   /**
    * @return Returns the element.
    */
   public WebElement getElement()
   {
      return element;
   }

   protected void findAndSetElementWithStaleRetry()
   {
      try
      {
         findAndSetElement();
      }
      catch (StaleElementReferenceException e)
      {
         loggeStaleElementReference(e);
         findAndSetElement();
      }
   }

   private boolean isEnabledWithStaleRetry()
   {
      try
      {
         return element.isEnabled();
      }
      catch (StaleElementReferenceException e)
      {
         loggeStaleElementReference(e);
         findAndSetElement();
         return element.isEnabled();
      }
   }

   private boolean isVisibleWithStaleRetry()
   {
      try
      {
         return element.isDisplayed();
      }
      catch (StaleElementReferenceException e)
      {
         loggeStaleElementReference(e);
         findAndSetElement();
         return element.isDisplayed();
      }
   }

   /**
    * Klicken auf element. Wenn Stale Element angezeigt wird (he element no longer appears on the DOM of the page.),
    * dann suche nach dem Element und versuch noch mal.
    */
   protected void clickElementStaleRetry()
   {
      try
      {
         element.click();
      }
      catch (StaleElementReferenceException e)
      {
         findAndSetElement();
         element.click();
      }
   }

   protected String elementGetAttributeStaleRetry(String name)
   {
      try
      {
         return element.getAttribute(name);
      }
      catch (StaleElementReferenceException e)
      {
         findAndSetElement();
         return element.getAttribute(name);
      }
   }

   protected void loggeStaleElementReference(StaleElementReferenceException e)
   {
      logger.warn("StaleElementReference aufgetreten, pruefe element erneut...");
      logger.debug(e.getMessage());
      logger.trace(e.getMessage(), e);
   }

   private void findAndSetElement()
   {
      element = driver.findElement(by);
   }

   /**
    * Initialize an element.
    */
   protected void checkElement()
   {
      if (driver == null)
      {
         throw new WebdriverNotSetException();
      }
      if (driver instanceof WebDriverWrapper && ((WebDriverWrapper) driver).isClosedOrQuit())
      {
         throw new RuntimeException("Der Webdriver im GuiElement wurde bereits geschlossen");
      }
      final WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
      try
      {
         wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
      }
      catch (TimeoutException timeoutException)
      {
         String message = erzeugeErrorMessageFuerElementNichtGefunden();
         throw new RuntimeException(message, timeoutException);
      }
      if (element == null)
      {
         findAndSetElementWithStaleRetry();
      }
   }

   /**
    * Verfassen eine Fehlermeldung in der Fall wenn, ein Element nicht gefunden.
    * 
    * @return die erzeugte Fehlermeldung
    */
   protected String erzeugeErrorMessageFuerElementNichtGefunden()
   {
      String message = "Element nicht gefunden '" + by.toString() + "'.";
      return message;
   }

   private void sendKeysStaleRetry(CharSequence... chars)
   {
      try
      {
         element.sendKeys(chars);
      }
      catch (StaleElementReferenceException e)
      {
         findAndSetElement();
         element.sendKeys(chars);
      }
      catch (InvalidElementStateException e)
      {
         throw new InvalidElementStateException("Cannot sendKeys to Element " + by, e);
      }
   }
}
