package de.clearit.test.framework.elemente;

import java.io.File;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import de.clearit.test.framework.ErrorBoxUtil;

public class GuiElementTest
{

	@Test
	   public void testKonstructor()
	   {
	      WebDriver driver = new HtmlUnitDriver();
	      WebBaseElement element = new WebBaseElement(By.id("test"), driver, "Beschreibung");
	      Assert.assertEquals(element.getDescription(), "Beschreibung");
	   }

	   @Test
	   public void testIsPresent()
	   {
	      WebDriver driver = initialize("pageCheckbox");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver, "Beschreibung");
	      element1.checkElement();
	      Assert.assertTrue(element1.isPresent());
	      WebBaseElement element2 = new WebBaseElement(By.id("erstename"), driver, "Beschreibung");
	      Assert.assertFalse(element2.isPresent());
	   }

	   @Test
	   public void testIsVisible()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver, "Beschreibung");
	      element1.waitForVisible();
	      Assert.assertTrue(element1.isVisible());
	   }

	   @Test
	   public void testWaitForVisible()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver, "Beschreibung");
	      element1.waitForVisible(1L);
	      Assert.assertTrue(element1.isVisible());
	   }

	   @Test
	   public void testWaitForEnable()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("disableButton"), driver);
	      element1.click();
	      WebBaseElement element2 = new WebBaseElement(By.id("firstname"), driver, "Beschreibung");
	      element2.waitForEnable();
	      Assert.assertTrue(element2.isEnabled());
	   }

	   @Test
	   public void testWaitForDisable()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("disableButton"), driver);
	      element1.click();
	      WebBaseElement element2 = new WebBaseElement(By.id("lastname"), driver, "Beschreibung");
	      element2.waitForDisable();
	      Assert.assertFalse(element2.isEnabled());
	   }

	   @Test
	   public void testClickAndWaitForPage()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("disableButton"), driver);
	      element1.clickAndWaitForPage();
	   }

	   @Test
	   public void testSendKeys()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      Assert.assertEquals(element1.getValue(), "sdsdsd");
	   }

	   @Test
	   public void testSendKeysEmpty()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys();
	      Assert.assertEquals(element1.getValue(), "");
	   }

	   @Test
	   public void testtypeWithError()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("invisible"), driver);
	      element1.sendkeys("sdsdsd");
	   }

	   @Test
	   public void testtypeWithoutClear()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.typeWithoutClear("ru");
	      Assert.assertEquals(element1.getValue(), "sdsdsdru");
	   }

	   @Test
	   public void testtypeSlowWithoutClear()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.typeSlowWithoutClear("ru");
	      Assert.assertEquals(element1.getValue(), "sdsdsdru");
	   }

	   @Test
	   public void testtypeSlowWithoutClear2()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.typeSlowWithoutClear("");
	      Assert.assertEquals(element1.getValue(), "sdsdsd");
	   }

	   @Test
	   public void testTypeSlowWithoutClear()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.typeSlowWithoutClear("ru");
	      Assert.assertEquals(element1.getValue(), "sdsdsdru");
	   }

	   @Test
	   public void testTypeSlowWithClearAndWithEnter()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.typeSlowWithClearAndWithEnter("ru");
	      Assert.assertEquals(element1.getValue(), "");
	   }

	   @Test
	   public void testClearInput1()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.clearInput();
	      Assert.assertEquals(element1.getValue(), "");
	   }

	   @Test
	   public void testClearInput2()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.clearInput(1);
	      Assert.assertEquals(element1.getValue(), "sdsds");
	   }

	   @Test
	   public void testClearInput3()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.clearInput(0);
	      Assert.assertEquals(element1.getValue(), "sdsdsd");
	   }

	   @Test
	   public void testClearInput4()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      element1.clearInput(-1);
	      Assert.assertEquals(element1.getValue(), "sdsdsd");
	   }

	   @Test
	   public void testGetValueForInvisibleElement()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("invisible"), driver);
	      Assert.assertEquals(element1.getValueForInvisibleElement(), "test");
	   }

	   @Test
	   public void testGetValue()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("firstname"), driver);
	      element1.sendkeys("sdsdsd");
	      Assert.assertEquals(element1.getValue(), "sdsds");
	   }

	   @Test
	   public void testOpenMenu()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("invisible"), driver);
	      element1.openMenu();
	      Assert.assertTrue(element1.isVisible());
	   }

	   @Test
	   public void testMouseOver()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("mouseover"), driver);
	      element1.mouseOverClick();
	      WebBaseElement element2 = new WebBaseElement(By.id("hidden"), driver, "Beschreibung");
	      Assert.assertTrue(element2.isVisible());
	   }

	   @Test
	   public void testGetInvisibleText()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("hidden"), driver, "Beschreibung");
	      Assert.assertEquals(element1.getText(), "");
	   }
	   
	   @Test
	   public void testGetVisibleText()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("visibletext"), driver, "Beschreibung");
	      Assert.assertEquals(element1.getText(), "visibtext");
	   }
	   
	   @Test
	   public void testGetInvisibleTextOrNull()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("hid4den"), driver, "Beschreibung");
	      String text = element1.getTextOrNull();
	      Assert.assertTrue(text == null);
	   }
	   
	   @Test
	   public void testGetVisibleTextOrNull()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("visibletext"), driver, "Beschreibung");
	      Assert.assertEquals(element1.getTextOrNull(), "visibtext");
	   }
	   
	   @Test
	   public void testIsReadOnlyAndDisable()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("lastname"), driver, "Beschreibung");
	      Assert.assertTrue(element1.isReadOnlyAndDisable());
	   }
	   
	   @Test
	   public void testIsDisable()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("lastname"), driver, "Beschreibung");
	      Assert.assertTrue(element1.isDisabled());
	   }
	   
	   @Test
	   public void testIsDisabledAttribute()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("lastname"), driver, "Beschreibung");
	      Assert.assertTrue(element1.isDisabledAttribute());
	   }
	   
	   @Test
	   public void testGetAttribute()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("visibletext"), driver, "Beschreibung");
	      Assert.assertEquals(element1.getAttribute("attr"), "test");
	   }
	   
	   @Test
	   public void testScrollToElement()
	   {
	      WebDriver driver = initialize("page1");
	      WebBaseElement element1 = new WebBaseElement(By.id("distanttext"), driver, "Beschreibung");
	      element1.scrollToElement();
	      Assert.assertTrue(element1.isVisible());
	   }
	   
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
}
