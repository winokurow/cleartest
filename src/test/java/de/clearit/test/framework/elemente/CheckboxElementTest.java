package de.clearit.test.framework.elemente;

import java.io.File;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import de.clearit.test.framework.ErrorBoxUtil;

public class CheckboxElementTest
{

	@Test
	   public void testCheck()
	   {
	      WebDriver driver = initialize("pageCheckbox");
	      CheckboxElement element1 = new CheckboxElement(By.id("check"), driver);
	      element1.check();
	      
	      Assert.assertTrue(element1.isChecked());
	      
	      element1.check();
	      
	      Assert.assertTrue(element1.isChecked());
	   }

	@Test
	   public void testUncheck()
	   {
	      WebDriver driver = initialize("pageCheckbox");
	      CheckboxElement element1 = new CheckboxElement(By.id("uncheck"), driver);
	      element1.uncheck();
	      
	      Assert.assertFalse(element1.isChecked());
	      
	      element1.uncheck();
	      
	      Assert.assertFalse(element1.isChecked());
	   }
	   
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
}
