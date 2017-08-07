package de.clearit.test.framework.elemente;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import de.clearit.test.common.SeitenladePruefer;

/**
 * Framework GUI Element.
 * 
 * @author Ilja Winokurow
 */
public class SelectElement extends GuiElement
{

   /* Select Element */
   Select selectElement;

   /**
    * Constructor.
    * 
    * @param by
    *           - Locator des Elements (By)
    * 
    */
   public SelectElement(final By by)
   {
      super(by);
   }

   /**
    * Constructor.
    * 
    * @param by
    *           - Element locator (By).
    * @param driver
    *           - WebDriver instance to use.
    */
   public SelectElement(final By by, final WebDriver driver)
   {
      super(by, driver);
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
   public SelectElement(final By by, final WebDriver driver, final long timeOutInSeconds)
   {
      super(by, driver, timeOutInSeconds);
   }

   /**
    * Der Wert im Auswahlfeld auswählen.
    * 
    * @param value
    *           - wert zu setzen
    * @param sleepMillis
    *           - wie lange nach der Auswahl gewartet wird
    */
   public void select(String value)
   {
      if (value != null && !(value.isEmpty()))
      {
         checkElement();
         waitForVisible();
         String newValue = value.replace("EMPTY", "");
         newSelectElementMitStaleRetry();
         GuiElement option = new GuiElement(By.cssSelector("option[value='" + newValue + "']"), driver);
         option.waitForPresent();
         selectElement.selectByValue(newValue);
         SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      }
   }

   /**
    * Der Wert im Auswahlfeld auswählen.
    * 
    * @param selectboxText
    *           - Wert zu setzen
    */
   public void selectByVisibleText(String text)
   {
      if (text != null && StringUtils.isNotEmpty(text))
      {
         checkElement();
         waitForVisible();
         newSelectElementMitStaleRetry();
         selectElement.selectByVisibleText(text);
         SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      }
   }

   /**
    * Den Eintrag der im Auswahlfeld ausgewählt ist.
    * 
    * @param String
    *           - Eintrag der ausgewählt ist.
    * @return
    */
   public String getTextSelectedOption()
   {
      newSelectElementMitStaleRetry();
      return selectElement.getFirstSelectedOption().getText();
   }

   /**
    * getOptionsList<br>
    * 
    * Die Auswahlmöglichkeiten für Auswahlfeld auslesen.
    * 
    * @return Die Auswahlmöglichkeiten.
    */
   public List<WebElement> getOptionsList()
   {
	   checkElement();
      return element.findElements(By.tagName("option"));
   }

   /**
    * Überprüft ob die Option aktiv ist.
    * 
    * @param value
    *           - der Wert der Option.
    * 
    * @return true wenn die Option aktiv ist, false andernfalls.
    */
   public boolean isOptionActive(String value)
   {
      WebElement el = element.findElement(By.cssSelector("option[value='" + value + "']"));
      return el.getAttribute("disabled") == null;
   }

   /**
    * getOptionsList<br>
    * 
    * Die Auswahlmöglichkeiten für Auswahlfeld auslesen.
    * 
    * @return die Auswahlmöglichkeiten.
    * 
    */
   public List<String> getOptionsTextList()
   {
      List<String> options = new ArrayList<>();
      List<WebElement> elements = getOptionsList();
      for (WebElement element : elements)
      {
         options.add(element.getText());
      }
      return options;
   }

   private void newSelectElementMitStaleRetry()
   {
      try
      {
         checkElement();
         selectElement = new Select(element);
      }
      catch (StaleElementReferenceException staleElementReferenceException)
      {
         loggeStaleElementReference(staleElementReferenceException);
         findAndSetElementWithStaleRetry();
         checkElement();
         selectElement = new Select(element);
      }
   }
}
