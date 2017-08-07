package de.clearit.test.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;

import de.clearit.test.common.SeitenladePruefer;
import de.clearit.test.data.HinweisBestaetigung;
import de.clearit.test.framework.PageObject;
import de.clearit.test.framework.elemente.GuiElement;

/**
 * Hinweis.
 */
public class HinweisPage extends PageObject
{

   /* Text */
   protected GuiElement text;

   protected GuiElement bestaetigenButton;

   protected GuiElement abbrechenButton;

   /* Dialog */
   protected GuiElement dialog;

   /* Der Text */
   protected String textLocator = "//div[contains(@id,':%s')]/div[2]/div[1]/div[1]";

   protected String bestaetigenButtonLocator = "button[id*='%s'][id*='hotKeyButtonDialogBestaetigen'] span";

   protected String abbrechenButtonLocator = "button[id*='%s'][id*='hotKeyButtonDialogAbbrechen'] span";

   /* Dialog */
   protected String dailogLocator = "div[id*=':%s']";

   public static final String KLICK_AUF_OK_BUTTON = "Klick auf OK-Button.";

   /**
    * Constructor.
    * 
    * @param driver
    *           - driver
    * @param dialogID
    *           - Teil der dialogID zum finden von Elementen
    */
   public HinweisPage(final WebDriver driver, final String dialogIDpart)
   {
      this(driver, dialogIDpart, true);
   }

   /**
    * Constructor.
    * 
    * @param driver
    *           - driver
    * @param dialogIDpart
    *           - Teil der dialogID zum finden von Elementen
    * @param checkElements
    *           - ob die Elemente geprüft werden sollen
    */
   public HinweisPage(final WebDriver driver, final String dialogIDpart, boolean checkElements)
   {
      this.driver = driver;
      logger = Logger.getLogger("Hinweis-Dialog");

      bestaetigenButton = new GuiElement(By.cssSelector(String.format(bestaetigenButtonLocator, dialogIDpart)), driver);
      abbrechenButton = new GuiElement(By.cssSelector(String.format(abbrechenButtonLocator, dialogIDpart)), driver);
      dialog = new GuiElement(By.cssSelector(String.format(dailogLocator, dialogIDpart)), driver);
      text = new GuiElement(By.cssSelector(String.format(textLocator, dialogIDpart)), driver);
      if (checkElements)
      {
         waitForMainElementsIsShown();
      }
      else
      {
         waitForPageIsLoad();
      }
   }

   /**
    * Builder
    * 
    * @param driver
    * @param dialogIDpart
    * @return HinweisPage
    */
   public static HinweisPage mit(final WebDriver driver, final String dialogIDpart)
   {
      return new HinweisPage(driver, dialogIDpart);
   }

   /**
    * isTextShown
    * 
    * Ob Text angezeigt ist
    * 
    * @param text
    *           - Text zu suchen
    * 
    * @return ob Text angezeigt ist
    */
   public boolean isTextShown(final String text)
   {
      return isTextShownOnPage(text);
   }

   private boolean isTextShownOnPage(final String text)
   {
      return !driver.findElements(By.xpath("//*[contains(text(),'" + text + "')]")).isEmpty();
   }

   /**
    * doGetText
    * 
    * Den Text auslesen
    * 
    * 
    * @return der Text
    */
   public String doGetText()
   {
      return text.getText();
   }

   /**
    * doClickNein
    * 
    * Button Nein betätigen
    * 
    */
   public void doClickNein()
   {
      logger.info("Button Nein betätigen");
      abbrechenButton.waitForVisible();
      abbrechenButton.click();
   }

   /**
    * doPressEsc doClickOK
    * 
    * Button OK betätigen
    * 
    */
   public void doClickOk()
   {
      logger.info("Button Ok betätigen");
      bestaetigenButton.waitForVisible();
      bestaetigenButton.click();
   }

   /**
    * Button OK betätigen um zur vorherigen Seite zu landen.
    * 
    * @param previousPage
    *           - die vorherige Seite
    */
   public void doClickOkMitPreviousPage(LoggedInPage previousPage)
   {
      logger.info("Button Ok betätigen");
      bestaetigenButton.waitForVisible();
      bestaetigenButton.click();
      previousPage.waitForPageIsLoad(3);

   }

   /**
    * Die Taste Escape drücken.
    * 
    */
   private void doPressEsc()
   {
      logger.info("Die Taste Escape drücken.");
      text.sendkeys(Keys.ESCAPE);
   }

   /**
    * doClickJa
    * 
    * Button Ja betätigen
    * 
    */
   public void doClickJa()
   {
      logger.info("Button Ja betätigen");
      bestaetigenButton.waitForVisible();
      bestaetigenButton.click();
      this.waitForPageIsLoad();
   }

   /**
    * Bestätigung je nach Enum clicken
    * 
    * @param hinweisBestaetigung
    */
   public void doClickButton(HinweisBestaetigung hinweisBestaetigung)
   {
      if (hinweisBestaetigung == HinweisBestaetigung.JA)
      {
         doClickJa();
      }
      if (hinweisBestaetigung == HinweisBestaetigung.NEIN)
      {
         doClickNein();
      }
      if (hinweisBestaetigung == HinweisBestaetigung.ESC)
      {
         doPressEsc();
      }
   }

   /**
    * doClickJa wenn angezeigt wird
    * 
    * Button Ja betätigen
    * 
    */
   public void doClickJaWennAngezeigt()
   {
      if (isShown())
      {
         doClickJa();
         SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      }
   }

   /**
    * doPressEsc
    * 
    * Die Taste 'Enter' drücken.
    * 
    */
   public void doPressEnter()
   {
      logger.info("Die Taste 'Enter' drücken.");
      text.sendkeys(Keys.ENTER);
      this.waitForPageIsLoad();
   }

   /**
    * isShown
    * 
    * Ob Hinweis angezeigt wird
    * 
    */
   public boolean isShown()
   {
      boolean isPresent = false;
      if (dialog.isPresent())
      {
         isPresent = dialog.getAttribute("aria-hidden").contains("false");
      }
      return isPresent;
   }

}
