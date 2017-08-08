package de.clearit.test.pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import de.clearit.test.common.ErrorPageHelper;
import de.clearit.test.data.HinweisBestaetigung;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.framework.PageObject;
import de.clearit.test.framework.elemente.WebBaseElement;

/**
 * Page Logged in (menu).
 *
 * @author Ilja Winokurow
 */
public class LoggedInPage extends PageObject
{

   /* Das Objekt der vorherigen Seite */
   protected LoggedInPage previousPage;

   /* Der Text 'Maskenname' */
   protected WebBaseElement maskennameText = new WebBaseElement(By.cssSelector("div[id*=':headerPanel'] h3"));

   public static final String MASKENAENDERUNG_HINWEIS_DIALOG = "maskenaenderungHinweisDialog_dialog";

   private ErrorPageHelper fehlermeldungenPageHelfer = new ErrorPageHelper();

   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    */
   public LoggedInPage(final WebDriver driver, final LoggedInPage previousPage)
   {
      this(driver, previousPage, "", false, true);
   }
   
   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    * @param title
    *           - der Name der Seite
    */
   public LoggedInPage(final WebDriver driver, final LoggedInPage previousPage, String title)
   {
      this(driver, previousPage, title, false, true);
   }

   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    * @param title
    *           - der Name der Seite
    * @param timeoutInSeconds
    *           - TimeOut in Sekunden
    */
   public LoggedInPage(final WebDriver driver, final LoggedInPage previousPage, String title, long timeoutInSeconds)
   {
      this(driver, previousPage, title, false, true, timeoutInSeconds);
   }

   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    * @param title
    *           - der Name der Seite
    * @param isVerify
    */
   public LoggedInPage(final WebDriver driver, final LoggedInPage previousPage, String title, final boolean isVerify)
   {
      this(driver, previousPage, title, isVerify, isVerify); // genau so, wenn isVerify, dann auch auf
                                                             // page load warten
   }

   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    * @param title
    *           - der Name der Seite
    * @param waitForPageLoad
    *           - ob gewartet wird bis Seite geladen ist
    * @param isVerify
    *           - on Elemente geprüft werden
    */
   private LoggedInPage(final WebDriver driver, final LoggedInPage previousPage, String title,
         final boolean waitForPageLoad, final boolean isVerify)
   {
      this(driver, previousPage, title, isVerify, isVerify, -1);
   }

   /**
    * Constructor.
    *
    * @param driver
    *           - Webdriver
    * @param previousPage
    *           - Die vorherige Seite
    * @param title
    *           - der Name der Seite
    * @param waitForPageLoad
    *           - ob gewartet wird bis Seite geladen ist
    * @param isVerify
    *           - on Elemente geprüft werden
    * @param timeoutInSeconds
    *           - wie lang wird es auf Elemente gewartet werden
    */
   private LoggedInPage(final WebDriver driver, final LoggedInPage previousPage, String title,
         final boolean waitForPageLoad, final boolean isVerify, long timeoutInSeconds)
   {
      if (previousPage == null)
      {
         ExecutionTimerManager.getExecutionTimer().start(title + " aufrufen.");
      }
      this.driver = driver;
      this.previousPage = previousPage;
      this.title = title;
      konfiguriereLogger();
      if (isVerify)
      {
         warteAufPageLoadWennNoetig(waitForPageLoad);
      }
      if (timeoutInSeconds == -1)
      {
         warteAufElementeWennNoetig(isVerify);
      }
      else
      {
         warteAufElementeWennNoetig(isVerify, timeoutInSeconds);
      }

   }

   /**
    * warteAufElementeWennNoetig.
    *
    * Warten bis die Hauptelementen angezeigt werden (nur wenn der Schalter ist true).
    *
    * @param isVerify
    *           - der Schalter
    */
   private void warteAufElementeWennNoetig(final boolean isVerify)
   {
      if (isVerify)
      {
         this.waitForMainElementsIsShown();
      }
   }

