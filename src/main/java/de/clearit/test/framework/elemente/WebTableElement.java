package de.clearit.test.framework.elemente;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import de.clearit.test.common.SeitenladePruefer;
import de.clearit.test.exceptions.TableRowNotFoundException;
import de.clearit.test.framework.WebDriverInjectable;

/**
 * WebTableButton
 * 
 * Findet einen Button in einer Webtable anhand der Tabellen ID und der Button Id mit index Platzhalter
 */
public class WebTableElement implements WebDriverInjectable
{

   private String webtableId;
   private String elementIdMitIndexPlatzhalter;
   private WebDriver driver;

   /**
    * Konstruktor zur Benutzung in PageObjekten. @Check wird nicht unterstützt!
    * 
    * @param webtableId
    *           - Die ID der WebTable, z.B. zapAnwendungFormId:vorgangsSucheTabelle_data
    * @param elementIdMitIndexPlatzhalter
    *           - Die ID des Elements mit index Platzhalter, z.B.
    *           zapAnwendungFormId:vorgangsSucheTabelle:%s:vorgangOeffnenBearbeitenButton, wobei %s dann durch den index
    *           ersetzt wird.
    */
   public WebTableElement(final String webtableId, final String elementIdMitIndexPlatzhalter)
   {
      this(null, webtableId, elementIdMitIndexPlatzhalter);
   }

   /**
    * Konstruktor zur Benutzung in PageObjekten. Die Tabellen ID wird durch den Platzhalter automatisch
    * ermittelt. @Check wird nicht unterstützt!
    * 
    * @param elementIdMitIndexPlatzhalter
    *           - Die ID des Elements mit index Platzhalter, z.B.
    *           zapAnwendungFormId:vorgangsSucheTabelle:%s:vorgangOeffnenBearbeitenButton, wobei %s dann durch den index
    *           ersetzt wird.
    */
   public WebTableElement(final String elementIdMitIndexPlatzhalter)
   {
      this(null, null, elementIdMitIndexPlatzhalter);
   }

   /**
    * Erzeugt eine WebTableButton inline.
    * 
    * @param driver
    *           - der Webdriver
    * @param webtableId
    *           - Die ID der WebTable, z.B. zapAnwendungFormId:vorgangsSucheTabelle_data
    * @param elementIdMitIndexPlatzhalter
    *           - Die ID des Elements mit index Platzhalter, z.B.
    *           zapAnwendungFormId:vorgangsSucheTabelle:%s:vorgangOeffnenBearbeitenButton, wobei %s dann durch den index
    *           ersetzt wird.
    * @return {@link WebTableElement}
    */
   public static WebTableElement mit(final WebDriver driver, final String webtableId,
         final String elementIdMitIndexPlatzhalter)
   {
      return new WebTableElement(driver, webtableId, elementIdMitIndexPlatzhalter);
   }

   /**
    * Erzeugt eine WebTableButton inline. Die Tabellen ID wird durch den Platzhalter automatisch ermittelt.
    * 
    * @param driver
    *           - der Webdriver
    * @param elementIdMitIndexPlatzhalter
    *           - Die ID des Elements mit index Platzhalter, z.B.
    *           zapAnwendungFormId:vorgangsSucheTabelle:%s:vorgangOeffnenBearbeitenButton, wobei %s dann durch den index
    *           ersetzt wird.
    * @return {@link WebTableElement}
    */
   public static WebTableElement mit(final WebDriver driver, final String elementIdMitIndexPlatzhalter)
   {
      return new WebTableElement(driver, null, elementIdMitIndexPlatzhalter);
   }

   /**
    * Privater Basis Konstruktor
    * 
    * @param driver
    * @param webtableId
    * @param elementIdMitIndexPlatzhalter
    */
   private WebTableElement(final WebDriver driver, final String webtableId, final String elementIdMitIndexPlatzhalter)
   {
      checkPlatzhalter(elementIdMitIndexPlatzhalter);
      this.driver = driver;
      this.webtableId = webtableId;
      if (webtableId == null)
      {
         this.webtableId = elementIdMitIndexPlatzhalter.split(":%s")[0] + "_data";
      }
      this.elementIdMitIndexPlatzhalter = elementIdMitIndexPlatzhalter;
   }

