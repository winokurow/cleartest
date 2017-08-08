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

public class WebTableElementTest
{

	@Test
	   public void testClick() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      
	      WebTableElement element = WebTableElement.mit(driver, "table", "button1%s");
	      element.click("Zeile12");
	     
	      WebBaseElement element1 = new WebBaseElement(By.id("text"), driver);
	      Assert.assertEquals(element1.getText(), "row1");
	      WebBaseElement element2 = new WebBaseElement(By.id("text2"), driver);
	      Assert.assertEquals(element2.getText(), "button1");
	      
	   }

	@Test
	   public void testGuiElement() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable");
	      
	      WebTableElement tableElement = WebTableElement.mit(driver, "table", "button1%s");
	      WebBaseElement element = tableElement.guiElement("Zeile12");
	      element.click();
	      WebBaseElement element1 = new WebBaseElement(By.id("text"), driver);
	      Assert.assertEquals(element1.getText(), "row1");
	      WebBaseElement element2 = new WebBaseElement(By.id("text2"), driver);
	      Assert.assertEquals(element2.getText(), "button1");
	   }
	
	@Test
	   public void testIsEnabledFor() throws TableRowNotFoundException
	   {
	      WebDriver driver = initialize("pageTable2");
	      
	      WebTableElement tableElement = WebTableElement.mit(driver, "table", "button1%s");
	      boolean isEnabled = tableElement.isEnabledFor("Zeile11");
	      Assert.assertFalse(isEnabled);
	   }
	
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   
}