   /**
    * Gibt die vorherige Seite zurück.
    *
    * @return die vorherige Seite.
    */
   protected LoggedInPage getPreviousPage()
   {
      return previousPage;
   }

   /**
    * Page titel von header lesen
    * <p>
    * Der Name der Seite auslesen
    *
    * @return der Name
    */
   public String doGetPageTitle()
   {
      maskennameText.waitForVisible();
      String value = maskennameText.getText();
      logger.info("Die Seite hat folgende Name: " + value);
      return value;
   }

   /**
    * doGetErrorMessage
    * <p>
    * Die Fehlermeldungen auslesen
    *
    * @return die Fehlermeldung
    */
   public String doGetErrorMessage()
   {
      return fehlermeldungenPageHelfer.readErrorMessage();
   }

   /**
    * isErrorMessageShown
    * <p>
    * Prüfe ob Fehlermeldung angezeigt
    *
    * @return ob Meldung existiert
    */
   public boolean isErrorMessageShown()
   {
      return fehlermeldungenPageHelfer.isErrorMessageShown();
   }

   /**
    * isElementAktiv
    *
    * Prüfen ob das Element aktiv ist.
    *
    * @param element
    *           - Das Element zu prüfen
    * @param elementName
    *           - die Bezeichnung des Elements zu prüfen (z. B. "Das Textfeld 'Name'")
    *
    * @return - ob das Element aktiv ist
    */
   protected boolean isElementAktiv(final WebBaseElement element, String elementName)
   {
      boolean isReadOnly = false;
      element.waitForVisible();
      if ((element.getAttribute("aria-readonly") != null && element.getAttribute("aria-readonly").equals("true"))
            || (element.getAttribute("readonly") != null && element.getAttribute("readonly").equals("true")))
      {

         isReadOnly = true;

      }
      boolean isAktiv = element.isEnabled()
            && !(element.getAttribute("class").contains("ui-state-disabled") || (isReadOnly));
      String text = isAktiv ? " ist aktiv." : " ist nicht aktiv.";
      logger.info(elementName + text);
      return isAktiv;
   }

   /**
    * doClickButtonAbbrechen
    * <p>
    *
    * Den Button Abbrechen betätigen.
    *
    * @param abbrechenButton
    *           - Abbrechen Button Element
    * @param dialogID
    *           - Dialog 'Maskenänderung' id
    * @return vorherige Seite
    *
    */
   protected LoggedInPage doClickButtonAbbrechen(WebBaseElement abbrechenButton, String dialogID)
   {
      clickWithPageChange(abbrechenButton, "Den Button 'Abbrechen' betätigen.");
      if (!(dialogID.isEmpty()))
      {
         HinweisPage hinweisPage = new HinweisPage(driver, dialogID);
         if (hinweisPage.isShown())
         {
            hinweisPage.doClickButton(HinweisBestaetigung.JA);
         }
      }

      previousPage.waitForMainElementsIsShown();
      return previousPage;
   }

   /**
    * doGetValue
    *
    * Den Wert auslesen.
    *
    * @param element
    *           - das Element
    * @param description
    *           - die Beschreibung
    * 
    * @return den Wert
    */
   protected String doGetValue(WebBaseElement element, String description)
   {
      String value = element.getValue();
      logger.info(description + " = " + value);
      return value;
   }

   /**
    * warteAufElementeWennNoetig.
    *
    * Warten bis die Hauptelementen angezeigt werden (nur wenn der Schalter ist true).
    *
    * @param isVerify
    *           - der Schalter
    */
   private void warteAufElementeWennNoetig(final boolean isVerify, long timeout)
   {
      if (isVerify)
      {
         this.waitForMainElementsIsShown(timeout);
      }
   }

   private void warteAufPageLoadWennNoetig(final boolean waitForPageLoad)
   {
      if (waitForPageLoad)
      {
         this.waitForPageIsLoad();
      }
   }

   private void konfiguriereLogger()
   {
      if (title == null)
      {
         throw new RuntimeException("Kein titel gefunden, richtig initialisiert?");
      }
      logger = Logger.getLogger(title);
   }
}
