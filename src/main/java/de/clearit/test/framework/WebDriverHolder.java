package de.clearit.test.framework;

import de.clearit.test.framework.webdriver.WebDriverWrapper;

/**
 * WebDriverHolder
 */
public interface WebDriverHolder
{

   /**
    * @return Die WebDriver Instanz des Holders
    */
   WebDriverWrapper getWebDriver();
}
