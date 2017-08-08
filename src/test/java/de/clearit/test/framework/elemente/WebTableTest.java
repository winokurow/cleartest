package de.clearit.test.framework.elemente;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import de.clearit.test.exceptions.TableRowNotFoundException;
import de.clearit.test.framework.ErrorBoxUtil;

public class WebTableTest
{

	@Test
	   public void testClickRowContainsStrg() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      table.clickRowContains("Zeile12Zeile22", true);
	      WebBaseElement element1 = new WebBaseElement(By.id("text"), driver);
	      Assert.assertEquals(element1.getText(), "row1");
	   }
	@Test
	   public void testClickRowContains() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      table.clickRowContains("Zeile12Zeile22", false);
	      WebBaseElement element1 = new WebBaseElement(By.id("text"), driver);
	      Assert.assertEquals(element1.getText(), "row1");
	   }
	@Test
	   public void testClickRowContainsElementInRow() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      table.clickRowContains("Zeile12Zeile22", By.tagName("button"), false);
	      //GuiElement element = new GuiElement(By.id("button12"), driver);
	      //element.click();
	      WebBaseElement element1 = new WebBaseElement(By.id("text2"), driver);
	      Assert.assertEquals(element1.getText(), "button2");
	   }
	@Test
	   public void testClickRowContainsElementInRowNull() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      table.clickRowContains("Zeile12Zeile22", null, false);
	      //GuiElement element = new GuiElement(By.id("button12"), driver);
	      //element.click();
	      WebBaseElement element1 = new WebBaseElement(By.id("text"), driver);
	      Assert.assertEquals(element1.getText(), "row1");
	      WebBaseElement element2 = new WebBaseElement(By.id("text2"), driver);
	      Assert.assertEquals(element2.getText(), "visibletext");
	   }
	
	@Test
	   public void testGetRowsAndColumns() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      List<WebTableRowWithColumns> list = table.getRowsAndColumns();
	      //GuiElement element = new GuiElement(By.id("button12"), driver);
	      //element.click();
	      
	      Assert.assertEquals(list.size(), 3);
	   }
	
	@Test
	   public void testGetTableRowsAsElements() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      List<WebElement> list = table.getTableRowsAsElements();
      
	      Assert.assertEquals(list.size(), 3);
	      Assert.assertNotEquals(list.get(0), null);
	   }
	
	@Test
	   public void testGetColumnsByRowContainsTrue() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      WebTableRowWithColumns webTableRowWithColumns = table.getColumnsByRowContains("Zeile13", "Zeile23", "Zeile33", "Button3");
   
	      Assert.assertTrue(webTableRowWithColumns.hasColumns());

	   }
	
	@Test
	   public void testGetColumnsByRowContainsFalse() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      boolean isException = false;
	      try
	      {
	      table.getColumnsByRowContains("Zeile63");
	      } catch (TableRowNotFoundException e)
	      {
	    	  isException = true;
	      }
	      Assert.assertTrue(isException);

	   }
	
	
	@Test
	   public void testGetRowsText() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      List<String> zeilen = table.getRowsText();
	      Assert.assertEquals(zeilen.get(0), "Zeile11 Zeile21 Zeile31 Button1");

	   }
	
	@Test
	   public void testGetRowsTextContains() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      List<String> zeilen = table.getRowsText("Zeile33");
	      Assert.assertEquals(zeilen.size(), 1);
	      Assert.assertEquals(zeilen.get(0), "Zeile13 Zeile23 Zeile33 Button3");

	   }
	
	@Test
	   public void testGetRowsTextContainsLeer() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      List<String> zeilen = table.getRowsText("Zeile35");
	      Assert.assertEquals(zeilen.size(), 0);
	   }
	
	@Test
	   public void testGetRowsTextStartsWith() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      List<String> zeilen = table.getRowsTextStartsWith("Zeile13");
	      Assert.assertEquals(zeilen.size(), 1);
	      Assert.assertEquals(zeilen.get(0), "Zeile13 Zeile23 Zeile33 Button3");

	   }
	
	@Test
	   public void testGetRowsTextStartsWithLeer() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      List<String> zeilen = table.getRowsTextStartsWith("Zeile33");
	      Assert.assertEquals(zeilen.size(), 0);
	   }

	
	@Test
	   public void testGetRowContainsIndexOrFail() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      int position = table.getRowContainsIndexOrFail("Zeile13");
	      Assert.assertEquals(position, 2);

	   }
	
	@Test
	   public void testGetRowContainsIndex() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      int position = table.getRowContainsIndex("Zeile13");
	      Assert.assertEquals(position, 2);

	   }

	@Test
	   public void testGetRowIndexOrFail() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      int position = table.getRowIndexOrFail("Zeile13", "Zeile23");
	      Assert.assertEquals(position, 2);

	   }
	
	@Test
	   public void testGetRowIndex() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      int position = table.getRowIndexOrFail("Zeile13", "Zeile23");
	      Assert.assertEquals(position, 2);

	   }

	@Test
	   public void testHasRows() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));

	      Assert.assertTrue(table.hasRows());

	   }
	

	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   

	   
	   
}
