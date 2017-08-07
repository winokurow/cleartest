package de.clearit.test.framework.elemente;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import de.clearit.test.common.SeitenladePruefer;
import de.clearit.test.exceptions.TableRowNotFoundException;
import de.clearit.test.framework.WebDriverInjectable;

/**
 * WebTable.
 * 
 * @author Ilja Winokurow
 */
public class WebTable implements WebDriverInjectable
{
   /* Logger */
   static Logger logger = Logger.getLogger("WebTable");

   /* Webdriver Instance */
   protected WebDriver driver;

   private By dataElement;

   /**
    * Constructor. Ohne WebDriver, nur zur Verwendung in PageObjekten
    * 
    * @param dataElement
    *           - Element, welches die Rows enthält
    */
   public WebTable(By dataElement)
   {
      this(null, dataElement);
   }

   /**
    * Constructor.
    * 
    * @param driver
    *           - WebDriver
    * @param dataElement
    *           - Element, welches die Rows enthält
    */
   public WebTable(final WebDriver driver, By dataElement)
   {
      this.driver = driver;
      this.dataElement = dataElement;
   }

   /**
    * Klickt eine tr an die zeileContains beinhaltet und verwendet das data-ri attribut
    * 
    * @param zeileContains
    *           - die Nummer der Zeile
    * @param strgGedrueckt
    *           - ob die Taste 'Strg' gedruckt werden soll
    * 
    * @throws TableRowNotFoundException
    */
   public void clickRowContains(String zeileContains, boolean strgGedrueckt) throws TableRowNotFoundException
   {
      clickRowContains(zeileContains, null, strgGedrueckt);
   }

   /**
    * Klickt eine tr an die zeileContains beinhaltet und verwendet das data-ri attribut. Darun wird auf das Element by
    * geklickt.
    * 
    * @param zeileContains
    * @param elementInRow
    *           - das Element zu klicken (zum Beispiel Button). Wenn null dann klicken die Zeile selbst.
    * @param strgGedrueckt
    *           - ob die Taste 'Strg' gedruckt werden soll
    * @throws TableRowNotFoundException
    */
   public void clickRowContains(String zeileContains, By elementInRow, boolean strgGedrueckt)
         throws TableRowNotFoundException
   {
      int index = getRowContainsIndexOrFail(zeileContains);
      WebElement row = driver.findElement(dataElement).findElements(By.tagName("tr")).get(index);
      new Actions(driver).moveToElement(row).build().perform();
      if (strgGedrueckt)
      {
         new Actions(driver).keyDown(Keys.CONTROL).perform();
      }
      if (elementInRow != null)
      {
         row.findElement(elementInRow).click();
      }
      else
      {
         row.click();
      }
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      if (strgGedrueckt)
      {
         new Actions(driver).keyUp(Keys.CONTROL).perform();
      }
   }

   /**
    * Tabelle Inhalt auslesen.
    * 
    * @return Liste von WebTableRow mit einzelnen Spalten
    */
   public List<WebTableRowWithColumns> getRowsAndColumns()
   {
      final List<WebTableRowWithColumns> webTableRows = new ArrayList<>();
      List<WebElement> rows = getRows();
      for (WebElement row : rows)
      {
         WebTableRowWithColumns webTableRow = addWebTableRow(webTableRows);
         List<WebElement> columns = row.findElements(By.tagName("td"));
         addColumns(webTableRow, columns);
      }

      return webTableRows;
   }

   /**
    * Tabelle Inhalt als WebElement auslesen.
    * 
    * @return Liste von Zeilen
    */
   public List<WebElement> getTableRowsAsElements()
   {
      WebElement tableBody = driver.findElement(dataElement);
      List<WebElement> elements = tableBody.findElements(By.cssSelector("tr"));
      return elements;
   }

   /**
    * Findet eine Zeile nach gesuchten Spalten
    * 
    * @param gesuchteSpalten
    * @return WebTableRowWithColumns eine Zeile
    * @throws TableRowNotFoundException
    */
   public WebTableRowWithColumns getColumnsByRowContains(String... gesuchteSpalten) throws TableRowNotFoundException
   {
      List<WebTableRowWithColumns> rowsAndColumns = getRowsAndColumns();
      int findeZeile = WebTableUtil.findeZeile(rowsAndColumns, gesuchteSpalten);
      checkZeilenIndex(rowsAndColumns, findeZeile, gesuchteSpalten);
      return rowsAndColumns.get(findeZeile);
   }

   protected WebTableRowWithColumns addWebTableRow(final List<WebTableRowWithColumns> webTableRows)
   {
      WebTableRowWithColumns webTableRow = new WebTableRowWithColumns();
      webTableRows.add(webTableRow);
      return webTableRow;
   }

   /**
    * Ganze Tabelle Inhalt auslesen.
    * 
    * @return Liste von Zeilen als Text
    */
   public List<String> getRowsText()
   {
      return getRowsText("");
   }

   /**
    * Tabelle Inhalt auslesen.
    * 
    * @param zeileContains
    *           - der Suchparameter
    * 
    * @return Liste von Zeilen als Text in denen "zeileContains" text vorkommt
    */
   public List<String> getRowsText(String zeileContains)
   {
      List<String> results = new ArrayList<>();
      final List<WebElement> rows = getRows();
      for (WebElement row : rows)
      {
         String text = row.getText().trim();
         text = text.replaceAll("\n\r", "");
         text = text.replaceAll("\r", "");
         text = text.replaceAll("\n", "");
         text = text.replaceAll("ui-button", "");
         if (text.contains(zeileContains))
         {
            results.add(text);
         }
      }
      return results;
   }