   /**
    * @param driver
    *           - der zu verwenden ist
    */
   @Override
   public void setDriver(WebDriver driver)
   {
      this.driver = driver;
   }

   /**
    * Klicke auf ein Element, das in einer Zeile mit dem gesuchten Text ist
    * 
    * @param gesuchteSpaltenInZeile
    *           - Wenn nur ein Paramater übergeben wird, dann wird dieser mit 'contains' in der Zeile gesucht. Ansonsten
    *           müssen die gesuchten Spalten exakt passen.
    */
   public void click(final String... gesuchteSpaltenInZeile)
   {
      WebBaseElement guiElement = guiElement(gesuchteSpaltenInZeile);
      guiElement.waitForEnable();
      guiElement.scrollToElement();
      guiElement.clickAndWaitForPage();
   }

   /**
    * Hole das Element, das in einer Zeile mit dem gesuchten Text ist
    * 
    * @param gesuchteSpaltenInZeile
    *           - Wenn nur ein Paramater übergeben wird, dann wird dieser mit 'contains' in der Zeile gesucht. Ansonsten
    *           müssen die gesuchten Spalten exakt passen.
    */
   public WebBaseElement guiElement(final String... gesuchteSpaltenInZeile)
   {
      checkGesuchteSpaltenInZeile(gesuchteSpaltenInZeile);
      checkDriver();
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      WebTable webTable = new WebTable(driver, By.id(webtableId));
      checkWebTable(webTable);
      int index = findIndex(webTable, gesuchteSpaltenInZeile);
      WebBaseElement guiElement = new WebBaseElement(By.id(String.format(elementIdMitIndexPlatzhalter, index)), driver);
      return guiElement;
   }

   /**
    * Prüft, ob das Element für die angegebenen Parameter aktiviert oder deaktiviert ist.
    * 
    * @param gesuchteSpaltenInZeile
    *           - Wenn nur ein Paramater übergeben wird, dann wird dieser mit 'contains' in der Zeile gesucht. Ansonsten
    *           müssen die gesuchten Spalten exakt passen.
    * @return true, wenn das Element aktiv ist, ansonsten false
    */
   public boolean isEnabledFor(final String... gesuchteSpaltenInZeile)
   {
      WebBaseElement guiElement = guiElement(gesuchteSpaltenInZeile);
      guiElement.waitForVisible();
      return guiElement.isEnabled();
   }

   private void checkPlatzhalter(final String elementIdMitIndexPlatzhalter)
   {
      if (!elementIdMitIndexPlatzhalter.contains("%s"))
      {
         throw new IllegalArgumentException("Der elementIdMitIndexPlatzhalter ohne %s funktioniert nicht!");
      }
   }

   private void checkWebTable(WebTable webTable)
   {
      try
      {
         webTable.checkExists();
      }
      catch (NoSuchElementException noSuchElementException)
      {
         Assert.fail(String.format("Die Tabelle mit der ID '%s' wurde nicht gefunden", webtableId),
               noSuchElementException);
      }
   }

   private int findIndex(WebTable webTable, final String... gesuchteSpaltenInZeile)
   {
      int index = -1;
      try
      {
         if (gesuchteSpaltenInZeile.length == 1)
         {
            index = webTable.getRowContainsIndexOrFail(gesuchteSpaltenInZeile[0]);
         }
         else
         {
            index = webTable.getRowIndexOrFail(gesuchteSpaltenInZeile);
         }
      }
      catch (TableRowNotFoundException e)
      {
         Assert.fail("Konnte WebTableButton nicht finden für " + Arrays.asList(gesuchteSpaltenInZeile), e);
      }
      return index;
   }

   private void checkDriver()
   {
      if (driver == null)
      {
         throw new RuntimeException("Kein Webdriver gefunden. Bitte richtig initialisieren");
      }
   }

   private void checkGesuchteSpaltenInZeile(final String... gesuchteSpaltenInZeile)
   {
      if (gesuchteSpaltenInZeile.length == 0)
      {
         throw new IllegalArgumentException("WebTableButton ohne gesuchteSpaltenInZeile funktioniert nicht!");
      }
   }

}
