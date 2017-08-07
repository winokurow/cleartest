package de.clearit.test.framework;

import org.openqa.selenium.WebDriver;

/**
 * WebDriverInjectable
 */
public interface WebDriverInjectable
{
   /**
    * @param driver
    *           - der verwendet werden soll
    */
   void setDriver(WebDriver driver);
}
