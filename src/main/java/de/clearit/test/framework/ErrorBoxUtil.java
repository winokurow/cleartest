package de.clearit.test.framework;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Get current error message
 */
final public class ErrorBoxUtil
{

   /**
    * Logger
    */
   private static final Logger LOGGER = Logger.getLogger(ErrorBoxUtil.class);

   /**
    * private constructor to hide the implicit public one
    */
   private ErrorBoxUtil()
   {

   }

   /**
    * Die Fehlermeldung auslesen
    * 
    * @param driver
    *           - aktueller WebDriver
    * @param timeoutInSekunden
    *           - timeout
    * 
    * @return aktueller Fehlertext in der roten Box, sonst null
    */
   public static String holeAktuellenErrorText(WebDriver driver, long timeoutInSekunden)
   {
      return getTextOrNull(driver, timeoutInSekunden);
   }

   /**
    * Die Fehlermeldung auslesen
    * 
    * @param driver
    *           - aktueller WebDriver
    * @param timeoutInSekunden
    *           - timeout
    * 
    * @return aktueller Fehlertext in der roten Box, sonst null
    */
   private static String getTextOrNull(WebDriver driver, long timeoutInSekunden)
   {
      // Hier auf keinen Fall GuiElement verwenden, sonst gibt es endlosschleifen, denn das GuiElement erzeugt intern
      // auch wieder Fehlermeldungen mit diesem Util
      try
      {
         By by = By.id("messages");
         final WebDriverWait wait = new WebDriverWait(driver, timeoutInSekunden);
         wait.until(ExpectedConditions.presenceOfElementLocated(by));
         WebElement element = driver.findElement(by);
         return element.getText();
      }
      catch (Exception e)
      {
         LOGGER.debug("Fehler beim holen der Messagebox: " + e);
         return null;
      }
   }
}