   /**
    * Tabelle Inhalt auslesen.
    * 
    * @param zeileStartsWith
    *           - Die Anfangszeichen der Zeile
    * 
    * @return Liste von Zeilen als Text die mit "zeileStartsWith" beginnen
    */
   public List<String> getRowsTextStartsWith(String zeileStartsWith)
   {
      List<String> results = getRowsText("");
      List<String> ergebnis = new ArrayList<>();
      for (String result : results)
      {
         if (result.startsWith(zeileStartsWith))
         {
            ergebnis.add(result);
         }
      }
      return ergebnis;
   }

   private List<WebElement> getRows()
   {
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      final WebElement tableElementContainingRows = driver.findElement(dataElement);
      final List<WebElement> rows = tableElementContainingRows.findElements(By.tagName("tr"));
      return rows;
   }

   /**
    * @param zeileContains
    *           - Text der gesucht wird, Leerzeichen werden zur Suche wegoptimiert
    * @return den Zeilen index, sonst eine Fehlermeldung
    * @throws TableRowNotFoundException
    */
   public int getRowContainsIndexOrFail(String zeileContains) throws TableRowNotFoundException
   {
      List<String> rows = getRowsText();
      int zeilenIndex = WebTableUtil.findeZeile(rows, zeileContains);
      checkZeilenIndex(zeileContains, rows, zeilenIndex);
      return zeilenIndex;
   }

   /**
    * @param zeileContains
    *           - Text der gesucht wird, Leerzeichen werden zur Suche wegoptimiert
    * @return den zeilen index, sonst -1
    */
   public int getRowContainsIndex(String zeileContains)
   {
      return WebTableUtil.findeZeile(getRowsText(), zeileContains);
   }

   /**
    * @param gesuchteSpalten
    * @return den Zeilen index der gesuchten Spalten, sonst eine Fehlermeldung
    * @throws TableRowNotFoundException
    */
   public int getRowIndexOrFail(String... gesuchteSpalten) throws TableRowNotFoundException
   {
      List<WebTableRowWithColumns> rowsAndColumns = getRowsAndColumns();
      int zeilenIndex = getRowIndex(rowsAndColumns, gesuchteSpalten);
      checkZeilenIndex(rowsAndColumns, zeilenIndex, gesuchteSpalten);
      return zeilenIndex;
   }

   /**
    * @param gesuchteSpalten
    * @return den zeilen index der gesuchten Spalten, sonst -1
    */
   public int getRowIndex(String... gesuchteSpalten)
   {
      int zeilenIndex = WebTableUtil.findeZeile(getRowsAndColumns(), gesuchteSpalten);
      return zeilenIndex;
   }

   private int getRowIndex(List<WebTableRowWithColumns> rowsAndColumns, String... gesuchteSpalten)
   {
      int zeilenIndex = WebTableUtil.findeZeile(rowsAndColumns, gesuchteSpalten);
      return zeilenIndex;
   }

   private void checkZeilenIndex(List<WebTableRowWithColumns> rowsAndColumns, int zeilenIndex,
         String... gesuchteSpalten) throws TableRowNotFoundException
   {
      if (zeilenIndex == -1)
      {
         throw WebTableUtil.createTableRowNotFoundException(rowsAndColumns, gesuchteSpalten);
      }
   }

   private void addColumns(WebTableRowWithColumns webTableRow, List<WebElement> columns)
   {
      for (WebElement column : columns)
      {
         webTableRow.insertColumn(column.getText());
      }
   }

   /**
    * @return true wenn die Tabelle Zeilen hat
    */
   public boolean hasRows()
   {
      return !getRowsAndColumns().isEmpty();
   }

   /**
    * throws NoSuchElementException wenn nicht gefunden
    */
   public void checkExists()
   {
      SeitenladePruefer.mitDriver(driver).waitForPageIsLoad();
      driver.findElement(dataElement); // throws NoSuchElementException
   }

   /**
    * @param driver
    *           - der WebDriver der verwendet werden soll
    */
   @Override
   public void setDriver(WebDriver driver)
   {
      this.driver = driver;
   }

   /**
    * getRowIdByHtmlContains.
    * 
    * ID der Zeile auslesen.
    * 
    * @param textToSearchFor
    *           - Der Text nach den die Zeile gesucht wird.
    * 
    * @return ID
    */
   public String getRowIdByHtmlContains(String textToSearchFor)
   {
      WebElement tableBody = driver.findElement(dataElement);
      List<WebElement> elements = tableBody.findElements(By.cssSelector("tr"));
      String result = "";
      for (WebElement el : elements)
      {
         String temp = el.getAttribute("outerHTML");
         if (temp.contains(textToSearchFor))
         {
            result = el.getAttribute("id");
         }
      }
      return result;
   }

   /**
    * Rausfinden ob die bestimmte Zeile in der Tabelle existiert.
    * 
    * @param nodeIdToSearchFor
    *           - Der Text nach den gesucht wird.
    * 
    * @return ob gefunden ist
    */
   public boolean isRowExists(String nodeIdToSearchFor)
   {
      boolean isFound = false;
      WebElement tableBody = driver.findElement(dataElement);
      List<WebElement> elements = tableBody.findElements(By.cssSelector("tr"));
      for (WebElement el : elements)
      {
         String temp = el.getAttribute("outerHTML");
         if (temp.contains(nodeIdToSearchFor))
         {
            isFound = true;
         }
      }
      return isFound;
   }

   private void checkZeilenIndex(String zeileContains, List<String> rows, int zeilenIndex)
         throws TableRowNotFoundException
   {
      if (zeilenIndex == -1)
      {
         throw new TableRowNotFoundException(
               "nicht gefunden in Tabelle: " + zeileContains + "\n" + "Rows waren:\n" + rows);
      }
   }
}
