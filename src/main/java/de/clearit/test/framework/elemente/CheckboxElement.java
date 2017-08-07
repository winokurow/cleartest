package de.clearit.test.framework.elemente;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Checkbox GUI Element.
 * 
 * @author Ilja Winokurow
 */
public class CheckboxElement extends GuiElement
{

   /**
    * Constructor.
    * 
    * @param by
    *           - Locator des Elements (By)
    */
   public CheckboxElement(final By by)
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
    * 
    */
   public CheckboxElement(final By by, final WebDriver driver)
   {
      super(by, driver);
   }

   /**
    * setVisiblePart
    */
   private void setupElement()
   {
      checkElement();
   }

   /**
    * Checkbox markieren.
    */
   public void check()
   {
      setupElement();
      checkElement();
      if (!isChecked())
      {
         waitForEnable();
         clickElementStaleRetry();
      }
   }

   /**
    * Checkbox Markierung entfernen.
    */
   public void uncheck()
   {
      setupElement();
      checkElement();
      if (isChecked())
      {
         waitForEnable();
         clickElementStaleRetry();
      }
   }

   /**
    * If Element checked.
    * 
    */
   public boolean isChecked()
   {
      checkElement();
      final boolean isChecked = istElementAttributeTrue("aria-checked") || istElementAttributeChecked("checked");
      return isChecked;
   }

   private boolean istElementAttributeTrue(final String attributsName)
   {
      return istElementAttributeGleich(attributsName, "true");
   }

   private boolean istElementAttributeChecked(final String attributsName)
   {
      String attributsWert = elementGetAttributeStaleRetry(attributsName);
      return attributsWert != null;
   }

   private boolean istElementAttributeGleich(final String attributsName, final String erwartet)
   {
      String attributsWert = elementGetAttributeStaleRetry(attributsName);
      return attributsWert != null && attributsWert.equals(erwartet);
   }

}
