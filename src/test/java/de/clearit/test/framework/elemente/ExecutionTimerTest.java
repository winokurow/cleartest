package de.clearit.test.framework.elemente;

import java.io.File;
import java.io.FileNotFoundException;
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

import de.clearit.test.common.DateiUtils;
import de.clearit.test.common.TestUtils;
import de.clearit.test.framework.ErrorBoxUtil;
import de.clearit.test.framework.ExecutionTimer;

public class ExecutionTimerTest
{
	   @Test
	   public void testInitProfileOff()
	   {
	         DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	         Date date = new Date();
	         final String umgebung = System.getProperty("umgebung", "local");
	         Path folder = Paths.get("target", "Profiler", dateFormat.format(date) + "_" + umgebung);
	         try {
				DateiUtils.cleanDirectory(folder.toFile());
	         Files.delete(folder);
				} catch (IOException|IllegalArgumentException e) {
				}
	         
		   System.setProperty("profiler", "false");
		   ExecutionTimer timer = new ExecutionTimer();
		   timer.init("timmm");

	         Assert.assertFalse(Files.exists(folder));
	   }
	   @Test
	   public void testInitProfileOn()
	   {
		   ExecutionTimer timer = new ExecutionTimer();
		   System.setProperty("profiler", "true");
		   timer.init("timmm");
	         DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	         Date date = new Date();

	         final String umgebung = System.getProperty("umgebung", "local");
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
	         Assert.assertEquals(list.get(0), "Datum;Start Seite;Aktion;End Seite;Ladedauer (ms)");
			} catch (IOException e) {
				e.printStackTrace();
			}
	   }
	   
	   @Test
	   public void testStartProfileOn()
	   {
		   ExecutionTimer timer = new ExecutionTimer();
		   System.setProperty("profiler", "true");
		   timer.init("timmm");
		   timer.start("goto Maske2");
		   TestUtils.sleep(1000);
		   timer.end("testMaske2");
		   timer.start("goto Maske3");
		   TestUtils.sleep(1000);
		   timer.end("testMaske3");
		   Assert.assertTrue(timer.duration()>0);
	         DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	         Date date = new Date();
	         final String umgebung = System.getProperty("umgebung", "local");
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
	         Assert.assertEquals(list.size(), 3);
	         Assert.assertTrue(list.get(1).contains(";;goto Maske2;testMaske2;"));
	         Assert.assertFalse(list.get(1).split(";")[4].isEmpty());
	         Assert.assertTrue(list.get(2).contains(";testMaske2;goto Maske3;testMaske3;"));
	         Assert.assertFalse(list.get(2).split(";")[4].isEmpty());
			} catch (IOException e) {
				e.printStackTrace();
			}

	   }
	   
	   @Test
	   public void testStartProfileOff()
	   {
		   DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
	         Date date = new Date();

	         final String umgebung = System.getProperty("umgebung", "local");
	         Path folder = Paths.get("target", "Profiler", dateFormat.format(date) + "_" + umgebung);
	         try {
				DateiUtils.cleanDirectory(folder.toFile());
	         Files.delete(folder);
				} catch (IOException|IllegalArgumentException e) {
				}
	         
		   ExecutionTimer timer = new ExecutionTimer();
		   System.setProperty("profiler", "false");
		   timer.init("timmm1");
		   timer.start("goto Maske2");
		   
		   TestUtils.sleep(2000);
		
		   timer.end("testMaske2");
		   Assert.assertTrue(timer.duration()==0);
		   
	         
	         Assert.assertFalse(Files.exists(folder));
	   }
	   
	   
	   
	   private WebDriver initialize(String page)
	   {
	      WebDriver driver = new HtmlUnitDriver(true);
	      File file = new File("src/test/resources/" + page + ".html");
	      driver.get("file:///" + file.getAbsolutePath().replace("\\", "/"));
	      return driver;
	   }
	   
   
}
