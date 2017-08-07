package de.clearit.test.common;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import de.clearit.test.framework.WebDriverInjectable;
import de.clearit.test.framework.elemente.GuiElement;

/**
 * Überprüfen die Fehlermeldungen
 */
public class ErrorPageHelper implements WebDriverInjectable
{
   /* Logger */
   private final static Logger logger = BasisLogger.LOGGER;

   /* Web Driver */
   private WebDriver driver;

   /* Der Text der Meldung */
   protected GuiElement messageText = new GuiElement(By.cssSelector("ul[id*='messages'] span"));

   /**
    * Fehlermeldung auslesen
    *
    * @return ausgelesene Fehlermeldung
    */
   public String readErrorMessage()
   {
      StringBuilder errorMessageBuilder = new StringBuilder();
      List<WebElement> messages = driver.findElements(messageText.getBy());
      if (messages.size() > 0)
      {
         for (final WebElement element : messages)
         {
            errorMessageBuilder.append(element.getText().trim());
         }
      }
      String errorMessage = errorMessageBuilder.toString();
      if (errorMessage.trim().isEmpty())
      {
         logger.info("Folgende Meldung ist angezeigt: " + errorMessage);
      }
      else
      {
         logger.info("Keine Fehlermeldung gefunden");
      }

      return errorMessage;
   }

   /**
    * Prüfe ob eine Fehlermeldung angezeigt ist
    *
    * @return ob Meldung angezeigt
    */
   public boolean isErrorMessageShown()
   {
      final boolean isPresent = messageText.isPresent();
      if (isPresent)
      {
         logger.info("Die Fehlermeldung ist angezeigt");
      }
      else
      {
         logger.info("Die Fehlermeldung ist nicht angezeigt");
      }

      return isPresent;
   }

   /**
    * Die Webdriver setzen
    *
    * @param driver
    *           - WebDriver
    */
   @Override
   public void setDriver(WebDriver driver)
   {
      this.driver = driver;
      messageText.setDriver(driver);
   }

}
