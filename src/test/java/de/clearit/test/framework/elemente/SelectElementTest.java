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

import de.clearit.test.framework.ErrorBoxUtil;

public class SelectElementTest
{

	@Test
	   public void testSelect()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.select("opt2");
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text2");
	   }

	@Test
	   public void testSelectNull()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.select(null);
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text1");
	      
	   }
	
	@Test
	   public void testSelectEmpty()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.select("");
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text1");
	      
	   }
	
	@Test
	   public void testSelectByText()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.selectByVisibleText("Text2");
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text2");
	   }

	@Test
	   public void testSelectByTextNull()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.selectByVisibleText(null);
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text1");
	   }
	
	@Test
	   public void testSelectByTextEmpty()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.selectByVisibleText("");
	      
	      Assert.assertEquals(element1.getTextSelectedOption(), "Text1");
	   }
	
	@Test
	   public void testGetOptionsList()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      List<WebElement> options = element1.getOptionsList();
	      
	      Assert.assertEquals(options.size(), 4);
	   }
	   
	@Test
	   public void testIsOptionActive()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      element1.select("opt2");
	      
	      Assert.assertTrue(element1.isOptionActive("opt2"));
	   }
	
	@Test
	   public void testGetOptionsTextList()
	   {
	      WebDriver driver = initialize("pageSelect");
	      SelectElement element1 = new SelectElement(By.id("selectElement"), driver);
	      List<String> options = element1.getOptionsTextList();
	      
	      Assert.assertEquals(options.size(), 4);
	   }
	
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   
	   
}
