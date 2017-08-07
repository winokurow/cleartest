package de.clearit.test.framework.elemente;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;

import de.clearit.test.framework.ErrorBoxUtil;
import de.clearit.test.framework.ExecutionTimer;
import de.clearit.test.framework.ExecutionTimerManager;
import de.clearit.test.pages.HinweisPage;

public class PageObjectTest
{
	   @Test
	   public void testWaitForMainElementsIsShown()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForMainElementsIsShown();
	   }
	   
	   @Test
	   public void testWaitForPageIsLoad()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForPageIsLoad();
	   }
	   
	   @Test
	   public void testWaitForPageIsLoadWithTimeout()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForPageIsLoad(1000);
	   }
	   
	   @Test
	   public void testWaitForPageIsReady()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForPageIsReady();
	   }
	   
	   @Test
	   public void testWaitForMainElemensIsShown()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForMainElementsIsShown(-1);
	   }
	   
	   @Test
	   public void testWaitForMainElemensIsShown100()
	   {
		   WebDriver driver = initialize("page3");
	     TestPage testPage = new TestPage(driver);
	     testPage.waitForMainElementsIsShown(100);
	   }
	   
	   @Test
	   public void testWaitForMainElemensIsShownHinweis()
	   {
		   WebDriver driver = initialize("page3");
		   HinweisPage testPage = new HinweisPage(driver, "dialog");
		   testPage.waitForMainElementsIsShown(100);
	   }
	   
	   @Test
	   public void testClickWithPageChange()
	   {
		   WebDriver driver = initialize("page3");
		   System.setProperty("profiler", "true");
		   
	     TestPage testPage = new TestPage(driver);
	     testPage.clickWithPageChange(new GuiElement(By.id("navigationLink"),  driver), "Tstsprung");
	     Test2Page test2Page = new Test2Page(driver);
	     final String umgebung = System.getProperty("umgebung", "local");
	     DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	     Date date = new Date();
         Path folder = Paths.get("target", "Profiler", dateFormat.format(date) + "_" + umgebung);
         Assert.assertTrue(Files.exists(folder));
         
	   String fileName = "timmm_Profiler.csv";
         Path file = folder.resolve(fileName);
         Scanner s;
		try {
			s = new Scanner(file).useDelimiter(System.lineSeparator());

         ArrayList<String> list = new ArrayList<String>();
         while (s.hasNext()){
             list.add(s.next());
         }
         s.close();
         Assert.assertEquals(list.size(), 2);
         Assert.assertTrue(list.get(1).contains("Tstsprung;Seite2;"));
         Assert.assertFalse(list.get(1).split(";")[4].isEmpty());
		} catch (IOException e) {
			e.printStackTrace();
		}
	   }
	   
	   
	   
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   
   
}
