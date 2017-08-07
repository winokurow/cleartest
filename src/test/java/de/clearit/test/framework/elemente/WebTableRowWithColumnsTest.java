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

public class WebTableRowWithColumnsTest
{

	@Test
	   public void testGetColumns() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      WebTableRowWithColumns webTableRowWithColumns = table.getColumnsByRowContains("Zeile13");

	      Assert.assertEquals(webTableRowWithColumns.getColumns().get(0), "Zeile13");
	      
	   }

	@Test
	   public void testHasColumns() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      WebTableRowWithColumns webTableRowWithColumns = table.getColumnsByRowContains("Zeile13");

	      Assert.assertTrue(webTableRowWithColumns.hasColumns());
	      
	   }
	
	@Test
	   public void testToString() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      WebTable table = new WebTable(driver, By.id("table"));
	      WebTableRowWithColumns webTableRowWithColumns = table.getColumnsByRowContains("Zeile13");

	      Assert.assertEquals(webTableRowWithColumns.toString(), "[Zeile13, Zeile23, Zeile33, Button3]");
	      
	   }
	
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   
}
